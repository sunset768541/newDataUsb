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
     * 一个绘图线程类
     */
    class dataThread extends Thread {
        private SurfaceHolder holder;
        public boolean isRun;
        int[] tunnelAdata;
        int[] tunnelA1data;
        int[] tunnelBdata;
        int[] tunnelB1data;
        int showLineViewHigh;
        int showLineViewWidth;
        float fiberLength = 2048;
        float maxnum = 16384;
        float dataMax=65536;
        float xOrientationOffset =40;
        /**
         * 定义了两支画笔
         * paxis用来画横纵坐标轴
         * axe用来绘制坐标轴中的坐标
         */
        Paint paxis = new Paint();
        Paint axe = new Paint();
        Paint zuobioa = new Paint();


        Paint tube1 = new Paint();
        Paint tube2 = new Paint();
        Paint tube3 = new Paint();
        Paint tube4 = new Paint();
        PathEffect pe1 = new CornerPathEffect(10);


        Canvas c;
        SurfaceView sss;
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
            //alltubedata=cc;
            showLineViewHigh = sss.getHeight();
            showLineViewWidth = sss.getWidth();
        }

        public void run() {
            try {//捕获线程运行中切换界面而产生的的空指针异常，防止程序崩溃。
                axe.setARGB(70, 0, 189, 30);
                //axe.setARGB(255, 83, 83, 83);
                axe.setStyle(Paint.Style.STROKE);
                axe.setStrokeWidth(1);
                paxis.setARGB(255, 83, 83, 83);
                paxis.setStyle(Paint.Style.STROKE);
                paxis.setStrokeWidth(3);
                tube1.setColor(Color.CYAN);
                tube1.setStyle(Paint.Style.STROKE);
                tube1.setAntiAlias(true);
                tube1.setPathEffect(pe1);

                tube2.setColor(Color.RED);
                tube2.setStyle(Paint.Style.STROKE);
                tube2.setAntiAlias(true);
                tube2.setPathEffect(pe1);

                tube3.setColor(Color.BLUE);
                tube3.setStyle(Paint.Style.STROKE);
                tube3.setAntiAlias(true);
                tube3.setPathEffect(pe1);

                tube4.setColor(Color.YELLOW);
                tube4.setStyle(Paint.Style.STROKE);
                tube4.setAntiAlias(true);
                tube4.setPathEffect(pe1);

                zuobioa.setColor(Color.WHITE);
                zuobioa.setStyle(Paint.Style.FILL);
                zuobioa.setTextAlign(Paint.Align.CENTER);
                zuobioa.setTextSize(10);
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
                        Path p1 = new Path();
                        Path p2 = new Path();
                        Path p3 = new Path();
                        Path p4 = new Path();
                        /**
                         * c为锁定suface时获得的一个画布，我们可以在上面画图
                         */
                        c = holder.lockCanvas();

                        /**
                         * 绘制整个画布的颜色
                         */
                        c.drawRGB(15, 15, 15);
                        //c.drawRGB(38, 38, 38);
                        /**
                         * 绘制横纵坐标轴
                         */
                        c.drawLine(40, 20, 40, showLineViewHigh - 40, paxis);
                        c.drawText("n",40,10,zuobioa);
                        c.drawText("m", showLineViewWidth -10, showLineViewHigh -20,zuobioa);
                        c.drawLine(40, showLineViewHigh - 40, showLineViewWidth - 10, showLineViewHigh - 40, paxis);//绘制坐标轴
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
                            float k = showLineViewHigh - (i * (showLineViewHigh - 40) / ci + 40);//画横轴直线需要的y坐标

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
                            float m = i * (showLineViewWidth - xOrientationOffset) / ci + xOrientationOffset;//纵轴间距
                            c.drawLine(xOrientationOffset, k, showLineViewWidth - xOrientationOffset, k, axe);//画横轴(40,k,showLineViewWidth-40,k)-->(x1,y1,x2,y2)画的横轴的长度是w-80,为每个循环得到画横轴的纵坐标的值
                            c.drawText(Integer.valueOf((int) y).toString(), (float) 18, (float) k, zuobioa);//在纵坐标上画字符
                            c.drawText(Integer.valueOf((int) x).toString(), (float) m, (float) (showLineViewHigh - 10), zuobioa);//在横坐标上画字符
                            c.drawLine(m, showLineViewHigh / (ci), m, showLineViewHigh - 40, axe);//画纵轴m为每次画纵轴的x坐标，2h-40-showLineViewHeigth/ci为该纵轴的长度
                        }

                        /**
                         * 绘制各个通道的图像
                         * 共新建4个画笔和4个路径
                         */
                        synchronized (holder) {
                        p1.moveTo(xOrientationOffset,-tunnelAdata[0]);
                        p2.moveTo(xOrientationOffset,-tunnelA1data[0]);
                        p3.moveTo(xOrientationOffset,-tunnelBdata[0]);
                        p4.moveTo(xOrientationOffset,-tunnelB1data[0]);
                            for (int i = 0; i < tunnelAdata.length; i++) {//lineTo显示一条直线是因为纵坐标的数值太大；
                                p1.lineTo(i, -tunnelAdata[i]);            //在实际的的系统2的14次方，也就是16384，还要在乘以参考电压
                                p2.lineTo(i, -tunnelA1data[i]);
                                p3.lineTo(i, -tunnelBdata[i]);
                                p4.lineTo(i, -tunnelB1data[i]);
                            }
                            c.translate(xOrientationOffset, (float) showLineViewHigh-40);
                            c.scale((float)(showLineViewWidth-80)/(float) tunnelAdata.length,(float) (showLineViewHigh-70)/(dataMax));
                            tube1.setStrokeWidth(dataMax/(float) showLineViewHigh);
                            tube2.setStrokeWidth(dataMax/(float) showLineViewHigh);
                            tube3.setStrokeWidth(dataMax/(float) showLineViewHigh);
                            tube4.setStrokeWidth(dataMax/(float) showLineViewHigh);
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

                            ((Main) getActivity()).dataObj.flag1 = false;
                            ((Main) getActivity()).wakeUpAllMainThread();
                        }


                    }


                }
            } catch (NullPointerException e) {
                Log.d("datamodel", "数据模式出现空指针异常");


            }

        }
    }


}


