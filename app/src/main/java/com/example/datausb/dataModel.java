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
        int[] tuba;
        int[] tuba1;
        int[] tubeb;
        int[] tubeb1;
        int h;
        int w;
        float fiberLength = 2048;
        float maxnum = 16384;
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
                tube1.setStrokeWidth(1);
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

                tube4.setColor(Color.YELLOW);
                tube4.setStyle(Paint.Style.STROKE);
                tube4.setAntiAlias(true);
                tube4.setStrokeWidth(1);
                tube4.setPathEffect(pe1);

                zuobioa.setColor(Color.WHITE);
                zuobioa.setStyle(Paint.Style.FILL);
                zuobioa.setTextAlign(Paint.Align.CENTER);
                zuobioa.setTextSize(10);
                while (!((main1) getActivity()).stopdatamodelthread) {//while中的语句是保障可以正常的结束线程
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
                        tuba = ((main1) getActivity()).get_TubeA1_data().getIntArray("tubea");
                        tuba1 = ((main1) getActivity()).get_TubeA1_data1().getIntArray("tubea1");
                        tubeb = ((main1) getActivity()).get_TubeA1_data2().getIntArray("tubeb");
                        tubeb1 = ((main1) getActivity()).get_TubeA1_data3().getIntArray("tubeb1");
                        // Log.e("datamodel1通道",Integer.valueOf(tuba[tuba.length-1]).toString()+"  2通道: "+Integer.valueOf(tuba1[tuba.length-1]).toString());
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
                        p1.moveTo(40, h - 400);
                        p2.moveTo(40, h - 280);
                        p3.moveTo(40, h - 160);
                        p4.moveTo(40, h - 40);
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
                        c.drawLine(40, 20, 40, h - 40, paxis);
                        c.drawText("n",40,10,zuobioa);
                        c.drawText("m",w-10,h-20,zuobioa);
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
                             *     | (40,k)----------------------(w-40,k)
                             *     |
                             *     | (40,k)----------------------(w-40,k)
                             *     |
                             *     | (40,k)----------------------(w-40,k)
                             *     |
                             *     | (40,k)----------------------(w-40,k)
                             *     |
                             *     | (40,k)----------------------(w-40,k)
                             *     |
                             *     | (40,k)----------------------(w-40,k)
                             *     |
                             *     | (40,k)----------------------(w-40,k)
                             */
                            float k = h - (i * (h - 40) / ci + 40);//画横轴直线需要的y坐标

                            /**
                             * (m,h/ci)
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             *     |        |       |       |       |
                             * (m,h-40)
                             */
                            float m = i * (w - 40) / ci + 40;//纵轴间距
                            c.drawLine(40, k, w - 40, k, axe);//画横轴(40,k,w-40,k)-->(x1,y1,x2,y2)画的横轴的长度是w-80,为每个循环得到画横轴的纵坐标的值
                            c.drawText(Integer.valueOf((int) y).toString(), (float) 18, (float) k, zuobioa);//在纵坐标上画字符
                            c.drawText(Integer.valueOf((int) x).toString(), (float) m, (float) (h - 10), zuobioa);//在横坐标上画字符
                            c.drawLine(m, h / (ci), m, h - 40, axe);//画纵轴m为每次画纵轴的x坐标，2h-40-h/ci为该纵轴的长度
                        }

                        /**
                         * 绘制各个通道的图像
                         * 共新建4个画笔和4个路径
                         */
                        synchronized (holder) {
                            /*
                            当程序发生异常的中断时，由于下位机中的USB FIFO中任然有数据待读取，这样会使数据图形看起来，通道1中的数据反而比其他的的几个
                            都要大。
                             */
                            //注意数据可能数值太大而不能再屏幕上显示，所以我能要把数据转换成光功率的数值进行显示，也方便后续的计算
                            //由于 屏幕上的点数有限，所以我们只有在屏幕上显示部分点，我们可以用总的点数除以屏幕上的点数，得出的数就是我们要每几个点显示为一个点，显示的这个点为这几个点中的最大值
                            //还有一个实际中存在的问题，每个通道第几个数据并不对应相应的第几个距离。可能还需要排序
                            int[] screentub1 = interpolation(w - 80, tuba);
                            int[] screentub2 = interpolation(w - 80, tuba1);
                            int[] screentub3 = interpolation(w - 80, tubeb);
                            int[] screentub4 = interpolation(w - 80, tubeb1);
                            int[] adp1 = screenadapter(screentub1, w - 80);
                            int[] adp2 = screenadapter(screentub2, w - 80);
                            int[] adp3 = screenadapter(screentub3, w - 80);
                            int[] adp4 = screenadapter(screentub4, w - 80);
                         //   Log.e("hhturb",Integer.valueOf(tuba[tuba.length-1]).toString()+"---"+Integer.valueOf(screentub1[screentub1.length-1]).toString());
                            // Log.e("hh", Integer.valueOf(hh.length).toString() + "--" + Integer.valueOf(hh[66]).toString());
                            // int[] adp2=screenadapter(tuba1,w);
                            //int[] adp3=screenadapter(tubeb,w);
                            //int[] adp4=screenadapter(tubeb1,w);
                            // Log.e("apdlength",Integer.valueOf(adp2[817]).toString()+":"+Integer.valueOf(adp1[816]).toString());
                            /**
                             * 在FPGA上传递增的数据的时候，adp1[i]/400,其中400这个数取值影响了图像在y轴上的分布，这个值取小了，图像的纵坐标就显示不全，取太大在屏幕上看的图线的变化就不是很明显
                             */
                            for (int i = 0; i < adp1.length; i++) {//lineTo显示一条直线是因为纵坐标的数值太大；
                                p1.lineTo(i + 45, -adp1[i] / 400 + h - 400);//在实际的的系统2的14次方，也就是16384，还要在乘以参考电压
                                // p2.lineTo(i+45, -tuba1[i]/400+h-280);
                                p2.lineTo(i + 45, -adp2[i] / 400 + h - 280);
                                //p3.lineTo(i+45, -tubeb1[i]/400+h-160);
                                p3.lineTo(i + 45, -adp3[i] / 400 + h - 160);
                                //p4.lineTo(i+45, -tubeb[i]/400+h-40);
                                p4.lineTo(i + 45, -adp4[i] / 400 + h - 40);
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
            } catch (NullPointerException e) {
                Log.d("datamodel", "数据模式出现空指针异常");


            }

        }
    }

    //进行屏幕大小适配的方法
    public int[] screenadapter(int[] data, int w) {
        // Log.e("jjj",Integer.valueOf(data.length).toString());
        int[] adptertube = new int[w];//设置屏可以显示在屏幕上的数据长度
        int[] databuf;
        int interval = data.length / adptertube.length;
        //  Log.e("输出间隔",Integer.toString(interval)+"    "+Integer.valueOf(data.length).toString()+"   "+Integer.valueOf(w).toString());
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
    public int max(int[] a) {
        int b;
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

    public int[] interpolation(int viewwidth, int[] interpolatdata) {

        int interval = interpolatdata.length / viewwidth + 1;//计算扩展数组的长度使用
        int targetlength = interval * viewwidth;//扩展数据的长度
        int interpotatenum = targetlength - interpolatdata.length;//需要在源数组中插值的个数
        // int chazhijiange=interpolatdata.length/interpotatenum+1;
        int chazhijiange = interval / 2;//在源数组中插值的位置
        // Log.e("cc",Integer.valueOf(chazhijiange).toString());
       // int mm = interpolatdata.length / chazhijiange;
        int[] afterinterpolate = new int[targetlength];//插值后的数组
        int[] zhongji = new int[interval];//保存从原数组拷贝的一段数据
        int jj = 0;//插值的间隔
        for (int i = 0; i < interpotatenum; i++) {//插值循环
            int cha = (interpolatdata[jj + chazhijiange - 1] + interpolatdata[jj + chazhijiange]) / 2;//计算插值的数值
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
        int[] uuu = Arrays.copyOfRange(interpolatdata, sourceinterpointer, interpolatdata.length);//从源数组拷贝出没有插值的一段数据
        for (int yy = 0; yy < cc; yy++) {//将没有插值的一段数据拷贝到扩展数组的对应位置
            afterinterpolate[yy + targeinterpointer] = uuu[yy];
        }
        // for (int qq=0;qq<afterinterpolate.length;qq++){
        // Log.e("aaa"," 当i= "+Integer.valueOf(qq).toString()+" af值为  "+Integer.valueOf(afterinterpolate[qq]).toString());}
        return afterinterpolate;
    }
}


