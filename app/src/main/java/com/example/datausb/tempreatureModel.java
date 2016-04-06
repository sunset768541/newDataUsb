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
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by sunset on 15/11/19.
 * /**
 * tempreturemodel要实现的功能就是先将标定的数据取出来，同时计算tuba和tua1的比值既P（SA），然后利用公式1/T=(K/HV)ln(P(SA(T)/P(SA)(0))+1/T0计算出y值，
 * 然后在surfaceview上显示出比值的图形
 */

public class tempreatureModel extends android.app.Fragment {
    /**
     * 定义一个SurfaceHolder用来管理surface
     */
    private SurfaceHolder holder;
   float [] caliPSA;
    float[] caliPSB;
    /**
     * 这个函数的作用是使Activity可以唤醒fragment中的显示线程
     */
    public void wakeup() {
        ((main1) getActivity()).dta.notifyAll();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tempreaturemodel, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//
        try{
            caliPSA=((main1) getActivity()).getfromdatabase(((main1) getActivity()).mDatabase, "tube1data");
            caliPSB=((main1) getActivity()).getfromdatabase(((main1) getActivity()).mDatabase, "tube2data");
        }
        catch (Exception e){
            Toast.makeText(((main1) getActivity()).getApplicationContext(), "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();

        }

        /**
         * 获得布局中的surfaceview
         */
        SurfaceView sur = (SurfaceView) getActivity().findViewById(R.id.surb1);
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
        private tempreatureThread myThread;
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
            myThread = new tempreatureThread(holder1, ss, datareceive);//创建一个绘图线程
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
    class tempreatureThread extends Thread {
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
        public tempreatureThread(SurfaceHolder holder, SurfaceView ss1, boolean dd) {
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
            while (!((main1) getActivity()).stoptempreturemodelthread) {




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
                        float[] T1=new float[tuba.length];
                        float[] T2=new float[tuba.length];
                        float [] PSA1=new float[tuba.length];
                        float [] PSA2=new float[tuba.length];
                        for (int i=0;i<tuba.length;i++){
                            if(tuba[i]==0){
                                PSA1[i]=0;
                            }
                            else PSA1[i]=(float)tuba1[i]/tuba[i];
                            if(tubeb[i]==0){
                                PSA2[i]=0;
                            }
                            else PSA2[i]=(float)tubeb1[i]/tubeb[i];
                        }
                        /**
                         * 由公式计算出温度
                         */
                        for (int i=0;i<tuba.length;i++){
                            double bb1=(double)PSA1[i]/caliPSA[i];
                            double bb2=(double)PSA2[i]/caliPSB[i];
                            //Log.e("PSA","PSA"+Double.valueOf(PSA1[i]).toString()+"PSB"+Double.valueOf(PSA2[i]).toString());
                            //Log.e("calopsa","当前温度"+Float.valueOf(caliPSA[caliPSA.length-1]).toString()+"Acli"+Float.valueOf(caliPSA[i]).toString()+"Bcli"+Float.valueOf(caliPSB[i]).toString());
                            float tt1=(float)(Math.log(bb1)+1/caliPSA[caliPSA.length-1]);
                            float tt2=(float)(Math.log(bb2)+1/caliPSB[caliPSB.length-1]);
                            T1[i]=1/tt1;
                            T2[i]=1/tt2;
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


                            p1.moveTo(20, h /2);
                            p2.moveTo(20, h /3);

                            float [] adp1=screenadapter(T1,w);
                            float [] adp2=screenadapter(T2,w);
                            for (int i = 1; i < adp1.length; i++) {
                                p1.lineTo(i+25, -adp1[i]+h /2);
                                p2.lineTo(i+25, -adp2[i]+h /3);
                               // Log.e("通道1的温度",Float.valueOf(adp1[i]).toString());
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

                            ((main1) getActivity()).dta.flag1 = false;
                            ((main1) getActivity()).wakeuppro();
                           // Log.d("绘图线程run", "绘制数据图像的方法完成方法");
                        }


                    }


                }
                }
            catch (NullPointerException e) {
                Log.d("tempretureModel", "温度模式出现空指针异常");


            }

        }


    }
    //进行屏幕大小适配的方法
    public float [] screenadapter(float [] data,int w){
        float [] adptertube=new float[w-10];//设置屏可以显示在屏幕上的数据长度
        float []databuf;
        int interval=data.length/w+1;
        // Log.d("输出间隔",Integer.toString(interval)+"    "+Integer.valueOf(data.length).toString()+"   "+Integer.valueOf(w).toString());
        int kkk=0;
        if(interval<=1){
            adptertube=data;
        }
        else {

            for(int i=0;i<data.length;i=i+interval){
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


