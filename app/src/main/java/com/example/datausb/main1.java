package com.example.datausb;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;

/**
 * Created by sunset on 15/12/11.
 */

public class main1 extends Activity {
    /**
     * usb的操作所需要的组件
     */
    private UsbManager myUsbManager;
    private UsbDevice myUsbDevice;
    private UsbInterface myInterface;
    private UsbDeviceConnection myDeviceConnection;
    private UsbEndpoint epOut;
    private UsbEndpoint epIn;
    /**
     * 屏幕下方显示相关信息的部分
     */
    private TextView usbstate;
    private TextView datasave;
    private TextView transmmitespeed1;
    /**
     * 实例化3个用于显示不同界面的Fragment
     * da为数据显示界面
     * db为数据标定界面
     * dc为温度和距离的显示界面
     */
    dataModel da = new dataModel();
    calibrateModel db = new calibrateModel();
    tempreatureModel dc = new tempreatureModel();
    systemSeting ss=new systemSeting();

    /**
     * 定义用于携带数据的Bundle
     */
    Bundle data_a = new Bundle();//携带通道A的数据Bundle
    Bundle data_a1 = new Bundle();//携带通道A1的数据的Bundle
    Bundle data_b = new Bundle();//携带通道B的数据Bundle
    Bundle data_b1 = new Bundle();//携带通道B1的数据Bundl

