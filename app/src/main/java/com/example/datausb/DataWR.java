package com.example.datausb;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by sunset on 16/5/6.
 * 用于存储和读取数据，以二进制的形式进行存取。
 * 对于存储，每个小时的数据为一个文件，防止文件过大造成读取困难
 * 根据年月日建立存储数据的文件夹
 */
public class DataWR {

    public static SimpleDateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd-kk-mm");//设置储存文件的显示格式
    public static SimpleDateFormat docFormaty = new SimpleDateFormat("yyyy");//建立文件夹年的名字
    public static SimpleDateFormat docFormatm = new SimpleDateFormat("MM");//建立文件夹月的名字
    public static SimpleDateFormat docFormatd = new SimpleDateFormat("dd");//建立文件夹日的名字
    public static Date date = new Date();
    public static String dir = docFormaty.format(date) + "/" + docFormatm.format(date) + "/" + docFormatd.format(date) + "/";//存储数据文件夹的目录

    public static File file;//file对象用来建立文件夹
    public static String filename = "/mnt/external_sd/" + dir + fileFormat.format(date) + ".dat";//存储数据文件的名字
    public static BufferedOutputStream d;//定义具有缓冲功能的输出流
    public static String[] dataname;
    public static float[] cla;
    public static float[] clb;
    public static boolean isexis = false;
    public static boolean datadoccreat = false;
    /**
     * 初始化函数
     */
//    public static void ini() {
//        try {
//         //   d = new BufferedOutputStream(new FileOutputStream(filename, true));
//        } catch (Exception e) {
//
//        }
//    }

    /**
     * 储存数据的静态方法
     */
    public static void writte(byte[] data) {
        date = new Date();
        String dir1 = docFormaty.format(date) + "/" + docFormatm.format(date) + "/" + docFormatd.format(date) + "/";//存储数据文件夹的目录
        if (dir != dir1) {
            dir = dir1;
            file = new File("/mnt/external_sd/" + dir);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        String filename1 = "/mnt/external_sd/" + dir + fileFormat.format(date) + ".dat";
        if (!filename.equals(filename1)) {
            datadoccreat = false;
            filename = filename1;
            try {
                //在数据文件的最后写时间数据之前要判断是d是否执行了d.write()，以确定是在上一个文档中写入数据

                //在建立新的文件之前对上一个文件写入文件尾，内容为结束的开始时间,时间为毫秒 data.getTime()来获取一个Long型的时间数据，为从1970.1.1到现在的毫秒数
                if (isexis) {
                    d.write(getBytes(date.getTime()));
                    d.close();
                }
                d = new BufferedOutputStream(new FileOutputStream(filename,true));
                isexis = true;
                byte[] ca = new byte[cla.length * 4];
                byte[] cb = new byte[cla.length * 4];
                int jj = 0;
                d.write(getBytesint(8192));
                for (int mm=0;mm<cla.length;mm++){//注意cla里有8193个数据
                    byte []clai=getBytes(cla[mm]);
                    byte []clao=getBytes(clb[mm]);
                    for (int kk=0;kk<4;kk++){
                        ca[jj]=clai[kk];
                        cb[jj]=clao[kk];
                        jj++;
                    }
                }
                d.write(ca);
                d.write(cb);
                d.write(long2byte(date.getTime()));
/******************************************************************************************
 * 该部分用来验证java的基本数据类型转换为byte数组的正确性，经过验证，这些算法都是正确的
 * ******************************************************************************************
//                d.writeFloat(cla[0]);
//                printHexString(getBytes(cla[0]));
//                Log.e("byt2fcla[0]", Float.valueOf(getFloat(getBytes(cla[0]))).toString());
//                d.writeFloat(clb[0]);
//                printHexString(getBytes(clb[0]));
//                Log.e("byt2fclb[0]", Float.valueOf(getFloat(getBytes(clb[0]))).toString());
//
//                d.writeFloat(cla[cla.length - 1]);
//                printHexString(getBytes(cla[cla.length - 1]));
//                Log.e("byt2fcla[leng-1]", Float.valueOf(getFloat(getBytes(cla[cla.length - 1]))).toString());
//
//                d.writeFloat(clb[clb.length - 1]);
//                printHexString(getBytes(cla[clb.length - 1]));
//                Log.e("byt2clb[clb.len-1]f", Float.valueOf(getFloat(getBytes(clb[clb.length - 1]))).toString());
//
//
//                d.write(new byte[]{4, 3, 2, 1});
//
//                d.writeInt(8192);
//                printHexString(getBytesint(8192));
//                Log.e("byte2int", Integer.valueOf(getInt(getBytesint(8192))).toString());
//
//                d.writeLong(date.getTime());
//                printHexString(long2byte(date.getTime()));
//                Log.e("byt2long", Long.valueOf(getLong(long2byte(date.getTime()))).toString());*/

                Log.e("creation", "创建一个新数据文件成功" + "time " + Integer.valueOf(jj).toString());
                datadoccreat=true;
                // byte [] aa={0x1c,0xab,0xaa,0x3f,0x00,0x00,0xc8,0x41};
                //在文件创建的时候为文件添加头，头的内容为数据长度()，标定数据（标定数据的末尾是标定温度）开始时间，时间为毫秒 ,头的长度是固定的
            } catch (Exception e) {
                Log.e("创建数据文件失败", e.toString());
            }
        }
        try {
            if (datadoccreat) {
                d.write(data);//写入数据
            }

        } catch (Exception e) {
            Log.e("写入出错", e.toString());
        }
    }

    public static String[] read(String year, String month, String day, Context cc) {


        String finddatafile = "/mnt/external_sd/" + year + "/" + month + "/" + day;
        Log.e("s", finddatafile);
        File datapathfile = new File(finddatafile);
        if (!datapathfile.exists()) {
            dataname = null;
        } else {
            dataname = datapathfile.list();
        }
        return dataname;

    }

    public static byte[] long2byte(long data) {//将long型数据转换为byte数组
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static long getLong(byte[] bytes) {//将byte数组转换为long型
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16)) | (0xff000000L & ((long) bytes[3] << 24))
                | (0xff00000000L & ((long) bytes[4] << 32)) | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48)) | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public static byte[] getBytes(float data) {//将float值转换为byte数组
        int intBits = Float.floatToIntBits(data);
        return getBytesint(intBits);
    }

    public static float getFloat(byte[] bytes) {//将byte数组转换为float值
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static int getInt(byte[] bytes) {//将byte数组转换为int值
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public static byte[] getBytesint(int data) {//将int型数据转换为byte数组
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static void printHexString(byte[] b) {//将byte数组以16进制的方式打印
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            Log.e("16", hex.toUpperCase());
        }

    }

}
