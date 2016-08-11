package com.example.datausb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.datausb.DataUtil.DataBaseOperation;
import com.example.datausb.DataUtil.DataWR;
import com.example.datausb.Fiber.FiberA;
import com.example.datausb.Fiber.FiberB;
import com.example.datausb.Fiber.FiberC;
import com.example.datausb.Fiber.FiberD;

import java.io.File;

/**
 * Created by wang on 2016/2/23.
 */
public class SystemSetting extends android.app.Fragment {
    private EditText data;
    private Button sentdata;
    private TextView showbyte;
   // private EditText oplong;
    private Button setopl;
    private Switch sw;
    private Switch sw2;
    private EditText temalerttube1;
    private EditText temalerttube2;
    private Spinner datasavetrr;
    private Spinner threscence;
    private EditText fiberAlength;
    private EditText fiberA1450Code;
    private EditText fiberA1663Code;
    private ToggleButton fiberAOpen;
    private EditText fiberBlength;
    private EditText fiberB1450Code;
    private EditText fiberB1663Code;
    private ToggleButton fiberBOpen;
    private EditText fiberClength;
    private EditText fiberC1450Code;
    private EditText fiberC1663Code;
    private ToggleButton fiberCOpen;
    private EditText fiberDlength;
    private EditText fiberD1450Code;
    private EditText fiberD1663Code;
    private ToggleButton fiberDOpen;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.systemseting, container, false);
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
                        SystemParameter.TEM_ALERT_TUBE1 = Integer.valueOf(temalerttube1.getText().toString());
                    } else {
                        // Log.e("jj99",temalerttube1.getText().toString());//当getText为""的时候，Log不会执行

                        SystemParameter.TEM_ALERT_TUBE1 = 0;
                    }
                    //  Log.e("通道1的监控温度为：",Integer.valueOf(system.TEM_ALERT_TUBE1)+"ll");


                }
                Log.e("焦点1变化", "" + SystemParameter.TEM_ALERT_TUBE1);
            }
        });

        temalerttube2 = (EditText) getActivity().findViewById(R.id.editText6);
        temalerttube2.setEnabled(false);
        temalerttube2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!temalerttube2.getText().toString().equals("")) {

                        //  Log.e("jj",temalerttube2.getText().toString());
                        SystemParameter.TEM_ALERT_TUBE2 = Integer.valueOf(temalerttube2.getText().toString());
                    } else {
                        // Log.e("jj99",temalerttube1.getText().toString());//当getText为""的时候，Log不会执行

                        SystemParameter.TEM_ALERT_TUBE2 = 0;
                    }
                    //  Log.e("通道1的监控温度为：",Integer.valueOf(system.TEM_ALERT_TUBE1)+"ll");


                }
                Log.e("焦点2变化", "" + SystemParameter.TEM_ALERT_TUBE2);
            }
        });


        sentdata = (Button) getActivity().findViewById(R.id.button10);
        showbyte = (TextView) getActivity().findViewById(R.id.textView17);
        datasavetrr = (Spinner) getActivity().findViewById(R.id.spinner5);
        threscence = (Spinner) getActivity().findViewById(R.id.spinner4);
        sw2 = (Switch) getActivity().findViewById(R.id.switch2);
        if (((Main) getActivity()).TEM_ALERT == 1) {//这个语句是当设置存储打开后，从系统设置fragment切换走又切换回来时，sw显示的是当前的存储状态
            sw.setChecked(true);
            temalerttube1.setEnabled(true);
            temalerttube2.setEnabled(true);

        }
        sw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TempreatureAlarm.cla1= DataBaseOperation.mDataBaseOperation.getFromDataBase("tube1data");
                    TempreatureAlarm.cla2=DataBaseOperation.mDataBaseOperation.getFromDataBase("tube2data");
                } catch (Exception e) {
                    Toast.makeText(((Main) getActivity()).getApplicationContext(), "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();

                }
                if (sw2.isChecked()) {
                    ((Main) getActivity()).TEM_ALERT = 1;
                    temalerttube2.setEnabled(true);
                    temalerttube1.setEnabled(true);
                    Toast.makeText(((Main) getActivity()), "温度报警已经打开", Toast.LENGTH_SHORT).show();

                } else {
                    ((Main) getActivity()).TEM_ALERT = 0;
                    temalerttube2.setEnabled(false);
                    temalerttube1.setEnabled(false);
                    Toast.makeText(((Main) getActivity()), "温度报警已经关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sw = (Switch) getActivity().findViewById(R.id.switch1);
        if (((Main) getActivity()).STOREDATA == 1) {//这个语句是当设置存储打开后，从系统设置fragment切换走又切换回来时，sw显示的是当前的存储状态
            sw.setChecked(true);
        }
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // File file = new File(DataWR.SDcardPath+"360");//用来检测是否插入了sd卡，因为只要插入sd卡，系统就会在这个sd卡下建立LOST.DIR文件夹
                File file = new File(DataWR.SDcardPath+"LOST.DIR");//用来检测是否插入了sd卡，因为只要插入sd卡，系统就会在这个sd卡下建立LOST.DIR文件夹
                //  Environment.getExternalStorageState();
                // Log.e("ee",Boolean.valueOf(file.exists()).toString());
                //Log.e("ee",Boolean.valueOf(sw.isChecked()).toString());
                int flag = 0;
                if (file.exists()) {
                    flag = 1;
                }
                if (sw.isChecked() && (file.exists())) {//如果开关sw打开，并且插入了sd卡，那么就可以设置STOREDATA标志
                    ((Main) getActivity()).STOREDATA = 1;
                    DataWR.iniSave();
                    Toast.makeText(((Main) getActivity()).getApplication(), "数据存储打开", Toast.LENGTH_SHORT).show();

                } else {
                    sw.setChecked(false);//如果没有检测到sd卡，则不能设置sw为checked状态，也不能设置STOREDATA标志，同时提示没有检测到sd卡
                    ((Main) getActivity()).STOREDATA = 0;
                    if (flag == 0) {
                        Toast.makeText(((Main) getActivity()).getApplication(), "没有检测到SD卡,或重启系统", Toast.LENGTH_SHORT).show();
                    }
                }
                try {
                    DataWR.cla =DataBaseOperation.mDataBaseOperation.getFromDataBase("tube1data");
                    DataWR.clb=DataBaseOperation.mDataBaseOperation.getFromDataBase("tube2data");
                } catch (Exception e) {
                    Toast.makeText(((Main) getActivity()).getApplicationContext(), "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();

                }
            }


        });
        sentdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = data.getText().toString();//String变量
                byte[] b = s.getBytes();//String转换为byte[]
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
                   ((Main) getActivity()).UsbSendData(b);

                } catch (NullPointerException e) {
                    Toast.makeText(((Main) getActivity()).getApplication(), "USB没有接收数据，请先开始接收数据", Toast.LENGTH_SHORT).show();
                }
                //在这里使用getActivity

            }
        });

