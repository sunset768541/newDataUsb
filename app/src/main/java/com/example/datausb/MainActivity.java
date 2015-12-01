/**
 * --利用USB HOST模式从数据采集卡接收数据
 * <p/>
 * --application 将接收的数据进行组合分类后分别利用16bit的数据计算出在相应的时间点
 * 数据采集卡上的AD芯片所采集的信号的强度，接收的是信号的强度，可以转化为int型数字
 * 设置强度为绘图坐标的纵坐标，时间为横坐标。
 * <p/>
 * -- 主要功能 数据的分类提取，数据的显示绘图，数据的储存
 * <p/>
 * --sunset 164053616@qq.com
 */
package com.example.datausb;

import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.String;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends Activity {
    private UsbManager myUsbManager;
    private UsbDevice myUsbDevice;
    private UsbInterface myInterface;
    private UsbDeviceConnection myDeviceConnection;
    private TextView deviceRecognise;
    private TextView reserve;
    private TextView deviceState;
    private EditText sendData;
    private UsbEndpoint epOut;
    private UsbEndpoint epIn;
    private TextView transmissionSpeed;
    private TextView receivedData;
    public TextView path;
    public ListView listv;
    //记录当前的父文件夹
    File currentParaent;
    //记录当前路径下所有文件的文件数组
    File[] currentFiles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceRecognise = (TextView) findViewById(R.id.deviceRecognise);
        reserve = (TextView) findViewById(R.id.reserve);
        deviceState = (TextView) findViewById(R.id.deviceState);
        transmissionSpeed = (TextView) findViewById(R.id.transmissionSpeed);
        receivedData = (TextView) findViewById(R.id.receivedData);
        myUsbManager = (UsbManager) getSystemService(USB_SERVICE);
        Button openDevice = (Button) findViewById(R.id.openDevice1);
        openDevice.setOnClickListener(new openDevice());
        Button startDataReceive = (Button) findViewById(R.id.startDataReceive);
        startDataReceive.setOnClickListener(new startDataReceive());
        Button stopDataReceive = (Button) findViewById(R.id.stopDataReceive);
        path = (TextView) findViewById(R.id.path);
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new save());
        listv = (ListView) findViewById(R.id.list);
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentFiles[position].isFile()) return;
                File[] tmp = currentFiles[position].listFiles();
                if (tmp == null || tmp.length == 0) {

                    Toast.makeText(MainActivity.this, "当前路径下不可方物或没有文件", Toast.LENGTH_SHORT).show();

                } else {
                    currentParaent = currentFiles[position];
                    currentFiles = tmp;
                    inflateListView(currentFiles);
                }
            }
        });
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new back());
        stopDataReceive.setOnClickListener(new stopDataReceive());

    }

    /**
     * 储存数据获得储存路径的按钮的监听函数按钮save
     */


    class save implements View.OnClickListener {
        public void onClick(View v) {
            File root = new File("/mnt/external_sd");
            if (root.exists()) {
                currentParaent = root;
                currentFiles = root.listFiles();
                inflateListView(currentFiles);
            }


        }

    }


    /**
     * 返回按钮back的监听函数
     */
    class back implements View.OnClickListener {
        public void onClick(View v) {
            try {
                if (!currentParaent.getCanonicalPath().equals("/mnt/external_sd")) {

                    currentParaent = currentParaent.getParentFile();
                    currentFiles = currentParaent.listFiles();
                    inflateListView(currentFiles);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void inflateListView(File[] files) {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < files.length; i++) {

            Map<String, Object> listItem = new HashMap<String, Object>();
            if (files[i].isDirectory()) {
                listItem.put("icon", R.drawable.filebox);
            } else {

                listItem.put("icon", R.drawable.file);

            }
            listItem.put("fileName", files[i].getName());
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.line, new String[]{"icon", "fileName"}
                , new int[]{R.id.icco, R.id.file_name});
        listv.setAdapter(simpleAdapter);
        try {
            path.setText("当前路径为" + currentParaent.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 该handler为了确保线程安全用来更新IU
     */
    private final int COMPLETED = 0;
    private Handler handler = new Handler() {//多线程中用于UI的更新
        @Override
        public void handleMessage(Message msg1) {
            if (msg1.what == COMPLETED) {
                reserve.setText(Integer.toString(msg1.arg2));
                transmissionSpeed.setText("传输的速度：" + msg1.arg1 + "MB/S");
                receivedData.setText("USB返回的数据：" + msg1.obj);
            }
        }
    };

    /**
     * 该线程的作业是为了接收USB的数据
     */

    public class dataReceiveThread extends Thread {//接收数据的线程
        private boolean suspend = false;
        resource r;

        dataReceiveThread(resource r) {
            this.r = r;
        }

        private String control = ""; // 只是需要一个对象而已，这个对象没有实际意义

        public void setSuspend(boolean suspend) {
            if (!suspend) {
                synchronized (control) {
                    control.notifyAll();
                }
            }
            this.suspend = suspend;
        }

        public boolean isSuspend() {
            return this.suspend;
        }

        public void run() {
            while (true) {
                synchronized (control) {
                    if (suspend) {
                        try {
                            control.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                byte[] Receivebytes = new byte[4096];//接收1KB的数据
                long startTime = System.nanoTime();             // 纳秒级
                //long startTime = System.currentTimeMillis();    // 毫秒级
                int xxx = myDeviceConnection.bulkTransfer(epIn, Receivebytes, 4096, 0); //do in another thread
                //  测试的代码
                long estimatedTime = System.nanoTime() - startTime;
                int t = (int) (4000000 / estimatedTime);
                r.data = Receivebytes;
                r.speed = t;
               write(Receivebytes);

            }
        }
    }

    /**
     * dataProcess线程是将从USB接收的8bit数据合成16bit信息并显示
     */
    public class dataProcess extends Thread {//接收数据的线程
        // private  final int COMPLETED = 0;
        resource r;

        dataProcess(resource r) {

            this.r = r;
        }

        private boolean suspend = false;

        private String control = ""; // 只是需要一个对象而已，这个对象没有实际意义

        public void setSuspend(boolean suspend) {
            if (!suspend) {
                synchronized (control) {
                    control.notifyAll();
                }
            }
            this.suspend = suspend;
        }

        public boolean isSuspend() {
            return this.suspend;
        }

        public void run() {

            while (true) {
                synchronized (control) {
                    if (suspend) {
                        try {
                            control.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                int[] combination = new int[2048];//combination用于储存合并后的16bit数据
                long startTime = System.nanoTime();             // 纳秒级
                String st;//数据转化为字符串的中间变量
                String st1 = "0";//储存接收数据转化后的字符串
                int i1 = 0;
                int p;
                int p1;

                for (int i = 0; i < 4095; i = i + 2) {
                    if(r.data[i]<0){
                         p=256+r.data[i];

                    }
                    else {
                         p=r.data[i];
                    }

                    if(r.data[i+1]<0){
                        p1=(256+r.data[i+1])<<8;

                    }
                    else {
                        p1=r.data[i+1]<<8;
                    }
                   combination[i1] = p + p1;
                   // combination[i1] = r.data[i] + r.data[i+1]<<8;
                  /* st = Integer.toString(combination[i1]);
                    st1 = st1 + st + " ";*/
                    i1 = i1 + 1;
                }
                long estimatedTime = System.nanoTime() - startTime;
                Message msg1 = new Message();
                msg1.what = COMPLETED;
                msg1.obj = st1;
                msg1.arg1 = r.speed;
                msg1.arg2 = (int) estimatedTime;//arg2表示携带的处理速度信息
                handler.sendMessage(msg1);
                //write(st1);
            }
        }
    }
    private void write(byte[] content){

        try{
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                FileOutputStream raf=new FileOutputStream("/mnt/external_sd/data.txt",true);
                raf.write(content);
            raf.close();}
        }
        catch (Exception e){
            e.printStackTrace();

        }

    }

    /**
     * 这个resource类的作用是提供一个共享的数据储存区域，封装数据用于储存
     */
    class resource {
        byte[] data;
        int speed;
    }

    /**
     * 该语句为实例化一个数据接收的类，re为数据接收线程
     */
    resource r = new resource();
    public dataReceiveThread re = new dataReceiveThread(r);
    public dataProcess pro = new dataProcess(r);

    /**
     * mli为打开usb通讯的按钮的监听函数
     */
    public class openDevice implements View.OnClickListener {
        public void onClick(View v) {
Log.d("121e12e21","1");
            enumerateDevice();//打开应用时枚举设备
            findInterface();//找到设备接口
            openDevice();//打开设备
            assignEndpoint();//指派端点
        }

    }

    /**
     * md为开始数据采集按钮的监听函数
     */
    class startDataReceive implements View.OnClickListener {
        public void onClick(View v) {
            if (re.isAlive()) {
                re.setSuspend(false);
                pro.setSuspend(false);
            } else {
                re.start();
                pro.start();
            }
        }

    }

    /**
     * ms为暂停数据采集按钮的监听函数
     */
    class stopDataReceive implements View.OnClickListener {
        public void onClick(View v) {
            re.setSuspend(true);
            pro.setSuspend(true);
        }

    }

    /**
     * sentdata为一个用来发送数据的函数，当前没有启用
     */

    public void sentdata(byte[] bytes) {//发送数据的函数
        myDeviceConnection.claimInterface(myInterface, true);
        int flag = myDeviceConnection.bulkTransfer(epOut, bytes, bytes.length, 0); //do in another thread
        // info1.setText(Integer.toString(flag));//flag标记是否发送成功，成功flag>1,不成功-1

    }

    /**
     * 分配端点，IN | OUT，即输入输出；此处我直接用1为OUT端点，0为IN，当然你也可以通过判断
     */
    //USB_ENDPOINT_XFER_BULK
     /*
     #define USB_ENDPOINT_XFER_CONTROL 0 --控制传输
     #define USB_ENDPOINT_XFER_ISOC 1 --等时传输
     #define USB_ENDPOINT_XFER_BULK 2 --块传输
     #define USB_ENDPOINT_XFER_INT 3 --中断传输
     * */
    private void assignEndpoint() {
        if (myInterface != null) { //这一句不加的话 很容易报错  导致很多人在各大论坛问:为什么报错呀
            epIn = myInterface.getEndpoint(1);//设置输入的数据的端点
            epOut = myInterface.getEndpoint(0);//设置输出数据的端点
            //int num=myInterface.getEndpointCount();//可以检测当前接口上的端点数目
            //deviceState.setText("当前接口的端点的数目"+num);//用文本显示端点的个数
            /**for (int i = 0; i < myInterface.getEndpointCount(); i++) {//该for循环用来找到指定的端点类型
             UsbEndpoint ep = myInterface.getEndpoint(i);//ep代表第i个端点
             if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
             //infdeviceState.setText("找到断点"+i);//查找usb设备某个类型的端点
             if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
             deviceState.setText("该端点的类型端点号为："+i);//显示指定方向端点的标号
             } else {

             }
             }
             }*/
        }
    }

    /**
     * 打开设备
     */
    private void openDevice() {
        if (myInterface != null) {
            UsbDeviceConnection conn = null;
            // 在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限，可以查阅相关资料
            if (myUsbManager.hasPermission(myUsbDevice)) {
                conn = myUsbManager.openDevice(myUsbDevice);
                deviceState.setText("打开设备成功");
            }
            if (conn == null) {
                return;
            }
            if (conn.claimInterface(myInterface, true)) {
                myDeviceConnection = conn; // 到此你的android设备已经连上HID设备
            } else {
                conn.close();
            }
        }
    }

    /**
     * 找设备接口
     */
    private void findInterface() {
        if (myUsbDevice != null) {
            //info1.setText(Integer.toString(myUsbDevice.getInterfaceCount()));//获得当前设备的接口数目
            myInterface = myUsbDevice.getInterface(0);
            /**for (int i = 0; i < myUsbDevice.getInterfaceCount(); i++) {
             UsbInterface intf = myUsbDevice.getInterface(i);
             // 根据手上的设备做一些判断，其实这些信息都可以在枚举到设备时打印出来
             if (intf.getInterfaceClass() == 8//找到指定类型的端点
             && intf.getInterfaceSubclass() == 6
             && intf.getInterfaceProtocol() == 80) {
             myInterface = intf;
             }
             break;
             }*/
        }
    }

    /**
     * 枚举设备
     */
    private void enumerateDevice() {
        if (myUsbManager == null)
            return;
        HashMap<String, UsbDevice> deviceList = myUsbManager.getDeviceList();
        Log.d("jdjksa","sjbdjab");
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();//历遍器
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            if (device.getVendorId() == 1204 && device.getProductId() == 4099) {//根据VID,PID枚举设备
                deviceRecognise.setText("数据采集卡已接入");
                myUsbDevice = device;
            } else {
                deviceRecognise.setText("不匹配的设备");
            }
        }

    }

}