    /**
     *preferences为读取参数的SharePreference的实例
     * editor为修改参数的Shareferecxes.Editor的实例
     * @param savedInstanceState
     */
     SharedPreferences preferences;
     SharedPreferences.Editor editor;
    int oplong;
    int fragmentnumber;
    private EditText oplong1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean b = this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.mainactivity);
        /**
         * 实例化3个按钮用来切换不同的Fragment
         * datamodle用来切换到数据显示模式
         * calibration用于切换到标定模式
         * temperature用来切换到温度距离显示模式
         * 并为按钮添加监听函数
         */
        Button datamodle = (Button) findViewById(R.id.button);
        datamodle.setOnClickListener(new rd());
        Button calibration = (Button) findViewById(R.id.button2);
        calibration.setOnClickListener(new ca());
        Button temperature = (Button) findViewById(R.id.button3);
        temperature.setOnClickListener(new te());
        Button systemSetingselect=(Button)findViewById(R.id.button6);
        systemSetingselect.setOnClickListener(new ss());
        /**
         *
         */
        ToggleButton togglebutton = (ToggleButton) findViewById(R.id.toggleButton);
        togglebutton.setOnClickListener(new tg(togglebutton));
        myUsbManager = (UsbManager) getSystemService(USB_SERVICE);
        usbstate = (TextView) findViewById(R.id.usbstate);
        datasave = (TextView) findViewById(R.id.savestate);
        oplong1=(EditText)findViewById(R.id.editText5);
        transmmitespeed1 = (TextView) findViewById(R.id.transmitespeed1);
        preferences=getSharedPreferences("opl",MODE_PRIVATE);
        editor=preferences.edit();
        int oplong=preferences.getInt("long",0);
        if (oplong==0){
            Toast.makeText(getApplicationContext(),"还没有设置光纤的长度，请先到系统设置中设置",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 更新UI数据
     */
    private final int COMPLETED = 0;
    private Handler handler = new Handler() {//多线程中用于UI的更新
        @Override
        public void handleMessage(Message msg1) {
            if (msg1.what == COMPLETED) {
                try {
                    transmmitespeed1.setText("shuj" + msg1.obj);
                } catch (NullPointerException e) {
                }
            }
        }
    };

    /**
     * 按钮datamodle的监听函数
     * 每个FragmentTransanction的commit只能提交一次，所以我们在按下每个按钮时从新实例化一个FragmentTransanction进行提交操作
     */
    class rd implements View.OnClickListener {
        public void onClick(View v) {
            fragmentnumber=0;
            // setchange(false);
            oplong=preferences.getInt("long",0);
            if (oplong==0){
                Toast.makeText(getApplicationContext(),"还没有设置光纤的长度，请先到系统设置中设置",Toast.LENGTH_SHORT).show();
            }
            else {

                    re.setSuspend(true);
                    pro.setSuspend(true);

                setstopcalibratemodelthred(true);
                setstoptemprturemodelthred(true);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contineer, da, "datamodel");//datamodel为fragment的tag值，用来在数据处理线程中找到当前的fragment
                //transaction.addToBackStack(null);
                //setchange(true);
                setstopdatamodelthred(false);
                transaction.commit();
                re.setSuspend(false);
                pro.setSuspend(false);
            }
        }


    }

    boolean bb = false;

    public boolean getchange() {
        return bb;
    }

    public void setchange(boolean cc) {
        bb = cc;
        // return bb;
    }

    public void wakeuppro() {
        dta.notifyAll();
    }

    /**
     * 在fragment中通过判断stopdatamodelthread来确定是否终止线程
     * setstopdatamodelthred（）方法用来在模式切换按钮按下时改变stopdatamodelthread状态使得该线程终止
     */
    boolean stopdatamodelthread=false;
    public boolean setstopdatamodelthred(boolean kk){
        stopdatamodelthread=kk;
        return kk;
    }
    boolean stopcalibratemodelthread=false;
    public boolean setstopcalibratemodelthred(boolean kk){
        stopcalibratemodelthread=kk;
        return kk;
    }
    boolean stoptempreturemodelthread=false;
    public boolean setstoptemprturemodelthred(boolean kk){
        stoptempreturemodelthread=kk;
        return kk;
    }
    /**
     * 按钮calibration的监听函数
     */
    class ca implements View.OnClickListener {
        public void onClick(View v) {
            fragmentnumber=1;
            //setchange(false);
            oplong=preferences.getInt("long",0);
            if (oplong==0){
                Toast.makeText(getApplicationContext(),"还没有设置光纤的长度，请先到系统设置中设置",Toast.LENGTH_SHORT).show();
            }
            else {
                re.setSuspend(true);
                pro.setSuspend(true);
                setstopdatamodelthred(true);
                setstoptemprturemodelthred(true);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contineer, db, "calibratemodel");
                //transaction.addToBackStack(null);//fragment压入堆栈
                //setchange(true);
                setstopcalibratemodelthred(false);
                transaction.commit();
                Log.d("l", "hha");
                re.setSuspend(false);
                pro.setSuspend(false);


            }
        }


    }

    /**
     * 按钮temperature的监听函数
     */
    class te implements View.OnClickListener {
        public void onClick(View v) {
            fragmentnumber=2;
            //setchange(false);
             oplong=preferences.getInt("long",0);
            if (oplong==0){
                Toast.makeText(getApplicationContext(),"还没有设置光纤的长度，请先到系统设置中设置",Toast.LENGTH_SHORT).show();
            }
            else {
                re.setSuspend(true);
                pro.setSuspend(true);
                setstopcalibratemodelthred(true);
                setstopdatamodelthred(true);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.contineer, dc, "tempreturemodel");
                    //transaction.addToBackStack(null);
                    //setchange(true);
                setstoptemprturemodelthred(false);
                    transaction.commit();
                    Log.d("dh", "hha");
                re.setSuspend(false);
                pro.setSuspend(false);


            }
        }


    }

    /**
     * 系统设置按键的监听函数
     */
    class ss implements View.OnClickListener {
        public void onClick(View v) {
            //setchange(false);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contineer, ss, "systemseting");
            //transaction.addToBackStack(null);
            //setchange(true);
            transaction.commit();
            Log.d("dh", "hha");
        }


    }
    /**
     * 打开设备的togglebutton监听函数
     */
    class tg implements View.OnClickListener {
        ToggleButton tgg;

        public tg(ToggleButton tgg) {
            this.tgg = tgg;

        }

        public void onClick(View v) {
            if (tgg.isChecked()) {
                //  Toast.makeText(main1.this, "你喜欢球类运动", Toast.LENGTH_SHORT).show();
                enumerateDevice();//打开应用时枚举设备
                if(myUsbDevice!=null) {
                    tgg.setBackgroundColor(Color.GREEN);
                    findInterface();//找到设备接口
                    openDevice();//打开设备
                    assignEndpoint();//指派端点
                    if (re.isAlive()) {
                        re.setSuspend(false);
                        pro.setSuspend(false);
                    } else {

                        re.start();
                        pro.start();//先不进行数据的处理
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"无法打开设备，USB设备非数据采集卡，请确认后重试",
                            Toast.LENGTH_SHORT).show();
                }

            }
            // 当按钮再次被点击时候响应的事件
            else {
                tgg.setBackgroundColor(Color.RED);
                //  Toast.makeText(main1.this, "你不喜欢球类运动", Toast.LENGTH_SHORT).show();
                re.setSuspend(true);
                pro.setSuspend(true);
            }


        }
    }

    /**
     * 设置参数的方法
     *
     */
     public boolean setpref(int opl){
         editor.putInt("long",opl);
         editor.commit();
         return true;
     }

    /**
     * 数据接收线程，负责从usb的endpoint读取数据
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

                synchronized (r) {

                    if (r.flag) {


                        try {
                            r.wait();

                        } catch (InterruptedException ex) {

                        }
                    }
                    byte[] Receivebytes = new byte[16384];//注意接收数据务必要为2的n次方，并且bulk的最大为16384，否侧会出现Fatal signal 11 (SIGSEGV)错误
                    long startTime = System.nanoTime();             // 纳秒级
                    //long startTime = System.currentTimeMillis();    // 毫秒级
                    int xxx = myDeviceConnection.bulkTransfer(epIn, Receivebytes, 16384, 0); //do in another thread
                    //  测试的代码
                    //write(Receivebytes);//将接收到的数据直接存储为2进制文件
                    long estimatedTime = System.nanoTime() - startTime;

                    //set_TubeA1_data(data_a, data_a1, data_b, data_b1);//调用传入通道A数据函数
                    Message msg1 = new Message();
                    msg1.what = COMPLETED;
                    //msg1.obj = dadd[1024] + "+" + dadd1[1024] + "+" + dadd2[1024] + "+" + dadd3[1024];
                    //msg1.obj =combination[0]+"+"+combination[20480]+"+"+combination[30720]+"+"+combination[40959];//要显示的数据，测试使用
                    msg1.obj =Receivebytes[5]+"+"+Receivebytes[9120]+"+"+Receivebytes[4448]+"+"+xxx;//要显示的数据，测试使用
                    msg1.arg1 = (int)estimatedTime;//数据的传输速度
                    msg1.arg2 = (int) estimatedTime;//arg2表示携带的处理速度信息
                    handler.sendMessage(msg1);
                    r.data = Receivebytes;//拼接好的数据传递出去
                    //r.speed = t;
                    r.flag = true;
                    r.notify();
                    Log.d("接收", "数据接收线程完成");
                }
            }

        }
    }


    /**
     * dataProcess线程是将从USB接收的8bit数据合成16bit信息并显示,//分类数据传输到各自的通道
     */
    public class dataProcess extends Thread {//接收数据的线程
        // private  final int COMPLETED = 0;
        resource r;
        data dd;

        dataProcess(resource r, data dd) {
            this.dd = dd;
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
                synchronized (r) {
                    if (!r.flag)
                        try {
                            r.wait();
                        } catch (InterruptedException ex) {
                        }

                    long startTime = System.nanoTime();             // 纳秒级
                   /* String st = "0";//数据转化为字符串的中间变量
                    String st1 = "0";//储存接收数据转化后的字符串*/
                    int p;
                    int p1;
                    int i1 = 0;
                    int[] combination = new int[8192];//combination用于储存合并后的16bit数据

                    // for (int i = 0; i < r.data.length; i++) {//报出空指针异常的原因是接收数据还没有处理完，线程就跳转到了这里
                    for (int i = 0; i < r.data.length; i = i + 2) {//数据组合

                        if (r.data[i] < 0) {
                            p = 256 + r.data[i];

                        } else {
                            p = r.data[i];
                        }

                        if (r.data[i + 1] < 0) {
                            p1 = (256 + r.data[i + 1]) << 8;

                        } else {
                            p1 = r.data[i + 1] << 8;
                        }
                        combination[i1] = p + p1;
                        i1 = i1 + 1;
                    }

                    long estimatedTime = System.nanoTime() - startTime;

                    int[] dadd = new int[4*1024];
                    int[] dadd1 = new int[4*1024];
                    int[] dadd2 = new int[4*1024];
                    int[] dadd3 = new int[4*1024];

//                    for (int i=0;i<combination.length;i++)//可以用foreach
//                    {
//
//                        if(combination[i]==0){
                    dadd = Arrays.copyOfRange(combination, 0, 2048-1);

//                        }
//                        else{
//
//                        }
//                        if (combination[i]==512){
                    dadd1 = Arrays.copyOfRange(combination, 2048, 4096-1);
//
//                        }
//                        if (combination[i]==1024){
                    dadd2 = Arrays.copyOfRange(combination, 4096, 6144-1);

//
//                        }
//                        if(combination[i]==1536){
                    dadd3 = Arrays.copyOfRange(combination, 6144, 8192-1);

                    // }

                    //}
                    data_a.putIntArray("tubea", dadd);//待传入通道A的数据
                    data_a1.putIntArray("tubea1", dadd1);//待传入通道A1的数据
                    data_b.putIntArray("tubeb", dadd2);//待传入通道B的数据
                    data_b1.putIntArray("tubeb1", dadd3);//待传入通道B1的数据
                    /***
                     * 这个线程同步的作用是发送处理好的数据交给fragment线程进行处理，在fragment显示线程进行数据显示的过程中，要求当前数据处理线程进行等待
                     */
                    synchronized (dd) {
                        try {

                            set_TubeA1_data(data_a);//将分好类的数据放入Bundle中以供fragment访问
                            set_TubeA1_data1(data_a1);
                            set_TubeA1_data2(data_b);
                            set_TubeA1_data3(data_b1);
                            dd.flag1 = true;//与fragment中的绘图线程进行生产者与消费者

                            switch (fragmentnumber) {
                                case 0:dataModel frgment1 = (dataModel) getFragmentManager().findFragmentByTag("datamodel");//获取当前的fragment
                                    frgment1.wakeup();//调用fragment中的唤醒方法
                                case 1:calibrateModel frgment2 = (calibrateModel) getFragmentManager().findFragmentByTag("calibratemodel");//获取当前的fragment
                                    frgment2.wakeup();//调用fragment中的唤醒方法
                                case 2:tempreatureModel fragment3=(tempreatureModel)getFragmentManager().findFragmentByTag("tempreturemodel");
                                    fragment3.wakeup();//调用fragment中的唤醒方法
                            }

                            //dataModel frgment1 = (dataModel) getFragmentManager().findFragmentByTag("datamodel");//获取当前的fragment
                            //frgment1.wakeup();//调用fragment中的唤醒方法
                            /**
                             * 先实现数据处理线程的等待，直到该fragment中显示线程显示数据完成后由显示线程对当前的数据处理线程进行唤醒继续运行
                             */
                            if (dd.flag1) {
                                try {
                                    dd.wait();
                                } catch (InterruptedException ex) {
                                }
                            } else {
                                dd.notifyAll();
                            }

                        } catch (NullPointerException ee) {
                        }
                    }

                    //set_TubeA1_data(data_a, data_a1, data_b, data_b1);//调用传入通道A数据函数
//                    Message msg1 = new Message();
//                    msg1.what = COMPLETED;
//                    msg1.obj = dadd[1024] + "+" + dadd1[1024] + "+" + dadd2[1024] + "+" + dadd3[1024];
//                    //msg1.obj =combination[0]+"+"+combination[20480]+"+"+combination[30720]+"+"+combination[40959];//要显示的数据，测试使用
//                    //msg1.obj =r.data[0]+"+"+r.data[40960]+"+"+r.data[61440]+"+"+r.data[81919];//要显示的数据，测试使用
//                    msg1.arg1 = r.speed;//数据的传输速度
//                    msg1.arg2 = (int) estimatedTime;//arg2表示携带的处理速度信息
//                    handler.sendMessage(msg1);
                    r.flag = false;
                    r.notify();
                    setchange(true);

                    Log.d("数据处理", "数据处理线程完成");
                }
            }


        }
    }

    /**
     * 创建四个Bundle数组用来携带每个通道的数据信息以便传递到各个Fragment
     */
    Bundle TubeA1data1;
    Bundle TubeA1data2;
    Bundle TubeA1data3;
    Bundle TubeA1data4;

    /**
     * set_TubeA1_data(Bundle a)函数是数据处理线程调用的函数，存放数据
     * @param a
     */
    public void set_TubeA1_data(Bundle a) {

        TubeA1data1 = a;
    }

    public void set_TubeA1_data1(Bundle a) {

        TubeA1data2 = a;
    }

    public void set_TubeA1_data2(Bundle a) {

        TubeA1data3 = a;
    }

    public void set_TubeA1_data3(Bundle a) {

        TubeA1data4 = a;
    }

    /**
     * 在Frafment中利用 Bundle bb= ((main1) getActivity()).get_TubeA1_data()来从该Avtivity中获取数据
     *
     * @return返回通道的数据Bundle
     */
    public Bundle get_TubeA1_data() {
        return TubeA1data1;
    }

    public Bundle get_TubeA1_data1() {
        return TubeA1data2;
    }

    public Bundle get_TubeA1_data2() {
        return TubeA1data3;
    }

    public Bundle get_TubeA1_data3() {
        return TubeA1data4;
    }

    /**
     * 数据的存储，储存的路径为/mnt/external_sd，文件名为data.txt
     */
    private void write(byte[] content) {//注意储存字符串传入的参数为String content，储存二进制传入的参数为byte[] content

        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileOutputStream raf = new FileOutputStream("/mnt/external_sd/data.txt", true);//储存二进制文件
                Log.d("chu", "sunce");
                // FileWriter raf=new FileWriter("/mnt/external_sd/data.txt",true);//储存为字符串
                raf.write(content);
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    /**
     * 这个resource类的作用是提供一个共享的数据储存区域，封装数据用于储存
     */
    class resource {
        byte[] data;
        int speed;
        boolean flag = false;
       /* public synchronized void setresource(int [] d,int s){
            data=d;
            speed=s;}*/
    }

    class data {
        Bundle[] DD;
        boolean flag1 = false;
    }

    /**
     * 该语句为实例化一个数据接收的类，re为数据接收线程
     */
    resource r = new resource();
    data dta = new data();
    public dataReceiveThread re = new dataReceiveThread(r);
    public dataProcess pro = new dataProcess(r, dta);

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
 /*           int num=myInterface.getEndpointCount();//可以检测当前接口上的端点数目
            deviceState.setText("当前接口的端点的数目"+num);//用文本显示端点的个数
            for (int i = 0; i < myInterface.getEndpointCount(); i++) {//该for循环用来找到指定的端点类型
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
                usbstate.setText("打开设备成功");
            }
            if (conn == null) {
                usbstate.setText("打开设备失败");
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
            if (device.getVendorId() == 1204 && device.getProductId() == 241) {//根据VID,PID枚举设备
                myUsbDevice = device;
            } else {
                return;
            }
        }

    }
}