//      //  oplong = (EditText) getActivity().findViewById(R.id.editText5);
//      //  oplong.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//
//                } else {
//                    int ol = Integer.valueOf(oplong.getText().toString());
//                    ((Main) getActivity()).setFiberLength(ol);
//                    SystemParameter.FIBER_LENGTH = ol;
//                    Log.e("光线的长度", Integer.valueOf(ol).toString());
//                }
//            }
//        });
//        int ll = ((Main) getActivity()).fiberLengthSharePre.getInt("long", 0);
//        if (ll != 0) {
//
//            oplong.setText(Integer.toString(ll));
//        }

        View sysseting = getActivity().findViewById(R.id.sysset);
        /**
         想要一个EditText失去焦点后，其他控件（尤其是别的可以编辑的EditText）不要获得焦点，则可以：
         对于该EditText的父级控件（一般都是LinearLayout），设置对应的focusable和focusableInTouchMode为true：*/
        sysseting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temalerttube2.clearFocus();
              //  oplong.clearFocus();
                temalerttube1.clearFocus();
            }
        });


        fiberAOpen=(ToggleButton) getActivity().findViewById(R.id.toggleButton2);
        fiberBOpen=(ToggleButton) getActivity().findViewById(R.id.toggleButton3);
        fiberCOpen=(ToggleButton) getActivity().findViewById(R.id.toggleButton4);
        fiberDOpen=(ToggleButton) getActivity().findViewById(R.id.toggleButton5);
        fiberA1450Code=(EditText)getActivity().findViewById(R.id.editText7);
        fiberB1450Code=(EditText)getActivity().findViewById(R.id.editText10);
        fiberC1450Code=(EditText)getActivity().findViewById(R.id.editText13);
        fiberD1450Code=(EditText)getActivity().findViewById(R.id.editText16);
        fiberA1663Code=(EditText)getActivity().findViewById(R.id.editText8);
        fiberB1663Code=(EditText)getActivity().findViewById(R.id.editText11);
        fiberC1663Code=(EditText)getActivity().findViewById(R.id.editText14);
        fiberD1663Code=(EditText)getActivity().findViewById(R.id.editText17);
        fiberBlength=(EditText)getActivity().findViewById(R.id.editText9);
        fiberClength=(EditText)getActivity().findViewById(R.id.editText12);
        fiberDlength=(EditText)getActivity().findViewById(R.id.editText15);
        fiberAlength=(EditText) getActivity().findViewById(R.id.editText5);
        fiberAOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   if ((fiberA1663Code.getText().toString().length()!=0)&&(fiberA1450Code.getText().toString().length()!=0)&&(fiberAlength.getText().toString().length()!=0)){
                       FiberA fiberA=FiberA.createFiberA();
                       fiberA.setFiberLength(Integer.parseInt(fiberAlength.getText().toString()));
                       fiberA.setOptical1440Head(Integer.parseInt(fiberA1450Code.getText().toString()));
                       fiberA.setOptical1663Head(Integer.parseInt(fiberA1663Code.getText().toString()));
                       //将Fiber加入到FiberManager中
                       fiberA.setFiberName("A");
                       ((Main)getActivity()).fiberManager.addFiber('A');
                       ((Main) getActivity()).setTunnelAOn();
                   }
                    else{
                       Toast.makeText(getActivity().getApplicationContext(), "请输入正确参数", Toast.LENGTH_SHORT).show();
                       fiberAOpen.setChecked(false);
                   }
                }
                else {
                    ((Main)getActivity()).fiberManager.removeFiber("A");
                    ((Main) getActivity()).setTunnelAOff();

                    //从FiberManager中删除这个光纤
                }

            }
        });
        fiberBOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if ((fiberB1663Code.getText().toString().length()!=0)&&(fiberB1450Code.getText().toString().length()!=0)&&(fiberBlength.getText().toString().length()!=0)){
                        FiberB fiberB=FiberB.createFiberB();
                        fiberB.setFiberLength(Integer.parseInt(fiberBlength.getText().toString()));
                        fiberB.setOptical1440Head(Integer.parseInt(fiberB1450Code.getText().toString()));
                        fiberB.setOptical1663Head(Integer.parseInt(fiberB1663Code.getText().toString()));
                        //将Fiber加入到FiberManager中
                        fiberB.setFiberName("B");

                        ((Main)getActivity()).fiberManager.addFiber('B');
                        ((Main) getActivity()).setTunnelBOn();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "请输入正确参数", Toast.LENGTH_SHORT).show();
                        fiberBOpen.setChecked(false);
                    }
                }
                else {
                    ((Main)getActivity()).fiberManager.removeFiber("B");
                    ((Main) getActivity()).setTunnelBOff();
                    //从FiberManager中删除这个光纤
                }

            }
        });
        fiberCOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if ((fiberC1663Code.getText().toString().length()!=0)&&(fiberC1450Code.getText().toString().length()!=0)&&(fiberClength.getText().toString().length()!=0)){
                        FiberC fiberC= FiberC.createFiberC();
                        fiberC.setFiberLength(Integer.parseInt(fiberClength.getText().toString()));
                        fiberC.setOptical1440Head(Integer.parseInt(fiberC1450Code.getText().toString()));
                        fiberC.setOptical1663Head(Integer.parseInt(fiberC1663Code.getText().toString()));
                        //将Fiber加入到FiberManager中
                        fiberC.setFiberName("C");

                        ((Main)getActivity()).fiberManager.addFiber('C');
                        ((Main) getActivity()).setTunnelCOn();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "请输入正确参数", Toast.LENGTH_SHORT).show();
                        fiberCOpen.setChecked(false);
                    }
                }
                else {
                    ((Main)getActivity()).fiberManager.removeFiber("C");
                    ((Main) getActivity()).setTunnelCOff();

                    //从FiberManager中删除这个光纤
                }

            }
        });
        fiberDOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if ((fiberD1663Code.getText().toString().length()!=0)&&(fiberD1450Code.getText().toString().length()!=0)&&(fiberDlength.getText().toString().length()!=0)){
                        FiberD fiberD=FiberD.createFiberD();
                        fiberD.setFiberLength(Integer.parseInt(fiberDlength.getText().toString()));
                        fiberD.setOptical1440Head(Integer.parseInt(fiberD1450Code.getText().toString()));
                        fiberD.setOptical1663Head(Integer.parseInt(fiberD1663Code.getText().toString()));
                        fiberD.setFiberName("D");
                        //将Fiber加入到FiberManager中
                        ((Main)getActivity()).fiberManager.addFiber('D');
                        ((Main) getActivity()).setTunnelDOn();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "请输入正确参数", Toast.LENGTH_SHORT).show();
                        fiberDOpen.setChecked(false);
                    }
                }
                else {
                    ((Main)getActivity()).fiberManager.removeFiber("D");
                    ((Main) getActivity()).setTunnelDOff();
                    //从FiberManager中删除这个光纤
                }

            }
        });
    }

}
