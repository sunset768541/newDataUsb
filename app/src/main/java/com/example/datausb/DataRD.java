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
    public static int datalength = 65536;
    public static byte data[];
    public static long seek;
    public static  boolean HAVE_READ_FINISEH=false;
    public static long datafilelength;
    public static int[] data_a;
    public static int[] data_a1;
    public static int[] data_b;
    public static int[] data_b1;
    public static void iniread(String filepath) throws IOException {
        dataInput = new RandomAccessFile(filepath,"r");
        data = new byte[datalength];
        seek=0;
        datafilelength=filelength(filepath);
//        while ((filelength(filepath)-dataoffset)<0){
//            readonce();
//        }
    }
    public static void moveseek()throws IOException{
        if ((seek)==datafilelength){
            seek=0;
            HAVE_READ_FINISEH=true;
        }
        else
        dataInput.seek(seek);//将读数据指针移动到要读取的位置
    }
    public static  void readonce() throws IOException {//读取四通道的指定光纤长度的数据

        for (int i=0;i<datalength;i++){
            dataInput.seek(seek+i);
            data[i]=dataInput.readByte();
        }
       // dataInput.skipBytes(12);
       // Log.d("oo", Long.valueOf(dataInput.length()).toString());
        //dataInput.read(data,dataoffset,32758);//
        //Log.d("oo", "读取ok2");
       // dataoffset=datalength+datalength;

    }

    public static long filelength(String file) {
        long filelegth = 0;
        File f = new File(file);
        if (f.exists() && f.isFile()) {
            filelegth = f.length();
        } else {
            Log.e("err", "file doesn't exist or is not a file");
        }
        return filelegth;
    }
    public static void stopread()throws IOException{
        dataInput.close();
    }
    public static int [] bytetoint(byte[] bytestyle){
        int p;
        int p1;
        int i1 = 0;
        int[] combination = new int[bytestyle.length / 2];//combination用于储存合并后的16bit数据
        // for (int i = 0; i < r.data.length; i++) {//报出空指针异常的原因是接收数据还没有处理完，线程就跳转到了这里
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
    public static void splitdata(){
        int[] dd=bytetoint(data);
        data_a = Arrays.copyOfRange(dd, 0, dd.length / 4);//copyOfRange(r,inclusive,exclusive),不包含exclusive那个

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



