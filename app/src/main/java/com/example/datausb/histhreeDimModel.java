package com.example.datausb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by sunset on 16/3/3.
 */
public class histhreeDimModel extends android.app.Fragment {
    public MySurfaceView mv;
    static float WIDTH;
    static float HEIGHT;
    float[] caliPSA;
    float[] caliPSB;
    private threedimThread myThread;

    //    public void wakeup() {
    //  ((main1) getActivity()).dta.notifyAll();
    //}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout ff = (FrameLayout) ((main1) getActivity()).findViewById(R.id.showdataframe);
        float x = ff.getWidth();
        float y = ff.getHeight();
        if (x > y) {
            WIDTH = x;
            HEIGHT = y;
        } else {
            WIDTH = y;
            HEIGHT = x;
        }
        mv = new MySurfaceView((main1) getActivity());
        mv.requestFocus();//获取焦点
        mv.setFocusableInTouchMode(true);//设置为可触控
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            caliPSA = ((main1) getActivity()).getfromdatabase(((main1) getActivity()).mDatabase, "tube1data");
            caliPSB = ((main1) getActivity()).getfromdatabase(((main1) getActivity()).mDatabase, "tube2data");
        } catch (Exception e) {
            Toast.makeText(((main1) getActivity()).getApplicationContext(), "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();

        }
        View view = mv;
        // Log.e("onCreat", Integer.valueOf(mv.getcont()).toString());
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//


    }
    public void threadstart(){
      //  boolean datareceive = ((main1) getActivity()).getchange();
        myThread = new threedimThread();//创建一个绘图线程
        myThread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mv.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mv.onPause();
    }

    class threedimThread extends Thread {


        public void run() {
            /**
             * 这个sleep是为了解决当开启设备后，从其他模式切换到当前模式时，由于多线程操作，时访问mRender.lovo1.vCont时会出现空指针异常，这是因为
             * lovo1在实例化之前，线程就切换到了该线程执行访问操作，造成了访问空指针异常，通过让当前的线程sleep 500ms，这期间lovo1就可以实例化，然后
             * 就可以被该线程访问了
             */
            try {
                this.sleep(350);

            } catch (InterruptedException e) {
            }
            try {//捕获线程运行中切换界面而产生的的空指针异常，防止程序崩溃。
                while (!DataRD.HAVE_READ_FINISEH) {
                    DataRD.SHOW_DATA_THREAT_FLAG=true;//标志读取一组数据的操作开始
                    try {
//                        for (int i=0;i<DataRD.datalength;i++){//用每次读取一个Byte的数据放到数组中的效率十分低下，1分钟的数据可以读取6--7分钟；
//                            DataRD.dataInput.seek(DataRD.seek + i);
//                            DataRD.data[i]=DataRD.dataInput.readByte();
//                        }
                        DataRD.dataInput.read(DataRD.data);//这种读取方式十分快，1分钟数据20读取完成
                        DataRD.dataInput.seek(DataRD.seek + DataRD.data.length);

                    } catch (IOException e) {
                        Log.e("出现IO异常", "histhreeDim"+e.toString());
                    }
                    DataRD.splitdata();

//                        /**
//                         * 如果当标志位为false这个线程开始等待
//                         */
//                        if (!((main1) getActivity()).dta.flag1)
//                            try {
//                                ((main1) getActivity()).dta.wait();
//
//                            } catch (InterruptedException ex) {
//
//                            }
//                        else {
//                            ((main1) getActivity()).dta.notifyAll();
//                        }
                    //下面的语句是从Activity中获取数据
                    int[] tuba = DataRD.data_a;
                    int[] tuba1 = DataRD.data_a1;
                    int[] tubeb = DataRD.data_b;
                    int[] tubeb1 = DataRD.data_b1;
                    float[] T1 = new float[tuba.length];
                    float[] T2 = new float[tuba.length];
                    float[] PSA1 = new float[tuba.length];
                    float[] PSA2 = new float[tuba.length];
                    for (int i = 0; i < tuba.length; i++) {
                        if (tuba[i] == 0) {
                            PSA1[i] = 0;
                        } else PSA1[i] = (float) tuba1[i] / tuba[i];
                        if (tubeb[i] == 0) {
                            PSA2[i] = 0;
                        } else PSA2[i] = (float) tubeb1[i] / tubeb[i];
                    }

                    /**
                     * 由公式计算出温度
                     */
                    for (int i = 0; i < tuba.length; i++) {
                        double bb1 = (double) PSA1[i] / caliPSA[i];
                        double bb2 = (double) PSA2[i] / caliPSB[i];
                        //Log.e("PSA","PSA"+Double.valueOf(PSA1[i]).toString()+"PSB"+Double.valueOf(PSA2[i]).toString());
                        //Log.e("calopsa","当前温度"+Float.valueOf(caliPSA[caliPSA.length-1]).toString()+"Acli"+Float.valueOf(caliPSA[i]).toString()+"Bcli"+Float.valueOf(caliPSB[i]).toString());
                        float tt1 = (float) (Math.log(bb1) + 1 / caliPSA[caliPSA.length - 1]);
                        float tt2 = (float) (Math.log(bb2) + 1 / caliPSB[caliPSB.length - 1]);
                        T1[i] = 1 / tt1;
                        T2[i] = 1 / tt2;
                    }
                    float[] TR = screenadapter(T1, mv.mRender.getcont() / 4);//选择T1通道的温度进行显示
                    float[] colors = new float[mv.mRender.getcont()];//创建用于给光纤模型颜色渲染的数据
                    float[] cc = colorprocess(TR);
                    colors = Arrays.copyOfRange(cc, 0, colors.length);
                    mv.mRender.setcolor(colors);
                    DataRD.seek = DataRD.seek + DataRD.datalength;
                    try {
                        DataRD.moveseek();
                       // DataRD.stopread();
                    }
                    catch (IOException e){

                    }
                    Log.e("读文件大小",Long.toString(DataRD.datafilelength));
                    Log.e("指针的位置",Long.toString(DataRD.seek));
                DataRD.SHOW_DATA_THREAT_FLAG=false;//标志读取一组数据操作完成
                }
                try {
                    DataRD.stopread();
                }
                catch (IOException e){

                }
                Log.e("文件读取完成","文件已经读取完成了");
                DataRD.HAVE_READ_FINISEH=false;

            } catch (NullPointerException e)

            {
                Log.d("threedimModel", "历史记录的3维模式出现空指针异常");


            }
        }


        /**
         * 求数组最大
         *
         * @param a
         * @return
         */
        public float max(float[] a) {
            float b;
            Arrays.sort(a);
            b = a[a.length - 1];
            return b;
        }

        /**
         * 求数组的最小
         *
         * @param a
         * @return
         */
        public float min(float[] a) {
            float b;
            Arrays.sort(a);
            b = a[0];
            return b;
        }

        /**
         * 利用伪颜色增强算法把温度数据转换成RGB的形式，温度的变化图请参考drawable中的tempchangecolor.jpg图片
         * 目前这个算法算出的温度颜色与当前的数据有关系，由当前数据的最大值和最小值决定，以后要改为由传感器的测量温度范围决定
         *
         * @param data
         * @return
         */
        public float[] colorprocess(float[] data) {
            float min = min(data);//获得温度数组中最小的数值，数组中每个数组都减去这个最小值，那么这个数组就没有存在负数的可能
            for (int i = 0; i < data.length; i++) {//该for循环就是将数组中的数据全部变成正数
                data[i] = data[i] - min;
            }
            float max = max(data);//获得数组中的最大值，根据这个数值将温度数据分成四个颜色范围
            float[] color = new float[data.length * 4];//用于存储生成的颜色值
            int j = 0;
            for (int i = 0; i < data.length; i++) {
                if (0 <= data[i] && data[i] < max / 4) {//这个范围为蓝色到浅蓝色范围，最小值为纯蓝色【0，0，1】。数值越大越偏向浅蓝色，可以通过增加绿色分量实现
                    color[j] = 0;//红色分量为0
                    color[j + 1] = data[i] / max;//根据温度数据增加绿色分量，
                    color[j + 2] = 1;//纯蓝色
                    color[j + 3] = 1;//透明度为1
                } else if (max / 4 <= data[i] && data[i] < max / 2) {//这个范围为浅蓝色到绿色，【0，1，1】-->[0,1,0],这个过程当数据越大就越少的蓝色分量，这样就向绿色过渡
                    color[j] = 0;//红色分量为0
                    color[j + 1] = 1;//绿色分量为1
                    color[j + 2] = 1 - data[i] / max;//根据温度数值的大小，去除相应的绿色
                    color[j + 3] = 1;
                } else if (max / 2 <= data[i] && data[i] < max * 3 / 4) {//这个范围为绿色到黄色，【0，1，0】-->[1，1，0],这个过程通过增加红色的分量可以实现绿色到黄色的过渡
                    color[j] = data[i] / max;
                    color[j + 1] = 1;
                    color[j + 2] = 0;
                    color[j + 3] = 1;
                } else {//这个范围为黄色到红色的过渡，【1，1，0】-->【1，0，0】,这个过程通过减少绿色的分量就可以达到黄色到红色的颜色过渡
                    color[j] = 1;
                    color[j + 1] = 1 - data[i] / max;
                    color[j + 2] = 0;
                    color[j + 3] = 1;
                }
                j = j + 4;
            }
            return color;
        }

        /**
         * 将光纤的长度与采集温度数据点的个数进行适配
         *
         * @param data
         * @param w
         * @return
         */
        public float[] screenadapter(float[] data, int w) {
            float[] adptertube = new float[w];//设置屏可以显示在屏幕上的数据长度
            float[] databuf;
            int interval = data.length / w + 1;
            // Log.d("输出间隔",Integer.toString(interval)+"    "+Integer.valueOf(data.length).toString()+"   "+Integer.valueOf(w).toString());
            int kkk = 0;
            if (interval <= 1) {
                adptertube = data;
            } else {

                for (int i = 0; i < (data.length / interval) * interval; i = i + interval) {
                    databuf = Arrays.copyOfRange(data, i, i + interval);
                    adptertube[kkk] = max(databuf);//这里出现了空指针异常
                    kkk = kkk + 1;
                }
            }

            return adptertube;
        }
    }
}