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
    public  static  String [] dataname;

    /**
     * 初始化函数
     */
    public static void ini() {
        try {
            d = new BufferedOutputStream(new FileOutputStream(filename, true));

        } catch (Exception e) {

        }
    }

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
            filename = filename1;
            try {
                d.close();
                d = new BufferedOutputStream(new FileOutputStream(filename, true));
            } catch (Exception e) {
            }
        }
        try {
            d.write(data);//写入数据

        } catch (Exception e) {
            Log.e("写入出错", e.toString());
        }
    }

    public static String[] read(String year,String month,String day,Context cc) {


        String finddatafile="/mnt/external_sd/"+year+"/"+month+"/"+day;
        Log.e("s", finddatafile);
        File datapathfile=new File(finddatafile);
        if (!datapathfile.exists()){
            dataname=null;
        }
        else {
             dataname=datapathfile.list();
        }
        return dataname;

    }

}
