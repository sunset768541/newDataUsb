package com.example.datausb.Fiber;

import android.content.Context;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.Log;
import android.widget.Toast;

import com.example.datausb.DataUtil.DataBaseOperation;

/**
 * Created by sunset on 16/7/28.
 * 1663 Anti-stoke scatting
 * 1440 Stoke scatting
 * Psa=Ps/Pa=1440/1663;
 */
 public abstract class Fiber {
    protected String fiberName;
    protected int fiberColor;
    PathEffect line1440Effect = new DashPathEffect(new float[] {0.1f, 0.1f}, 0.0f);
    public float[] caliPSA;
    protected Paint line1440;
    protected Paint line1663;
    protected Paint calibrate;
    protected int optical1663Head;
    protected int optical1440Head;
    protected int length;
    protected int[] optical1663Data;
    private int[] pre1663Data;
    protected int[] optical1440Data;
    private int[] pre1440Data;
    private char fiberId;
    private boolean isShow;
    private Context context;
    public Fiber() {
        setFiberColor();
        setLine1440();
        setLine1663();
        setCalibratePaint();
    }

    public boolean setCalibrate(){
        try {
            caliPSA= DataBaseOperation.mDataBaseOperation.getFromDataBase(fiberName);
            Log.e(fiberName+"从数据库获得标定数据","-----ok");
            Log.e(fiberName+"标定温度为",Float.valueOf(caliPSA[caliPSA.length-1]).toString());
            Log.e(fiberName+"标定光纤长度为",Integer.valueOf(caliPSA.length-1).toString());
            if (getFiberLength()!=(caliPSA.length-1)){
                Toast.makeText(context, fiberName+"光纤长度已经改变，需重新标定", Toast.LENGTH_SHORT).show();
                Log.e("不等于","光纤长度");
                return false;
            }
        }
        catch (Exception e){
            Log.e("标定数据",Log.getStackTraceString(e));
            Toast.makeText(context, "标定数据不存在，请先在标定模式下进行标定", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public int getOptical1663Head() {
        return optical1663Head;
    }

    public void setOptical1663Head(int optical1663Head) {
        this.optical1663Head = optical1663Head;
    }

    public int getOptical1440Head() {
        return optical1440Head;
    }

    public void setOptical1440Head(int optical1440Head) {
        this.optical1440Head = optical1440Head;
    }

    public int getFiberLength() {
        return length;
    }

    public void setFiberLength(int length) {
        this.length = length;
    }

    public int[] getOptical1663Data() {
        return optical1663Data;
    }

    public void setOptical1663Data(int[] optical1663Data) {
        this.optical1663Data = optical1663Data;
    }

    public int[] getOptical1440Data() {
        return optical1440Data;
    }

    public void setOptical1440Data(int[] optical1440Data) {
        this.optical1440Data = optical1440Data;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }



    public Paint getLine1440() {
        return line1440;
    }

    public  void setLine1440(){
        line1440=new Paint();//开启抗锯齿使得图线变细
        line1440.setStyle(Paint.Style.STROKE);
        line1440.setColor(fiberColor);
        line1440.setStrokeWidth(1f);
        line1440.setPathEffect(line1440Effect);
    }

    public Paint getLine1663() {
        return line1663;
    }

    public  void setLine1663(){
        line1663=new Paint(Paint.ANTI_ALIAS_FLAG);
        line1663.setStyle(Paint.Style.STROKE);
        line1663.setColor(fiberColor);
        line1663.setStrokeWidth(1f);
    }

    public int getFiberColor() {
        return fiberColor;
    }

    public abstract void setFiberColor();

    public String getFiberName() {
        return fiberName;
    }

    protected   void setFiberName(String name){
        fiberName=name;
    };
    public Paint getCalibratePaint() {
        return calibrate;
    }

    public  void setCalibratePaint(){
        calibrate=new Paint(Paint.ANTI_ALIAS_FLAG);
        calibrate.setStyle(Paint.Style.STROKE);
        calibrate.setColor(fiberColor);
        calibrate.setStrokeWidth(1f);
    }
    public float[] calculatePsa(){
       // Log.e("进入计算PSA","ok");
        float []tem =new float[getFiberLength()];
        //Log.e("psa长度",Integer.valueOf(tem.length).toString());
        //Log.e("1663长度=",Integer.valueOf(optical1663Data[10]).toString());
        //Log.e("1440长度=",Integer.valueOf(optical1440Data[20]).toString());
        for (int i=0;i<tem.length;i++){
          //  Log.e("i= ",Integer.valueOf(i).toString());
            if (optical1663Data[i]==0){
                tem[i]=0;
                }
            else {
                tem[i] = (float) optical1440Data[i] / optical1663Data[i];
            }
        }
        Log.e("完场计算PSA","ok");
        return tem;
    }
    public float[] showcCalculateCalibrate(){
        float []tem =new float[getFiberLength()];
        for (int i=0;i<getFiberLength();i++){
            if (optical1663Data[i]==0)
                tem[i]=0;
            else
                tem[i]=-(float)optical1440Data[i]/optical1663Data[i];
        }
        return tem;
    }
    public float[] calculateTempreture(){
        Log.e("进入计算温度","ok");
        float []psa= calculatePsa();
        float [] tem=new float[getFiberLength()];
        if (getFiberLength()==caliPSA.length-1){
        for (int i=0;i<getFiberLength();i++){
            double bb1=(double)psa[i]/caliPSA[i];
            float tt2=(float)(Math.log(bb1)+1/caliPSA[caliPSA.length-1]);
            tem[i]=-1/tt2;
        }
        }
        Log.e("完场计算温度","ok");
        return tem;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public char getFiberId() {
        return fiberId;
    }

    public void setFiberId(char fiberId) {
        this.fiberId = fiberId;
    }

    public int[] getPre1663Data() {
        return pre1663Data;
    }

    public void setPre1663Data(int[] pre1663Data) {
        this.pre1663Data = pre1663Data;
    }

    public int[] getPre1440Data() {
        return pre1440Data;
    }

    public void setPre1440Data(int[] pre1440Data) {
        this.pre1440Data = pre1440Data;
    }
    public void iniPreData(){
        pre1663Data=new int[getFiberLength()];
        pre1440Data=new int[getFiberLength()];
    }

}
