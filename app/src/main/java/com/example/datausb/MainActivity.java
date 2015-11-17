package com.example.datausb;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.String;
import java.util.HashMap;
import java.util.Iterator;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceRecognise = (TextView) findViewById(R.id.deviceRecognise);
        reserve = (TextView) findViewById(R.id.reserve);
        deviceState = (TextView) findViewById(R.id.deviceState);
        transmissionSpeed = (TextView) findViewById(R.id.transmissionSpeed);
        receivedData = (TextView) findViewById(R.id.receivedData);
        sendData = (EditText) findViewById(R.id.sendData);
        myUsbManager = (UsbManager) getSystemService(USB_SERVICE);
        Button openDevice = (Button) findViewById(R.id.openDevice);
        openDevice.setOnClickListener(new openDevice());
        Button startDataReceive = (Button) findViewById(R.id.startDataReceive);
        startDataReceive.setOnClickListener(new startDataReceive());
        Button stopDataReceive = (Button) findViewById(R.id.stopDataReceive);
        stopDataReceive.setOnClickListener(new stopDataReceive());

    }
    private  final int COMPLETED = 0;
    private  Handler handler = new Handler() {//多线程中用于UI的更新
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                Bundle myData=msg.getData();
                byte[] rd=myData.getByteArray("data");
                int[] combination=new int[512];
                for (int i=0;i<512;i=i+2){
                    combination[i]=rd[i]+rd[i+1]<<8;
                }
                String st;
                String st1="";
                for (int i=0;i<combination.length;i++) {

                    st = Integer.toHexString(combination[i]);
                    st1=st1+st+"\b";
                }
                transmissionSpeed.setText("传输的速度：" + msg.arg1 + "MB/S");
                receivedData.setText("USB返回的数据：" + msg.obj);
            }
        }
    };

    public class MyThread extends Thread {//接收数据的线程

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
                byte[] Receivebytes = new byte[1024];//接收1KB的数据
                long startTime = System.nanoTime();             // 纳秒级
                //long startTime = System.currentTimeMillis();    // 毫秒级
                int xxx = myDeviceConnection.bulkTransfer(epIn, Receivebytes, 1024, 0); //do in another thread
                //  测试的代码
                long estimatedTime = System.nanoTime() - startTime;
                int t = (int) (1000000 / estimatedTime);
                /*int[] combination=new int[512];
                for (int i=0;i<512;i=i+2){
                    combination[i]=Receivebytes[i]+Receivebytes[i+1]<<8;
                }
                String st;
                String st1="";
               for (int i=0;i<combination.length;i++) {

                    st = Integer.toHexString(combination[i]);
                    st1=st1+st+"\b";
                }*/
               // String str = new String();
                Message msg = new Message();
                msg.what = COMPLETED;
                Bundle bb=new Bundle();
                bb.putByteArray("data", Receivebytes);
                msg.setData(bb);
                //msg.obj = Receivebytes;
                msg.arg1 = t;
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * 该语句为实例化一个数据接收的类，re为数据接收线程
     */
    public MyThread re = new MyThread();

    /**
     * mli为打开usb通讯的按钮的监听函数
     */
    public class openDevice implements View.OnClickListener {
        public void onClick(View v) {

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
            } else {
                re.start();
            }
        }

    }

    /**
     * ms为暂停数据采集按钮的监听函数
     */
    class stopDataReceive implements View.OnClickListener {
        public void onClick(View v) {
            re.setSuspend(true);
        }

    }

    /**
     * sentdata为一个用来发送数据的函数，当前没有启用
     *
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
