package com.example.datausb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
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
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import com.example.datausb.achartengine.ChartFactory;
import com.example.datausb.achartengine.GraphicalView;
import com.example.datausb.achartengine.chart.PointStyle;
import com.example.datausb.achartengine.model.XYMultipleSeriesDataset;
import com.example.datausb.achartengine.model.XYSeries;
import com.example.datausb.achartengine.renderer.XYMultipleSeriesRenderer;
import com.example.datausb.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sunset on 15/11/19.
 */
public class DataModel extends android.app.Fragment {
    /**
     * 定义一个SurfaceHolder用来管理surface
     */
    private SurfaceHolder holder;


    /**
     * 这个函数的作用是使Activity可以唤醒fragment中的显示线程
     */
    public void wakeup() {
        ((Main) getActivity()).dataObj.notifyAll();
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datamodel, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//
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
            //boolean datareceive = ((Main) getActivity()).GetByteDataProcessComlete();
            myThread = new dataThread(holder1, ss);//创建一个绘图线程
            myThread.start();
        }

        public void surfaceCreated(SurfaceHolder holder) {
            holder.addCallback(this);
        }

        public void surfaceDestroyed(SurfaceHolder holder) {

        }


    }
    /**
     * 一个集成Surfaceview并实现了SurfaceHolder.Callback方法的的类
     */



    /**
     * 一个绘图线程类
     */
    class dataThread extends Thread {
        private SurfaceHolder holder;
        public boolean isRun;
        int[] tunnelAdata;
        int[] tunnelA1data;
        int[] tunnelBdata;
        int[] tunnelB1data;
        Canvas c;
        SurfaceView sss;
        DataChart  dataChart;
        /**
         * 该线程的构造函数
         *
         * @param holder，传入的holder用来指定绘图的surfaceview
         * @param ss1，用来获得surfaceview的大小
         */
        public dataThread(SurfaceHolder holder, SurfaceView ss1) {
            this.holder = holder;
            sss = ss1;
            isRun = true;
            dataChart = new DataChart();

        }

        public void run() {
            try {//捕获线程运行中切换界面而产生的的空指针异常，防止程序崩溃。

                while (!((Main) getActivity()).stopDataModelThread) {//while中的语句是保障可以正常的结束线程

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

                        tunnelAdata = ((Main) getActivity()).get_TubeA1_data().getIntArray("tunnelAdata");
                        tunnelA1data = ((Main) getActivity()).get_TubeA1_data1().getIntArray("tunnelA1data");
                        tunnelBdata = ((Main) getActivity()).get_TubeA1_data2().getIntArray("tunnelBdata");
                        tunnelB1data = ((Main) getActivity()).get_TubeA1_data3().getIntArray("tunnelB1data");
                        /**
                         * 1通道：8191  2通道: 16383
                         * 1通道：40959  2通道: 49151
                         * 因为FPGA上传的数据为0--65535，FPGA上传是将16位的数据拆分成2个byte
                         * 而我们一次接收的为65536个byte，也就是收到了一半的数据，要2次可以将65536个数全部收回
                         * 所以可以发现，每个通道的某个点的数据是有2个可能的。因为数据的跳跃很大，过渡不是平滑的，所以也造成了图像剧烈的抖动。
                         */

//                         * c为锁定suface时获得的一个画布，我们可以在上面画图

                        c = holder.lockCanvas();

                        synchronized (holder) {
                            List<float[]> data=new ArrayList<>();
                            data.add(intArray2MinusFloat(tunnelAdata));
                            data.add(intArray2MinusFloat(tunnelA1data));
                            data.add(intArray2MinusFloat(tunnelBdata));
                            data.add(intArray2MinusFloat(tunnelB1data));
                            dataChart.drawAll(c,data,new int[]{Color.CYAN,Color.BLUE,Color.RED,Color.YELLOW});
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
                           //Log.e("dataModer","一次");
                        }



                    }

                }
            } catch (NullPointerException e) {
                Log.d("数据模式空指针异常", Log.getStackTraceString(e));


            }

        }

    }
    public float[] intArray2MinusFloat(int[] a){
     float[] b=  new float[a.length];
        for (int i=0;i<a.length;i++){
            b[i]=-(float)a[i];
        }
        return b;

    }
}

