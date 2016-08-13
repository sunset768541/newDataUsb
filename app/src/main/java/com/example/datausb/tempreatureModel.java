package com.example.datausb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

import com.example.datausb.DataUtil.DataBaseOperation;

import java.util.ArrayList;
import java.util.List;

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
            caliPSA= DataBaseOperation.mDataBaseOperation.getFromDataBase("tube1data");
            caliPSB=DataBaseOperation.mDataBaseOperation.getFromDataBase("tube2data");
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();

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
        drawLineSurface v1 = new drawLineSurface(getActivity(), holder, sur);
        /**
         * 调用这个surfaceview的surfaceCreated方法
         */
        v1.surfaceCreated(holder);

    }

    /**
     * 一个继承Surfaceview并实现了SurfaceHolder.Callback方法的的类
     */

    class drawLineSurface extends SurfaceView implements SurfaceHolder.Callback {
        private tempreatureThread myThread;
        SurfaceView surfaceView;

        /**
         * 该类的构造函数
         *
         * @param context
         * @param holder1，传入holder，给绘图线程使用
         * @param surfaceView，传入sur使绘图线程获得当前surfaceview的大小
         */
        public drawLineSurface(Context context, SurfaceHolder holder1, SurfaceView surfaceView) {
            super(context);
            this.surfaceView = surfaceView;
        }

        public void surfaceChanged(SurfaceHolder holder1, int a, int b, int c) {
            holder1.addCallback(this);
            myThread = new tempreatureThread(holder1, surfaceView);//创建一个绘图线程
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

        int showLineSurfaceViewHeught;
        int showLineSurfaceViewWidth;
        SurfaceView showLineSurfaceView;
        float fiberLength = 2048;
        float maxnum = 16384;
        List<Float> T1p=new ArrayList<>();
        List<Float> T2p=new ArrayList<>();

        float[]tp1;
        float[]tp2;
        DataChart dataChart;

        /**
         * 该线程的构造函数
         *
         * @param holder，传入的holder用来指定绘图的surfaceview
         * @param surfaceView，用来获得surfaceview的大小
         */
        public tempreatureThread(SurfaceHolder holder, SurfaceView surfaceView) {
            this.holder = holder;
            showLineSurfaceView = surfaceView;
            isRun = true;
            showLineSurfaceViewHeught = showLineSurfaceView.getHeight();
            showLineSurfaceViewWidth = showLineSurfaceView.getWidth();
            dataChart=new DataChart(8200,0,68000,0);
            dataChart.setyMax(50);
        }

        public void run() {
            try {//捕获线程运行中切换界面而产生的的空指针异常，防止程序崩溃。
            while (!((Main) getActivity()).stopTemperatureModelThread) {
                    synchronized (((Main) getActivity()).dataObj) {//所有的等待和唤醒的锁都是同一个，这里选用了Activity中的一个对象
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
                        int[] tuba = ((Main) getActivity()).get_TubeA1_data().getIntArray("tunnelAdata");
                        int[] tuba1 = ((Main) getActivity()).get_TubeA1_data1().getIntArray("tunnelA1data");
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
                            float tt1=(float)(Math.log(bb1)+1/caliPSA[caliPSA.length-1]);
                            float tt2=(float)(Math.log(bb2)+1/caliPSB[caliPSB.length-1]);

                            T1[i]=-1/tt1;
                            T2[i]=-1/tt2;
                        }
                        /**
                         * 定义了两支画笔
                         * paxis用来画横纵坐标轴
                         * axe用来绘制坐标轴中的坐标
                         */

                        Canvas c = holder.lockCanvas();
                       List<float[]> tem=new ArrayList<>();
                        tem.add(T1);
                        tem.add(T2);

                       // dataChart.drawAll(c,tem,new int[]{Color.RED,Color.YELLOW});
                       // Log.e("温度模式","ok");

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
                        }


                    }



                }
            catch (NullPointerException e) {
                Log.d("tempretureModel", Log.getStackTraceString(e));


            }

        }



    }

}