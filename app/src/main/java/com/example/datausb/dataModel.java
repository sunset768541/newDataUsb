package com.example.datausb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
//import android.sutube1ort.v4.atube1.Fragment;
import android.os.Bundle;
//import android.atube1.Fragment;
//import android.sutube1ort.v4.atube1.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sunset on 15/11/19.
 */
public class dataModel extends android.app.Fragment {
    /**
     * 定义一个SurfaceHolder用来管理surface
     */
    private SurfaceHolder holder;
    Bundle[] alltubedata;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datamodel, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//
        /**
         * 获得布局中的surfaceview
         */
        SurfaceView sur = (SurfaceView) getActivity().findViewById(R.id.sura);
        /**
         * 将holder和surfaceview绑定
         */
        holder = sur.getHolder();
        /**
         * 实例化一个surfaceview
         */
        VV v1 = new VV(getActivity(), holder,sur);
        /**
         * 调用这个surfaceview的surfaceCreated方法
         */
        v1.surfaceCreated(holder);

    }

    /**
     * 一个集成Surfaceview并实现了SurfaceHolder.Callback方法的的类
     */

    class VV extends SurfaceView implements SurfaceHolder.Callback {
        private MyThreadA myThread;
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

        public void surfaceChanged(SurfaceHolder holder1,int a,int b,int c) {
            holder1.addCallback(this);
            // Bundle[] alltubedata= ((main1) getActivity()).get_TubeA1_data();//从Avtivity中获取数据
            boolean datareceive= ((main1) getActivity()).getchange();
            myThread = new MyThreadA(holder1, ss,datareceive);//创建一个绘图线程
            ;//从Avtivity中获取数据

            myThread.start();
        }

        public void surfaceCreated(SurfaceHolder holder) {
            holder.addCallback(this);
            // alltubedata= ((main1) getActivity()).get_TubeA1_data();//从Avtivity中获取数据
         //  Log.d("ThreadA", Integer.toString(alltubedata[0].getIntArray("tubea")[3]));
            boolean datareceive= ((main1) getActivity()).getchange();

           // myThread = new MyThreadA(holder, ss,alltubedata,datareceive);//创建一个绘图线程
            //;//从Avtivity中获取数据
            //myThread.start();
            //drawC(holder);
        }

        public void surfaceDestroyed(SurfaceHolder holder) {

        }



    }
    /**
     * 一个绘图线程类
     */
    class MyThreadA extends Thread {
        private SurfaceHolder holder;
        public boolean isRun;

        int h;
        int w;
        SurfaceView sss;
        //Bundle[] alltubedata;
        boolean dd;
        /**
         * 该线程的构造函数
         * @param holder，传入的holder用来指定绘图的surfaceview
         * @param ss1，用来获得surfaceview的大小
         */
        public MyThreadA(SurfaceHolder holder,SurfaceView ss1,boolean dd) {
            this.holder = holder;
            sss=ss1;
            isRun = true;
            //alltubedata=cc;
            h=sss.getHeight();
            w=sss.getWidth();
            this.dd=dd;
        }

        public void run () {
            while (true){
Log.d("绘图线程run","执行到run方法");

           try {//捕获线程运行中切换界面而产生的的空指针异常，防止程序崩溃。

                   while (((main1) getActivity()).dta.flag1)


               //((main1) getActivity()).dta.DD[0];

                   {
                       Log.d("绘图线程run","执行到while方法");
                       synchronized (((main1) getActivity()).dta){

//                           if (!((main1) getActivity()).dta.flag1)
//                               try {
//                                   Log.d("绘图线程run", "执行到try等待前");
//                                   ((main1) getActivity()).dta.wait();
//                                   Log.d("绘图线程run", "执行到try等待后的方法");
//
//                               } catch (InterruptedException ex) {
//                                   Log.d("绘图线程run","执行到if判断dta的flag方法");
//                               }
//                           int[] tuba = alltubedata[0].getIntArray("tubea");
//                           int[] tuba1 = alltubedata[1].getIntArray("tubea1");
//                           int[] tubeb = alltubedata[2].getIntArray("tubeb");
//                           int[] tubeb1 = alltubedata[3].getIntArray("tubeb1");
                           Log.d("画笔", "画布提交");
                           int[] tuba = ((main1) getActivity()).get_TubeA1_data().getIntArray("tubea");
                           int[] tuba1 = ((main1) getActivity()).get_TubeA1_data1().getIntArray("tubea1");
                           int[] tubeb = ((main1) getActivity()).get_TubeA1_data2().getIntArray("tubeb");
                           int[] tubeb1 =((main1) getActivity()).get_TubeA1_data3().getIntArray("tubeb1");
                           Log.d("画笔", "画布77777777提交");

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
                               Path p3 = new Path();
                               Path p4 = new Path();

                               Paint tube1 = new Paint();
                               Paint tube2 = new Paint();
                               Paint tube3 = new Paint();
                               Paint tube4 = new Paint();

                               tube1.setColor(Color.GREEN);
                               tube1.setStyle(Paint.Style.STROKE);
                               tube1.setAntiAlias(true);
                               tube1.setStrokeWidth(1);
                               PathEffect pe1 = new CornerPathEffect(10);
                               tube1.setPathEffect(pe1);

                               tube2.setColor(Color.RED);
                               tube2.setStyle(Paint.Style.STROKE);
                               tube2.setAntiAlias(true);
                               tube2.setStrokeWidth(1);
                               tube2.setPathEffect(pe1);

                               tube3.setColor(Color.BLUE);
                               tube3.setStyle(Paint.Style.STROKE);
                               tube3.setAntiAlias(true);
                               tube3.setStrokeWidth(1);
                               tube3.setPathEffect(pe1);

                               tube4.setARGB(255, 255, 255, 17);
                               tube4.setStyle(Paint.Style.STROKE);
                               tube4.setAntiAlias(true);
                               tube4.setStrokeWidth(1);
                               tube4.setPathEffect(pe1);
                               p1.moveTo(20, h / 8 + 30);
                               p2.moveTo(20, h / 6 + 40);
                               p3.moveTo(20, h / 4 + 50);
                               p4.moveTo(20, h / 2 + 60);

                               for (int i = 1; i < tuba.length; i++) {
                                   p1.lineTo(tuba[i - 1], tuba[i] + h / 5);
                               }
                               c.drawPath(p1, tube1);

                               for (int i = 1; i < tuba1.length; i++) {
                                   p2.lineTo(tuba1[i - 1], tuba1[i] + h / 5 + h / 4 + 20);
                               }
                               c.drawPath(p2, tube2);

                               for (int i = 1; i < tubeb.length; i++) {
                                   p3.lineTo(tubeb[i - 1], tubeb[i] + h / 3);
                               }
                               c.drawPath(p3, tube3);

                               for (int i = 1; i < tubeb1.length; i++) {
                                   p4.lineTo(tubeb1[i - 1], tubeb1[i]);
                               }
                               c.drawPath(p4, tube4);
                               /**
                                * 结束锁定画布并显示
                                */
                               holder.unlockCanvasAndPost(c);//结束锁定画图，并提交改变。// ;
                               ((main1) getActivity()).dta.flag1=false;
                              // ((main1) getActivity()).r.notify();
                               Log.d("绘图线程run", "绘制数据图像的方法完成方法");
                           }


                       }
                   }


           }
            catch(NullPointerException e){
                Log.d("hh", "huazuohiao");


            }

        }

    }
    }

    }


