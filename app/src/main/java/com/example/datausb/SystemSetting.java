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
import com.example.datausb.Fiber.Fiber;
import com.example.datausb.Fiber.FiberA;
import com.example.datausb.Fiber.FiberB;
import com.example.datausb.Fiber.FiberC;
import com.example.datausb.Fiber.FiberD;
import com.example.datausb.Fiber.FiberManager;

import java.io.File;
import java.util.Map;

/**
 * Created by wang on 2016/2/23.
 * 系统设置Fragment
 */
public class SystemSetting extends android.app.Fragment {
    private EditText fiberLength;
    private EditText averageTimes;
    private Button sendDataToUsb;
    private TextView showSendData;
    private final String STATR_CODE="1111111";
   // private EditText oplong;
    private Button setopl;
    private Switch sw;
    private Switch sw2;
    private EditText temalerttube1;
    private EditText temalerttube2;
    private Spinner datasavetrr;
    private Spinner threscence;
//    private EditText fiberAlength;
//    private EditText fiberA1450Code;
//    private EditText fiberA1663Code;
    private ToggleButton fiberAOpen;
//    private EditText fiberBlength;
//    private EditText fiberB1450Code;
//    private EditText fiberB1663Code;
    private ToggleButton fiberBOpen;
//    private EditText fiberClength;
//    private EditText fiberC1450Code;
//    private EditText fiberC1663Code;
    private ToggleButton fiberCOpen;
//    private EditText fiberDlength;
//    private EditText fiberD1450Code;
//    private EditText fiberD1663Code;
    private ToggleButton fiberDOpen;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.systemseting, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fiberLength = (EditText) getActivity().findViewById(R.id.editText3);
        temalerttube1 = (EditText) getActivity().findViewById(R.id.editText4);
        temalerttube1.setEnabled(false);
        temalerttube1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                        Log.e("聚焦","ok");
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
                        Log.e("hasFocus","ok");
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


