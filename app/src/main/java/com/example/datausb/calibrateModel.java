package com.example.datausb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.datausb.R;
import com.example.datausb.main1;

import java.util.Arrays;

/**
 * calibratemodel要实现的功能就是将tuba，tuba1的比值既P（SA）和标定温度T0储存起来，然后在surfaceview上显示出比值的图形
 */

public class calibrateModel extends android.app.Fragment {
    /**
     * 定义一个SurfaceHolder用来管理surface
     */
    private SurfaceHolder holder;
    EditText editT0;
    EditText editT1;
    Button   calibriteT0;
    Button   calibriteT1;
    //SQLiteDatabase db;
    /**
     * 这个函数的作用是使Activity可以唤醒fragment中的显示线程
     */
    public void wakeup() {
        ((main1) getActivity()).dta.notifyAll();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calibratemodel, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);//
        editT0 = (EditText) getActivity().findViewById(R.id.editText);
        editT1 = (EditText) getActivity().findViewById(R.id.editText2);
        calibriteT0 = (Button) getActivity().findViewById(R.id.button8);
        calibriteT1 = (Button) getActivity().findViewById(R.id.button9);
        calibriteT0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] tuba;
                int[] tuba1;
                String tem=editT0.getText().toString().trim();
                      if(TextUtils.isEmpty(tem)){
                          Toast.makeText( ((main1) getActivity()).getApplicationContext(), "请输入当前的标定温度", Toast.LENGTH_SHORT).show();
                      }
                      else {
                          Log.e("tt",editT0.getText().toString());

                        final  String tablename="tube1data";//要建立表格的命字
                          //建立表格的sql命令，存在就不建立，不存在就建立
                          String stu_table = "create table if not exists tube1data(_id integer primary key autoincrement,calibtem INTEGER,tubedata text)";
                          ((main1) getActivity()).creatOrgettable(((main1) getActivity()).mDatabase, stu_table);
                          try {
                               tuba = ((main1) getActivity()).get_TubeA1_data().getIntArray("tubea");
                              tuba1 = ((main1) getActivity()).get_TubeA1_data1().getIntArray("tubea1");
                             final float [] PSA=new float[tuba.length];
                              for (int i=0;i<tuba.length;i++){
                                  if(tuba[i]==0){
                                      PSA[i]=0;
                                  }

                                  else {

                                      PSA[i]=(float)tuba1[i]/tuba[i];
                                      // Log.e("PAS",Float.valueOf(PSA[i]).toString());
                                  }

                              }
                              new Thread(){
                                public void run(){
                                      ((main1) getActivity()).updatadatabase(((main1) getActivity()).mDatabase, Integer.valueOf(editT0.getText().toString()), PSA, tablename);
                                    Looper.prepare();

                                     Toast.makeText(((main1) getActivity()).getApplicationContext(), "传感通道1标定完成", Toast.LENGTH_SHORT).show();
                                    Looper.loop();

                                  }
                              }.start();


                          }
                          catch (NullPointerException e){
                              Toast.makeText( ((main1) getActivity()).getApplicationContext(), "通道1无数据输入", Toast.LENGTH_SHORT).show();

                          }


                          //标定直接计算出P（SA）储存。
                          // db= SQLiteDatabase.openOrCreateDatabase("/mny/db/datausb.db3",null);
                          //db.execSQL("insert into news_inf values(null,?,?)",new String[]{});
                          //开始标定

                      }
                      //标定按钮的监听函数
                  }



        });
        calibriteT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] tubeb;
                int[] tubeb1;
                String tem=editT1.getText().toString().trim();
                        if(TextUtils.isEmpty(tem)){
                            Toast.makeText( ((main1) getActivity()).getApplicationContext(), "请输入当前的标定温度", Toast.LENGTH_SHORT).show();
                        }
                        else {
                          final  String tablename = "tube2data";
                            String stu_table = "create table if not exists tube2data(_id integer primary key autoincrement,calibtem INTEGER,tubedata text)";
                            ((main1) getActivity()).creatOrgettable(((main1) getActivity()).mDatabase, stu_table);
                            try{
                                tubeb = ((main1) getActivity()).get_TubeA1_data2().getIntArray("tubeb");
                                tubeb1 = ((main1) getActivity()).get_TubeA1_data3().getIntArray("tubeb1");
                             final     float [] PSA1=new float[tubeb.length];
                                for (int i=0;i<tubeb.length;i++){
                                    if(tubeb[i]==0){
                                        PSA1[i]=0;
                                    }
                                    else PSA1[i]=(float)tubeb1[i]/tubeb[i];
                                    // Log.e("PAS",Float.valueOf(PSA[i]).toString());
                                }

                                //标定直接计算出P（SA）储存。
                                new Thread(){
                                    public void run(){
                                        ((main1) getActivity()).updatadatabase(((main1) getActivity()).mDatabase, Integer.valueOf(editT0.getText().toString()), PSA1, tablename);
                                        Looper.prepare();

                                        Toast.makeText(((main1) getActivity()).getApplicationContext(), "传感通道2标定完成", Toast.LENGTH_SHORT).show();
                                        Looper.loop();

                                   }
                                }.start();

                            }

                            catch (NullPointerException e){
                                Toast.makeText(((main1) getActivity()).getApplicationContext(), "通道2无数据输入", Toast.LENGTH_SHORT).show();

                            }


       // db= SQLiteDatabase.openOrCreateDatabase("/mny/db/datausb.db3",null);
                            //db.execSQL("insert into news_inf values(null,?,?)",new String[]{});
                            //开始标定

                        }
                        //标定按钮的监听函数



            }
        });
                /**
                 * 获得布局中的surfaceview
                 */
                SurfaceView sur = (SurfaceView) getActivity().findViewById(R.id.surb);
                /**
                 * 将holder和surfaceview绑定
                 */
                holder = sur.getHolder();
                /**
                 * 实例化一个surfaceview
                 */
                VV v1 = new VV(getActivity(), holder, sur);
                /**
                 * 调用这个surfaceview的surfaceCreated方法
                 */
                v1.surfaceCreated(holder);

            }

            /**
             * 一个集成Surfaceview并实现了SurfaceHolder.Callback方法的的类
             */

            class VV extends SurfaceView implements SurfaceHolder.Callback {
                private calibrateThread myThread;
                SurfaceView ss;

                /**
                 * 该类的构造函数
                 *
                 * @param context
                 * @param holder1，传入holder，给绘图线程使用
                 * @param sur，传入sur使绘图线程获得当前surfaceview的大小
                 */
                public VV(Context context, SurfaceHolder holder1, SurfaceView sur) {
                    super(context);
                    ss = sur;
                }

                public void surfaceChanged(SurfaceHolder holder1, int a, int b, int c) {
                    holder1.addCallback(this);
                    boolean datareceive = ((main1) getActivity()).getchange();
                    myThread = new calibrateThread(holder1, ss, datareceive);//创建一个绘图线程
                    myThread.start();
                }

                public void surfaceCreated(SurfaceHolder holder) {
                    holder.addCallback(this);
                }

                public void surfaceDestroyed(SurfaceHolder holder) {

                }


            }

            /**
             * 一个绘图线程类
             */
            class calibrateThread extends Thread {
                private SurfaceHolder holder;
                public boolean isRun;

                int h;
                int w;
                SurfaceView sss;
                //Bundle[] alltubedata;
                boolean dd;

                /**
                 * 该线程的构造函数
                 *
                 * @param holder，传入的holder用来指定绘图的surfaceview
                 * @param ss1，用来获得surfaceview的大小
                 */
                public calibrateThread(SurfaceHolder holder, SurfaceView ss1, boolean dd) {
                    this.holder = holder;
                    sss = ss1;
                    isRun = true;
                    //alltubedata=cc;
                    h = sss.getHeight();
                    w = sss.getWidth();
                    this.dd = dd;
                }

                public void run() {
                    try {//捕获线程运行中切换界面而产生的的空指针异常，防止程序崩溃。
                        while (!((main1) getActivity()).stopcalibratemodelthread) {


                            synchronized (((main1) getActivity()).dta) {//所有的等待和唤醒的锁都是同一个，这里选用了Activity中的一个对对象
                                /**
                                 * 如果当标志位为false这个线程开始等待
                                 */
                                if (!((main1) getActivity()).dta.flag1)
                                    try {
                                        ((main1) getActivity()).dta.wait();

                                    } catch (InterruptedException ex) {

                                    }
                                else {
                                    ((main1) getActivity()).dta.notifyAll();
                                }
                                //下面的语句是从Activity中获取数据
                                int[] tuba = ((main1) getActivity()).get_TubeA1_data().getIntArray("tubea");
                                int[] tuba1 = ((main1) getActivity()).get_TubeA1_data1().getIntArray("tubea1");
                                int[] tubeb = ((main1) getActivity()).get_TubeA1_data2().getIntArray("tubeb");
                                int[] tubeb1 = ((main1) getActivity()).get_TubeA1_data3().getIntArray("tubeb1");
                                /**
                                 * 下面是根据通道1234的数据计算标定光功率的PSA
                                 */
                                float [] PSA1=new float[tuba.length];
                                float [] PSA2=new float[tuba.length];
                                for (int i=0;i<tuba.length;i++){
                                    if(tuba[i]==0){
                                        PSA1[i]=0;
                                    }
                                    else
                                    {
                                        PSA1[i]=(float)tuba1[i]/tuba[i];
                                  //      Log.e("data",Float.valueOf(PSA1[i]).toString()+" tuba1 "+Integer.valueOf(tuba1[i]).toString()+" tuba "+Integer.valueOf(tuba[i]).toString());
                                    }
                                    if(tubeb[i]==0){
                                        PSA2[i]=0;
                                    }
                                    else PSA2[i]=(float)tubeb1[i]/tubeb[i];
                                }

                                /**
                                 * 定义了两支画笔
                                 * paxis用来画横纵坐标轴
                                 * axe用来绘制坐标轴中的坐标
                                 */
                                Paint paxis = new Paint();
                                Paint axe = new Paint();
                                axe.setARGB(255, 83, 83, 83);
                                axe.setStyle(Paint.Style.STROKE);
                                axe.setStrokeWidth(1);
                                paxis.setARGB(255, 83, 83, 83);
                                paxis.setStyle(Paint.Style.STROKE);
                                paxis.setStrokeWidth(3);
                                /**
                                 * c为锁定suface时获得的一个画布，我们可以在上面画图
                                 */
                                Canvas c = holder.lockCanvas();
                                /**
                                 * 绘制整个画布的颜色
                                 */
                                c.drawRGB(38, 38, 38);
                                /**
                                 * 绘制横纵坐标轴
                                 */
                                c.drawLine(20, 10, 20, h - 20, paxis);
                                c.drawLine(20, h - 20, w - 10, h - 20, paxis);//绘制坐标轴
                                /**
                                 * 绘制横纵轴各画10条线
                                 */
                                int ci = 10;
                                for (int i = 1; i < ci; i++) {
                                    int k = i * h / ci;
                                    int m = i * w / ci;
                                    c.drawLine(20, k, w - 20, k, axe);
                                    c.drawLine(m, h / ci, m, h - 20, axe);
                                }

                                /**
                                 * 绘制各个通道的图像
                                 * 共新建4个画笔和4个路径
                                 */
                                synchronized (holder) {
                                    Path p1 = new Path();
                                    Path p2 = new Path();


                                    Paint tube1 = new Paint();
                                    Paint tube2 = new Paint();

                                    tube1.setColor(Color.RED);
                                    tube1.setStyle(Paint.Style.STROKE);
                                    tube1.setAntiAlias(true);
                                    tube1.setStrokeWidth(1);
                                    PathEffect pe1 = new CornerPathEffect(10);
                                    tube1.setPathEffect(pe1);

                                    tube2.setColor(Color.GREEN);
                                    tube2.setStyle(Paint.Style.STROKE);
                                    tube2.setAntiAlias(true);
                                    tube2.setStrokeWidth(1);
                                    tube2.setPathEffect(pe1);


                                    p1.moveTo(20, 3*h/4);
                                    p2.moveTo(20, h/2);

                                    float [] adp1=screenadapter(PSA1,w);
                                    float [] adp2=screenadapter(PSA2,w);
                                    for (int i = 1; i < adp1.length; i++) {
                                        p1.lineTo(i+25, -adp1[i]+3*h/4);
                                        p2.lineTo(i+25, -adp2[i] +h/2);
                                    }
                                    c.drawPath(p1, tube1);
                                    c.drawPath(p2, tube2);


                                    /**
                                     * 结束锁定画布并显示
                                     */
                                    holder.unlockCanvasAndPost(c);//结束锁定画图，并提交改变。// ;
                                    /**
                                     * 把标识位置为false
                                     * 同时唤醒数据处理线程
                                     */

                                    ((main1) getActivity()).dta.flag1 = false;//不要在该语句前加Log输出
                                    ((main1) getActivity()).wakeuppro();
                                    //Log.d("绘图线程run", "绘制数据图像的方法完成方法");
                                }


                            }


                        }

                    } catch (NullPointerException e) {
                        Log.d("calibrateModel", "标定模式出现空指针异常");

                    }

                }
            }
    //进行屏幕大小适配的方法
    public float [] screenadapter(float [] data,int w){
        float [] adptertube=new float[w-10];//设置屏可以显示在屏幕上的数据长度，adptertube的长度要与data.length匹配，其长度要大于data.length/interval
        float []databuf;
        int interval=data.length/adptertube.length+1;
        // Log.e("输出间隔",Integer.toString(interval)+"    "+Integer.valueOf(data.length).toString()+"   "+Integer.valueOf(w).toString());
        //Log.e("aps",Integer.valueOf(adptertube.length).toString());
        int kkk=0;
        if(interval<=1){
            adptertube=data;
        }
        else {

            for(int i=0;i<(data.length/interval)*interval;i=i+interval){
                databuf= Arrays.copyOfRange(data, i, i + interval);
                adptertube[kkk]=max(databuf);//这里出现了空指针异常
                kkk=kkk+1;
            }
        }

        return adptertube;
    }
    //对一个数组输出最大值方法
    public float  max(float [] a){
        float b;
        Arrays.sort(a);
        b= a[a.length-1];
        return b;
    }
        }


