package com.example.datausb;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Arrays;

/**
 * Created by wang on 2016/2/23.
 */
public class systemSeting extends android.app.Fragment {
    private EditText data;
    private Button sentdata;
    private TextView showbyte;
    private EditText oplong;
    private Button setopl;
    private Switch sw;
    private Switch sw2;
    private EditText temalerttube1;
    private EditText temalerttube2;
    private Spinner datasavetrr;
    private Spinner threscence;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.systemseting, container, false);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = (EditText) getActivity().findViewById(R.id.editText3);
        temalerttube1 = (EditText) getActivity().findViewById(R.id.editText4);
        temalerttube1.setEnabled(false);
        temalerttube1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!temalerttube1.getText().toString().equals("")) {

                        //  Log.e("jj",temalerttube2.getText().toString());
                        system.TEM_ALERT_TUBE1 = Integer.valueOf(temalerttube1.getText().toString());
                    } else {
                        // Log.e("jj99",temalerttube1.getText().toString());//当getText为""的时候，Log不会执行

                        system.TEM_ALERT_TUBE1 = 0;
                    }
                    //  Log.e("通道1的监控温度为：",Integer.valueOf(system.TEM_ALERT_TUBE1)+"ll");


                }
                Log.e("焦点1变化", "" + system.TEM_ALERT_TUBE1);
            }
        });
//        temalerttube1.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                //Log.e("bef：",temalerttube1.getText().toString());
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//               // Log.e("on：",temalerttube1.getText().toString());
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!temalerttube1.getText().toString().equals("")){
//
//                  //  Log.e("jj",temalerttube1.getText().toString());
//                    if (temalerttube1.getText().toString().equals("-")){
//
//                    }
//                    //system.TEM_ALERT_TUBE1=Integer.valueOf(temalerttube1.getText().toString());
//                }
//                else {//当文本框中内容为“”时，设置监控温度为0度
//                   // Log.e("jj99",temalerttube1.getText().toString());//当getText为""的时候，Log不会执行
//
//                    system.TEM_ALERT_TUBE1=0;
//                }
//              //  Log.e("通道1的监控温度为：",Integer.valueOf(system.TEM_ALERT_TUBE1)+"ll");
//
//            }
//        });
        temalerttube2 = (EditText) getActivity().findViewById(R.id.editText6);
        temalerttube2.setEnabled(false);
        temalerttube2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!temalerttube2.getText().toString().equals("")) {

                        //  Log.e("jj",temalerttube2.getText().toString());
                        system.TEM_ALERT_TUBE2 = Integer.valueOf(temalerttube2.getText().toString());
                    } else {
                        // Log.e("jj99",temalerttube1.getText().toString());//当getText为""的时候，Log不会执行

                        system.TEM_ALERT_TUBE2 = 0;
                    }
                    //  Log.e("通道1的监控温度为：",Integer.valueOf(system.TEM_ALERT_TUBE1)+"ll");


                }
                Log.e("焦点2变化", "" + system.TEM_ALERT_TUBE2);
            }
        });