        sendDataToUsb = (Button) getActivity().findViewById(R.id.button10);
        showSendData = (TextView) getActivity().findViewById(R.id.textView17);
        datasavetrr = (Spinner) getActivity().findViewById(R.id.spinner5);
        threscence = (Spinner) getActivity().findViewById(R.id.spinner4);
        sw2 = (Switch) getActivity().findViewById(R.id.switch2);
        if (Main.TEM_ALERT == 1) {//这个语句是当设置存储打开后，从系统设置fragment切换走又切换回来时，sw显示的是当前的存储状态
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
                    Toast.makeText( getActivity().getApplicationContext(), "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();

                }
                if (sw2.isChecked()) {
                    Main.TEM_ALERT = 1;
                    temalerttube2.setEnabled(true);
                    temalerttube1.setEnabled(true);
                    Toast.makeText( getActivity(), "温度报警已经打开", Toast.LENGTH_SHORT).show();

                } else {
                    Main.TEM_ALERT = 0;
                    temalerttube2.setEnabled(false);
                    temalerttube1.setEnabled(false);
                    Toast.makeText(getActivity(), "温度报警已经关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sw = (Switch) getActivity().findViewById(R.id.switch1);
        if (Main.STOREDATA == 1) {//这个语句是当设置存储打开后，从系统设置fragment切换走又切换回来时，sw显示的是当前的存储状态
            sw.setChecked(true);
        }
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(DataWR.SDcardPath+"Android");//用来检测是否插入了sd卡，因为只要插入sd卡，系统就会在这个sd卡下建立LOST.DIR文件夹
               // File file = new File(DataWR.SDcardPath+"LOST.DIR");//用来检测是否插入了sd卡，因为只要插入sd卡，系统就会在这个sd卡下建立LOST.DIR文件夹
                // Log.e("ee",Boolean.valueOf(file.exists()).toString());
                //Log.e("ee",Boolean.valueOf(sw.isChecked()).toString());
                int flag = 0;
                if (file.exists()) {
                    flag = 1;
                }
                try {
                    /**
                     * 遍历FiberManger并设置光纤的标定温度
                     * for (Map.Entry<String,Fiber>item: ((Main)getActivity()).fiberManager.getFiberMap().entrySet())
                     */
                    for (Map.Entry<String,Fiber>item: ((Main)getActivity()).fiberManager.getFiberMap().entrySet()){//如果设置标定温度返回false则不能开启储存
                        if (!item.getValue().setCalibrate()){
                            Main.STOREDATA = 0;
                            sw.setChecked(false);
                        }
                    }
//                    DataWR.cla =DataBaseOperation.mDataBaseOperation.getFromDataBase("tube1data");
//                    DataWR.clb=DataBaseOperation.mDataBaseOperation.getFromDataBase("tube2data");
                } catch (Exception e) {
                    Toast.makeText( getActivity().getApplicationContext(), "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();

                }
                if (sw.isChecked() && (file.exists())) {//如果开关sw打开，并且插入了sd卡，那么就可以设置STOREDATA标志
                    Main.STOREDATA = 1;
                    DataWR.iniSave();
                    Toast.makeText(getActivity().getApplication(), "数据存储打开", Toast.LENGTH_SHORT).show();

                } else {
                    sw.setChecked(false);//如果没有检测到sd卡，则不能设置sw为checked状态，也不能设置STOREDATA标志，同时提示没有检测到sd卡
                    Main.STOREDATA = 0;
                    if (flag == 0) {
                        Toast.makeText( getActivity().getApplication(), "没有检测到SD卡,或重启系统", Toast.LENGTH_SHORT).show();
                    }
                }

            }


        });
        averageTimes=(EditText)getActivity().findViewById(R.id.editText22);
        sendDataToUsb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String length = fiberLength.getText().toString();//String变量
                String average=averageTimes.getText().toString();
                byte[] b = (STATR_CODE+length+average).getBytes();//String转换为byte[]
                String hex = "0";
                String hh = "";
                for (int i:b) {
                    hex = Integer.toHexString(i & 0xFF);

                    if (hex.length() == 1) {
                        hex = '0' + hex;
                    }
                    hh = hh + hex;

                }
                showSendData.setText(hh);
                try {
                   ((Main) getActivity()).UsbSendData(b);
                    FiberManager.fiberLength=Integer.parseInt(length);
                } catch (NullPointerException e) {
                    Toast.makeText(getActivity().getApplication(), "USB没有接收数据，请先开始接收数据", Toast.LENGTH_SHORT).show();
                }

            }
        });


        View sysseting = getActivity().findViewById(R.id.sysset);
        /**
         想要一个EditText失去焦点后，其他控件（尤其是别的可以编辑的EditText）不要获得焦点，则可以：
         对于该EditText的父级控件（一般都是LinearLayout），设置对应的focusable和focusableInTouchMode为true：*/
        sysseting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temalerttube2.clearFocus();
                temalerttube1.clearFocus();
            }
        });


        fiberAOpen=(ToggleButton) getActivity().findViewById(R.id.toggleButton2);
        fiberBOpen=(ToggleButton) getActivity().findViewById(R.id.toggleButton3);
        fiberCOpen=(ToggleButton) getActivity().findViewById(R.id.toggleButton4);
        fiberDOpen=(ToggleButton) getActivity().findViewById(R.id.toggleButton5);
        fiberAOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   if ((fiberLength.getText().toString().length()!=0)){
                       FiberA fiberA=FiberA.createFiberA();
                       fiberA.setFiberLength(Integer.parseInt(fiberLength.getText().toString()));
                       fiberA.setContext(getActivity().getApplicationContext());
                       //将Fiber加入到FiberManager中
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
                    if ((fiberLength.getText().toString().length()!=0)){
                        FiberB fiberB=FiberB.createFiberB();
                        fiberB.setFiberLength(Integer.parseInt(fiberLength.getText().toString()));
                        fiberB.setContext(getActivity().getApplicationContext());
                        //将Fiber加入到FiberManager中
                        ((Main)getActivity()).fiberManager.addFiber('B');
                        ((Main) getActivity()).setTunnelBOn();
                    //}
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
                    if ((fiberLength.getText().toString().length()!=0)){
                        FiberC fiberC= FiberC.createFiberC();
                        fiberC.setFiberLength(Integer.parseInt(fiberLength.getText().toString()));
                        fiberC.setContext(getActivity().getApplicationContext());
                        //将Fiber加入到FiberManager中
                        ((Main)getActivity()).fiberManager.addFiber('C');
                        ((Main) getActivity()).setTunnelCOn();
                    //}
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
                    if ((fiberLength.getText().toString().length()!=0)){
                        FiberD fiberD=FiberD.createFiberD();
                        fiberD.setFiberLength(Integer.parseInt(fiberLength.getText().toString()));
                        fiberD.setContext(getActivity().getApplicationContext());
                           //从数据库中读取标定数据
                           //将Fiber加入到FiberManager中
                           ((Main) getActivity()).fiberManager.addFiber('D');
                           ((Main) getActivity()).setTunnelDOn();
                      // }
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
