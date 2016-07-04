package com.example.datausb;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Created by sunset on 16/5/30.
 */
public class DataRD {
    public static RandomAccessFile dataInput;//read(byte[] b,off,datalength)其中,表示把所有数据读到b中，off为数据在b中放置的起始位置;
    public static int datalength = 65536;//读取记录数据的长度
    public static byte data[];//缓存一组数据
    public static long seek;//随机读取数据的指针，指针指向当前开始读取Byte数据的位置
    public static  boolean HAVE_READ_FINISEH=false;//文件读取结束标志位，作用是控制histhrDimModel中的数据显示线程中的while循环可以安全的结束
    public static long datafilelength;//记录数据文件的长度
    public static int[] data_a;//四个通道的数据
    public static int[] data_a1;//
    public static int[] data_b;//
    public static int[] data_b1;//
    public static boolean SHOW_DATA_THREAT_FLAG=false;//3维显示数据线程开始显示一组数据的开始和结束标志位
    public static void iniread(String filepath) throws IOException {//初始化读取数据
        dataInput = new RandomAccessFile(filepath,"resourceObj");//开启新的随机输入流
        data = new byte[datalength];//重新设置数组
        seek=0;//数据读取位置为文件的第一个位置
        datafilelength=filelength(filepath);//获取要读取文件的长度
         HAVE_READ_FINISEH=false;//读取数据标志位为false，表示数据读取没有完成
        SHOW_DATA_THREAT_FLAG=false;//设置显示数据的线程为结束
//        while ((filelength(filepath)-dataoffset)<0){
//            readonce();
//        }
    }
    public static void moveseek()throws IOException{//将随机读取数据的指针移动到指定的位置
        if ((seek)==datafilelength){//如果seek指针移动到了文件的末尾，则标识文件读取完成
            seek=0;//文件读取完成后将seek置0，以便下一次读取
            HAVE_READ_FINISEH=true;//将文件读取完成标志位置为true
        }
        else
        dataInput.seek(seek);//将读数据指针移动到要读取的位置
    }
    public static  void readonce() throws IOException {//读取四通道的指定光纤长度的数据，这个操作要放到线程中进行，防止数据多线程操作中的数据不安全

        for (int i=0;i<datalength;i++){//从文件中读取指定长度的数据存放到数组中
            dataInput.seek(seek+i);
            data[i]=dataInput.readByte();
        }
        //dataInput.read(dataObj);
       // dataInput.skipBytes(12);
       // Log.d("oo", Long.valueOf(dataInput.length()).toString());
        //dataInput.read(dataObj,dataoffset,32758);//
        //Log.d("oo", "读取ok2");
       // dataoffset=datalength+datalength;

    }

    public static long filelength(String file) {//获取文件的长度
        long filelegth = 0;
        File f = new File(file);
        if (f.exists() && f.isFile()) {
            filelegth = f.length();
        } else {
            Log.e("err", "file doesn't exist or is not a file");
        }
        return filelegth;
    }
    public static void stopread()throws IOException{//关闭数据流
        dataInput.close();
    }
    public static int [] bytetoint(byte[] bytestyle){//将2个Byte数据合并为一个整数，注意java中的Byte为-128到127
        int p;
        int p1;
        int i1 = 0;
        int[] combination = new int[bytestyle.length / 2];//combination用于储存合并后的16bit数据
        // for (int i = 0; i < resourceObj.dataObj.length; i++) {//报出空指针异常的原因是接收数据还没有处理完，线程就跳转到了这里
        for (int i = 0; i < bytestyle.length; i = i + 2) {//数据组合

            if (bytestyle[i] < 0) {
                p = 256 + bytestyle[i];

            } else {
                p = bytestyle[i];
            }

            if (bytestyle[i + 1] < 0) {
                p1 = (256 + bytestyle[i + 1]) << 8;

            } else {
                p1 = bytestyle[i + 1] << 8;
            }
            combination[i1] = p + p1;
            i1 = i1 + 1;
        }
        return combination;
    }
    public static void splitdata(){//将数据分为4个通道
        int[] dd=bytetoint(data);
        data_a = Arrays.copyOfRange(dd, 0, dd.length / 4);//copyOfRange(resourceObj,inclusive,exclusive),不包含exclusive那个

        // Log.e("com",Integer.valueOf(combination.length).toString()+"   "+Integer.valueOf(dadd.length).toString());
//                        }
//                        else{
//
//                        }
//                        if (combination[i]==512){
        data_a1 = Arrays.copyOfRange(dd, dd.length / 4, dd.length / 2);
//
//                        }
//                        if (combination[i]==1024){
        data_b = Arrays.copyOfRange(dd, dd.length / 2, dd.length - dd.length / 4);

//
//                        }
//                        if(combination[i]==1536){
        data_b1 = Arrays.copyOfRange(dd, dd.length - dd.length / 4, dd.length);
    }
}



