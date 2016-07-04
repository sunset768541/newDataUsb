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

public class TempreatureModel extends android.app.Fragment {
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
        ((Main) getActivity()).dataObj.notifyAll();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tempreaturemodel, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//
        try{
            caliPSA=DataBaseOperation.mDataBaseOperation.getfromdatabase("tube1data");
            caliPSB=DataBaseOperation.mDataBaseOperation.getfromdatabase("tube2data");
        }
        catch (Exception e){
            Toast.makeText(((Main) getActivity()).getApplicationContext(), "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();

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
            boolean datareceive = ((Main) getActivity()).GetByteDataProcessComlete();
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
        float fiberLength = 2048;
        float maxnum = 16384;
       // FileWriter ps;
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
          //  StrStore.cresave();
//            try {
//            //   ps= new FileWriter("/mnt/external_sd/temdata.txt");
//
//            }
//            catch (Exception e){}
        }

        public void run() {
            try {//捕获线程运行中切换界面而产生的的空指针异常，防止程序崩溃。
            while (!((Main) getActivity()).stopTemperatureModelThread) {




                    synchronized (((Main) getActivity()).dataObj) {//所有的等待和唤醒的锁都是同一个，这里选用了Activity中的一个对对象
                        /**
                         * 如果当标志位为false这个线程开始等待
                         */
                        if (!((Main) getActivity()).dataObj.flag1)
                            try {
                                ((Main) getActivity()).dataObj.wait();

                            } catch (InterruptedException ex) {

                            }
                        else {
                            ((Main) getActivity()).dataObj.notifyAll();
                        }
                        //下面的语句是从Activity中获取数据
                        int[] tuba = ((Main) getActivity()).get_TubeA1_data().getIntArray("tubea");
                        int[] tuba1 = ((Main) getActivity()).get_TubeA1_data1().getIntArray("tubea1");
                        int[] tubeb = ((Main) getActivity()).get_TubeA1_data2().getIntArray("tunnelBdata");
                        int[] tubeb1 = ((Main) getActivity()).get_TubeA1_data3().getIntArray("tunnelB1data");
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
                           // StrStore.s1="通道1第 "+Integer.valueOf(i).toString() + " 个温度为" + Float.valueOf(tt1).toString()+"\n"+"通道2第 "+Integer.valueOf(i).toString() + " 个温度为" + Float.valueOf(tt2).toString()+"\n";

//                            try {
//                                ps.write(s1+"\n");
//                                ps.write(s2+"\n");
//                            }
//                            catch (Exception e){
//
//                            }

                           // StrStore.saves();
                            T1[i]=1/tt1;
                            T2[i]=1/tt2;
                        }
                        //Log.e("Tmode1最后一个数据",Float.valueOf(T1[T1.length-1]).toString());
                        //Log.e("ClabMode最后一个数据",Float.valueOf(caliPSA[caliPSA.length-2]).toString());
                        /**
                         * 定义了两支画笔
                         * paxis用来画横纵坐标轴
                         * axe用来绘制坐标轴中的坐标
                         */
                        Paint paxis = new Paint();
                        Paint axe = new Paint();
                        Paint zuobioa = new Paint();

                        axe.setARGB(255, 83, 83, 83);
                        axe.setStyle(Paint.Style.STROKE);
                        axe.setStrokeWidth(1);
                        paxis.setARGB(255, 83, 83, 83);
                        paxis.setStyle(Paint.Style.STROKE);
                        paxis.setStrokeWidth(3);
                        zuobioa.setColor(Color.WHITE);
                        zuobioa.setStyle(Paint.Style.FILL);
                        zuobioa.setTextAlign(Paint.Align.CENTER);
                        zuobioa.setTextSize(10);
                        /**
                         * c为锁定suface时获得的一个画布，我们可以在上面画图
                         */
                        Canvas c = holder.lockCanvas();
                        /**
                         * 绘制整个画布的颜色
                         */
                        c.drawRGB(15, 15, 15);
                        /**
                         * 绘制横纵坐标轴
                         */
                        c.drawLine(40, 20, 40, h - 40, paxis);
                        c.drawText("n", 40, 10, zuobioa);
                        c.drawText("m", w - 10, h - 20, zuobioa);
                        c.drawLine(40, h - 40, w - 10, h - 40, paxis);//绘制坐标轴
                        /**
                         * 绘制横纵轴各画ci条线
                         */
                        int ci = 21;
                        for (int i = 0; i < ci; i++) {
                            float y = i * maxnum / (ci - 1);
                            float x = i * fiberLength / (ci - 1);
                            /**
                             * (0,0)-------------------------------------->
                             *     |
                             *     | (40,k)----------------------(showLineViewWidth-40,k)
                             *     |
                             *     | (40,k)----------------------(showLineViewWidth-40,k)
                             *     |
                             *     | (40,k)----------------------(showLineViewWidth-40,k)
                             *     |
                             *     | (40,k)----------------------(showLineViewWidth-40,k)
                             *     |
                             *     | (40,k)----------------------(showLineViewWidth-40,k)
                             *     |
                             *     | (40,k)----------------------(showLineViewWidth-40,k)
                             *     |
                             *     | (40,k)----------------------(showLineViewWidth-40,k)
                             */
                            float k = h - (i * (h - 40) / ci + 40);//画横轴直线需要的y坐标

                            /**
                             * (m,showLineViewHeigth/ci)
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             * (m,showLineViewHeigth-40)
                             */
                            float m = i * (w - 40) / ci + 40;//纵轴间距
                            c.drawLine(40, k, w - 40, k, axe);//画横轴(40,k,showLineViewWidth-40,k)-->(x1,y1,x2,y2)画的横轴的长度是w-80,为每个循环得到画横轴的纵坐标的值
                            c.drawText(Integer.valueOf((int) y).toString(), (float) 18, (float) k, zuobioa);//在纵坐标上画字符
                            c.drawText(Integer.valueOf((int) x).toString(), (float) m, (float) (h - 10), zuobioa);//在横坐标上画字符
                            c.drawLine(m, h / (ci), m, h - 40, axe);//画纵轴m为每次画纵轴的x坐标，2h-40-showLineViewHeigth/ci为该纵轴的长度
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


                            p1.moveTo(40, h /2);
                            p2.moveTo(40, h /3);
                            float[]hh1=interpolation(w-80,T1);
                            float[]hh2=interpolation(w-80,T2);
                            float [] adp1=screenadapter(hh1,w-80);
                            float [] adp2=screenadapter(hh2,w-80);
                            for (int i = 1; i < adp1.length; i++) {
                                p1.lineTo(i+45, -adp1[i]+h /2);
                                p2.lineTo(i+45, -adp2[i]+h /3);
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

                            ((Main) getActivity()).dataObj.flag1 = false;
                            ((Main) getActivity()).wakeUpAllMainThread();
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
    public float[] screenadapter(float[] data, int w) {
        // Log.e("jjj",Integer.valueOf(dataObj.length).toString());
        float[] adptertube = new float[w];//设置屏可以显示在屏幕上的数据长度
        float[] databuf;
        int interval = data.length / adptertube.length;
        //  Log.e("输出间隔",Integer.toString(interval)+"    "+Integer.valueOf(dataObj.length).toString()+"   "+Integer.valueOf(showLineViewWidth).toString());
        int kkk = 0;
        if (interval <= 1) {
            adptertube = data;
        } else {

            for (int i = 0; i < (data.length / interval) * interval; i = i + interval) {//有必要将i先变成整数
                databuf = Arrays.copyOfRange(data, i, i + interval);
                adptertube[kkk] = max(databuf);//这里出现了空指针异常
                kkk = kkk + 1;
            }
        }

        return adptertube;
    }

    //对一个数组输出最大值方法
    public float max(float[] a) {
        float b;
        Arrays.sort(a);
        b = a[a.length - 1];
        return b;
    }

    /**
     * 屏幕点数与数据匹配函数
     * 基本思想，利用总的数据长度除以显示控件横向像素点数得出一个整数将这个整数+1就得到了interval，interval的作用就是扩展源数据长度
     * 使其可以整除显示控件的横向像素点数。interval*viewwidth viewwidth就是显示控件（坐标系）的横向像素点数，乘积的结果就是要扩展的
     * 数组的长度。为了保证扩展后的数据图像的趋势与源数组的图像趋势一致，通过对源数组的插值来获得扩展数组。插值的点数就是扩展数组的长度
     * 减去源数组的长度。插值的方法就是在interval的一半处进行插值，所插值通过相邻的两个数据计算平均值得出。并将源数组的数据插入到扩展
     * 数组中。插值完成后，再将源数组中没有插值的最后一段数据全部拷贝到扩展数组中。
     * @param viewwidth
     * @param interpolatdata
     * @return
     */

    public float[] interpolation(int viewwidth, float[] interpolatdata) {

        int interval = interpolatdata.length / viewwidth + 1;//计算扩展数组的长度使用
        int targetlength = interval * viewwidth;//扩展数据的长度
        int interpotatenum = targetlength - interpolatdata.length;//需要在源数组中插值的个数
        // int chazhijiange=interpolatdata.length/interpotatenum+1;
        int chazhijiange = interval / 2;//在源数组中插值的位置
        // Log.e("cc",Integer.valueOf(chazhijiange).toString());
        // int mm = interpolatdata.length / chazhijiange;
        float[] afterinterpolate = new float[targetlength];//插值后的数组
        float[] zhongji = new float[interval];//保存从原数组拷贝的一段数据
        int jj = 0;//插值的间隔
        for (int i = 0; i < interpotatenum; i++) {//插值循环
            float cha = (interpolatdata[jj + chazhijiange - 1] + interpolatdata[jj + chazhijiange]) / 2;//计算插值的数值
            afterinterpolate[jj + chazhijiange] = cha;//将需要插得值放入新的数组中的指定插值位置
            zhongji = Arrays.copyOfRange(interpolatdata, jj, jj + chazhijiange);//从源数组拷贝出一段数据
            int pp = jj;
            for (int kk = 0; kk < chazhijiange; kk++) {//将从源数组中拷贝的数据填入到新的数组中的对应位置
                // Log.e("kk","   当kk=  "+Integer.valueOf(kk).toString()+"  zhongji=   "+Integer.valueOf(zhongji[kk]).toString());
                afterinterpolate[pp + kk] = zhongji[kk];
            }


            //  Log.e("jj",Integer.valueOf(jj+chazhijiange).toString()+"  ii="+Integer.valueOf(i).toString());
            jj = jj + chazhijiange + 1;
        }
        //插值完毕后，将剩下的没有插值的一段数据从源数组拷贝到扩展数组中
        int sourceinterpointer = chazhijiange * interpotatenum;//源数组插值的结束位置
        int targeinterpointer = chazhijiange * interpotatenum + interpotatenum;//扩展数组填入数据的结束位置
        int cc = interpolatdata.length - sourceinterpointer;//没有插值的一段数据的长度
        float[] uuu = Arrays.copyOfRange(interpolatdata, sourceinterpointer, interpolatdata.length);//从源数组拷贝出没有插值的一段数据
        for (int yy = 0; yy < cc; yy++) {//将没有插值的一段数据拷贝到扩展数组的对应位置
            afterinterpolate[yy + targeinterpointer] = uuu[yy];
        }
        // for (int qq=0;qq<afterinterpolate.length;qq++){
        // Log.e("aaa"," 当i= "+Integer.valueOf(qq).toString()+" af值为  "+Integer.valueOf(afterinterpolate[qq]).toString());}
        return afterinterpolate;
    }
}