//        temalerttube2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!temalerttube2.getText().toString().equals("")){
//
//                  //  Log.e("jj",temalerttube2.getText().toString());
//                    system.TEM_ALERT_TUBE2=Integer.valueOf(temalerttube2.getText().toString());
//                }
//                else {
//                    // Log.e("jj99",temalerttube1.getText().toString());//当getText为""的时候，Log不会执行
//
//                    system.TEM_ALERT_TUBE2=0;
//                }
//                //  Log.e("通道1的监控温度为：",Integer.valueOf(system.TEM_ALERT_TUBE1)+"ll");
//
//
//            }
//        });
        sentdata = (Button) getActivity().findViewById(R.id.button10);
        showbyte = (TextView) getActivity().findViewById(R.id.textView17);
        datasavetrr = (Spinner) getActivity().findViewById(R.id.spinner5);
        threscence = (Spinner) getActivity().findViewById(R.id.spinner4);
        sw2 = (Switch) getActivity().findViewById(R.id.switch2);
        if (((main1) getActivity()).TEM_ALERT == 1) {//这个语句是当设置存储打开后，从系统设置fragment切换走又切换回来时，sw显示的是当前的存储状态
            sw.setChecked(true);
            temalerttube1.setEnabled(true);
            temalerttube2.setEnabled(true);

        }
        sw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TemAlert.cla1 = ((main1) getActivity()).getfromdatabase(((main1) getActivity()).mDatabase, "tube1data");
                    TemAlert.cla2 = ((main1) getActivity()).getfromdatabase(((main1) getActivity()).mDatabase, "tube2data");
                } catch (Exception e) {
                    Toast.makeText(((main1) getActivity()).getApplicationContext(), "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();

                }
                if (sw2.isChecked()) {
                    ((main1) getActivity()).TEM_ALERT = 1;
                    temalerttube2.setEnabled(true);
                    temalerttube1.setEnabled(true);
                    Toast.makeText(((main1) getActivity()), "温度报警已经打开", Toast.LENGTH_SHORT).show();

                } else {
                    ((main1) getActivity()).TEM_ALERT = 0;
                    temalerttube2.setEnabled(false);
                    temalerttube1.setEnabled(false);
                    Toast.makeText(((main1) getActivity()), "温度报警已经关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sw = (Switch) getActivity().findViewById(R.id.switch1);
        if (((main1) getActivity()).STOREDATA == 1) {//这个语句是当设置存储打开后，从系统设置fragment切换走又切换回来时，sw显示的是当前的存储状态
            sw.setChecked(true);
        }
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("mnt/external_sd/LOST.DIR");//用来检测是否插入了sd卡，因为只要插入sd卡，系统就会在这个sd卡下建立LOST.DIR文件夹
                //  Environment.getExternalStorageState();
                // Log.e("ee",Boolean.valueOf(file.exists()).toString());
                //Log.e("ee",Boolean.valueOf(sw.isChecked()).toString());
                int flag = 0;
                if (file.exists()) {
                    flag = 1;
                }
                if (sw.isChecked() && (file.exists())) {//如果开关sw打开，并且插入了sd卡，那么就可以设置STOREDATA标志
                    ((main1) getActivity()).STOREDATA = 1;
                    Toast.makeText(((main1) getActivity()).getApplication(), "数据存储打开", Toast.LENGTH_SHORT).show();

                } else {
                    sw.setChecked(false);//如果没有检测到sd卡，则不能设置sw为checked状态，也不能设置STOREDATA标志，同时提示没有检测到sd卡
                    ((main1) getActivity()).STOREDATA = 0;
                    if (flag == 0) {
                        Toast.makeText(((main1) getActivity()).getApplication(), "没有检测到SD卡,或重启系统", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        sentdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = data.getText().toString();//String变量
                byte[] b = s.getBytes();//String转换为byte[]
                // String res = new String(b);
                String hex = "0";
                String hh = "";
                for (int i = 0; i < b.length; i++) {
                    hex = Integer.toHexString(b[i] & 0xFF);

                    if (hex.length() == 1) {
                        hex = '0' + hex;
                    }
                    hh = hh + hex;
                }
                showbyte.setText(hh);
                try {
                    ((main1) getActivity()).sentdata(b);

                } catch (NullPointerException e) {
                    Toast.makeText(((main1) getActivity()).getApplication(), "USB没有接收数据，请先开始接收数据", Toast.LENGTH_SHORT).show();
                }
                //在这里使用getActivity

            }
        });

        oplong = (EditText) getActivity().findViewById(R.id.editText5);
        oplong.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    int ol = Integer.valueOf(oplong.getText().toString());
                    ((main1) getActivity()).setpref(ol);
                    system.FIBER_LENGTH = ol;
                    Log.e("光线的长度", Integer.valueOf(ol).toString());
                }
            }
        });
        int ll = ((main1) getActivity()).preferences.getInt("long", 0);
        if (ll != 0) {

            oplong.setText(Integer.toString(ll));
        }

        // setopl = (Button) getActivity().findViewById(R.id.button11);
//        setopl.setOnClickListener(new View.OnClickListener() {
//                                      @Override
//                                      public void onClick(View v) {
//                                          int ol = Integer.valueOf(oplong.getText().toString());
//                                          ((main1) getActivity()).setpref(ol);
//                                      }
//                                  }
//        );
        View sysseting = getActivity().findViewById(R.id.sysset);
        /**
         想要一个EditText失去焦点后，其他控件（尤其是别的可以编辑的EditText）不要获得焦点，则可以：
         对于该EditText的父级控件（一般都是LinearLayout），设置对应的focusable和focusableInTouchMode为true：*/
        sysseting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temalerttube2.clearFocus();
                oplong.clearFocus();
                temalerttube1.clearFocus();
            }
        });
    }

}
