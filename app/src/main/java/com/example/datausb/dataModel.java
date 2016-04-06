package com.example.datausb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
//import android.sutube1ort.v4.atube1.Fragment;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
//import android.atube1.Fragment;
//import android.sutube1ort.v4.atube1.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

/**
 * Created by sunset on 15/11/19.
 */
public class dataModel extends android.app.Fragment {
    /**
     * 定义一个SurfaceHolder用来管理surface
     */
    private SurfaceHolder holder;

    /**
     * 这个函数的作用是使Activity可以唤醒fragment中的显示线程
     */
    public void wakeup() {
        ((main1) getActivity()).dta.notifyAll();
    }

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
        private dataThread myThread;
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
            myThread = new dataThread(holder1, ss, datareceive);//创建一个绘图线程
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
    class dataThread extends Thread {
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
        public dataThread(SurfaceHolder holder, SurfaceView ss1, boolean dd) {
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

                while (!((main1) getActivity()).stopdatamodelthread) {



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
                            p1.moveTo(20, h-400);
                            p2.moveTo(20, h-280);
                            p3.moveTo(20, h-160);
                            p4.moveTo(20, h-40);
//注意数据可能数值太大而不能再屏幕上显示，所以我能要把数据转换成光功率的数值进行显示，也方便后续的计算
//由于 屏幕上的点数有限，所以我们只有在屏幕上显示部分点，我们可以用总的点数除以屏幕上的点数，得出的数就是我们要每几个点显示为一个点，显示的这个点为这几个点中的最大值
//还有一个实际中存在的问题，每个通道第几个数据并不对应相应的第几个距离。可能还需要排序
                            int [] adp1=screenadapter(tuba,w);
                            int [] adp2=screenadapter(tuba1,w);
                            int [] adp3=screenadapter(tubeb,w);
                            int [] adp4=screenadapter(tubeb1,w);
                            /**
                             * 在FPGA上传递增的数据的时候，adp1[i]/500,其中500这个数取值影响了图像在y轴上的分布，这个值取小了，图像的纵坐标就显示不全，取太大在屏幕上看的图线的变化就不是很明显
                             */
                            for (int i = 0; i < adp1.length; i++) {//lineTo显示一条直线是因为纵坐标的数值太大；
                                p1.lineTo(i+25, -adp1[i]/400+h-400);//在实际的的系统2的14次方，也就是16384，还要在乘以参考电压
                                p2.lineTo(i+25, -adp2[i]/400+  h-280);
                                p3.lineTo(i+25, -adp3[i]/400+h-160);
                                p4.lineTo(i+25, -adp4[i]/400+h-40);
                            }
                            c.drawPath(p1, tube1);
                            c.drawPath(p2, tube2);
                            c.drawPath(p3, tube3);
                            c.drawPath(p4, tube4);
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
                Log.d("datamodel", "数据模式出现空指针异常");


            }

        }
    }

//进行屏幕大小适配的方法
    public int [] screenadapter(int [] data,int w){
        int [] adptertube=new int[w-10];//设置屏可以显示在屏幕上的数据长度
        int []databuf;
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
    public int  max(int [] a){
        int b;
       Arrays.sort(a);
       b= a[a.length-1];
        return b;
    }
}


