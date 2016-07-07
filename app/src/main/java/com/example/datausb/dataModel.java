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

import java.util.Arrays;

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
                            int [] adpdata= arrayInterpolation(showLineViewWidth-80,tunnelAdata);
                            int [] adpdata1= arrayInterpolation(showLineViewWidth-80,tunnelA1data);
                            int [] adpdatb= arrayInterpolation(showLineViewWidth-80,tunnelBdata);
                            int [] adpdatb1= arrayInterpolation(showLineViewWidth-80,tunnelB1data);
                            int [] geta= displyViewWidthAdapter(adpdata,showLineViewWidth-80);
                            int [] geta1= displyViewWidthAdapter(adpdata1,showLineViewWidth-80);
                            int [] getb= displyViewWidthAdapter(adpdatb,showLineViewWidth-80);
                            int [] getb1= displyViewWidthAdapter(adpdatb1,showLineViewWidth-80);
                            c.translate(xOrientationOffset, (float) showLineViewHigh-40);

                            p1.moveTo(0,-geta[0]/200);
                            p2.moveTo(0,-geta1[0]/200);
                            p3.moveTo(0,-getb[0]/200);
                            p4.moveTo(0,-getb1[0]/200);

                            for (int i = 0; i < geta.length; i++) {//lineTo显示一条直线是因为纵坐标的数值太大；
                                p1.lineTo(i, -geta[i]/200);            //在实际的的系统2的14次方，也就是16384，还要在乘以参考电压
                                p2.lineTo(i, -geta1[i]/200);
                                p3.lineTo(i, -getb[i]/200);
                                p4.lineTo(i, -getb1[i]/200);
                            }
                           // c.scale((float)(showLineViewWidth-80)/(float) tunnelAdata.length,(float) (showLineViewHigh-70)/(dataMax));
                            tube1.setStrokeWidth(1);
                            tube2.setStrokeWidth(1);
                            tube3.setStrokeWidth(1);
                            tube4.setStrokeWidth(1);
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
    //进行屏幕大小适配的方法
    public int[] displyViewWidthAdapter(int[] needAdapterData, int displayViewWidth) {
        if (needAdapterData.length < displayViewWidth) {
            return needAdapterData;
        } else {
            int[] afterAdapterData = new int[displayViewWidth];//设置屏可以显示在屏幕上的数据长度
             //  int[] dataBuffer;
            int sampleInterval = needAdapterData.length / afterAdapterData.length;
            int afterAdapterDataArrayPionter = 0;
            if (sampleInterval <= 1) {
                afterAdapterData = needAdapterData;
            } else {
                for (int i = 0; i < (needAdapterData.length / sampleInterval) * sampleInterval; i = i + sampleInterval) {//有必要将i先变成整数
                   int[] dataBuffer = Arrays.copyOfRange(needAdapterData, i, i + sampleInterval);
                    afterAdapterData[afterAdapterDataArrayPionter] = getMax(dataBuffer);//这里出现了空指针异常
                    afterAdapterDataArrayPionter = afterAdapterDataArrayPionter + 1;
                }
            }

            return afterAdapterData;
        }
    }
    //对一个数组输出最大值方法
    public int getMax(int[] dataArray) {
        int max;
        Arrays.sort(dataArray);
        max= dataArray[dataArray.length - 1];
        return max;
    }

    /**
     * 屏幕点数与数据匹配函数
     * 基本思想，利用总的数据长度除以显示控件横向像素点数得出一个整数将这个整数+1就得到了interval，interval的作用就是扩展源数据长度
     * 使其可以整除显示控件的横向像素点数。interval*viewwidth viewwidth就是显示控件（坐标系）的横向像素点数，乘积的结果就是要扩展的
     * 数组的长度。为了保证扩展后的数据图像的趋势与源数组的图像趋势一致，通过对源数组的插值来获得扩展数组。插值的点数就是扩展数组的长度
     * 减去源数组的长度。插值的方法就是在interval的一半处进行插值，所插值通过相邻的两个数据计算平均值得出。并将源数组的数据插入到扩展
     * 数组中。插值完成后，再将源数组中没有插值的最后一段数据全部拷贝到扩展数组中。
     * @param displayViewWidth
     * @param needInterporlationArray
     * @return
     */

    public int[] arrayInterpolation(int displayViewWidth, int[] needInterporlationArray) {
        if (needInterporlationArray.length < displayViewWidth) {
            return needInterporlationArray;
        } else {

            int integerOnePiexOfDataNumber = needInterporlationArray.length / displayViewWidth + 1;//计算插值后的数组可以整除显示View的宽度，以协助displyViewWidthAdapter函数抽样
            int afterInterporlationArrayLength = integerOnePiexOfDataNumber * displayViewWidth;//扩展数据的长度
            int needInterpolationDataNumber = afterInterporlationArrayLength - needInterporlationArray.length;//需要在源数组中插值的个数
            int interpolationInterval = integerOnePiexOfDataNumber-1;//插值的间隔
            int[] afterIntetpolationArray = new int[afterInterporlationArrayLength];//插值后的数组
            int needInterporlationArrayPointer = 0;//插值数组的指针，指向插值的位置
            for (int i = 0; i < needInterpolationDataNumber; i++) {//插值循环
                //
                int[] dataBuffer = Arrays.copyOfRange(needInterporlationArray, needInterporlationArrayPointer, needInterporlationArrayPointer + interpolationInterval);//从源数组拷贝出一段数据
                int totalOfDataBufferArray = 0;//dataBufferArray的和
                for (int mm = 0; mm < dataBuffer.length; mm++) {
                    totalOfDataBufferArray = dataBuffer[mm] + totalOfDataBufferArray;
                }
                int interpolationNumber = totalOfDataBufferArray / dataBuffer.length;//计算dataBuffer的平均值获得插值的大小
                afterIntetpolationArray[needInterporlationArrayPointer + interpolationInterval] = interpolationNumber;//将需要插得值放入新的数组中的指定插值位置
                int pp = needInterporlationArrayPointer;

                for (int kk = 0; kk < interpolationInterval; kk++) {//将从源数组中拷贝的数据填入到新的数组中的对应位置
                    afterIntetpolationArray[pp + kk] = dataBuffer[kk];
                }
                needInterporlationArrayPointer = needInterporlationArrayPointer + interpolationInterval + 1;

            }
            //插值完毕后，将剩下的没有插值的一段数据从源数组拷贝到扩展数组中
            int needInterPloArrayPointerPosi =(interpolationInterval) * needInterpolationDataNumber;//源数组插值的结束位置
            int afterInterPloArrayPointerPosi =interpolationInterval * needInterpolationDataNumber + needInterpolationDataNumber;//扩展数组填入数据的结束位置
            int leaftNoInterpoSectionLength = needInterporlationArray.length - needInterPloArrayPointerPosi;//没有插值的一段数据的长度
            int[] noInterpolArraySetion = Arrays.copyOfRange(needInterporlationArray, needInterPloArrayPointerPosi, needInterporlationArray.length);//从源数组拷贝出没有插值的一段数据
            for (int yy = 0; yy < leaftNoInterpoSectionLength; yy++) {//将没有插值的一段数据拷贝到扩展数组的对应位置
                afterIntetpolationArray[yy + afterInterPloArrayPointerPosi] = noInterpolArraySetion[yy];
            }
            return afterIntetpolationArray;
        }
    }

}


