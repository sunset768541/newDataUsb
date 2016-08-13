package com.example.datausb.Fiber;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.Log;

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
    //PathEffect line1440Effect = new DiscretePathEffect(2.0f,3.0f);


    protected Paint line1440;
    protected Paint line1663;
    protected Paint calibrate;
    protected int optical1663Head;
    protected int optical1440Head;
    protected int length;
    protected int[] optical1663Data;
    protected int[] optical1440Data;
    private boolean isShow;
    private float[] clibrateData;
    private float[] tempreture;

    public Fiber() {
        setFiberColor();
        setLine1440();
        setLine1663();
        setCalibrate();
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
    public Paint getCalibrate() {
        return calibrate;
    }

    public  void setCalibrate(){
        calibrate=new Paint(Paint.ANTI_ALIAS_FLAG);
        calibrate.setStyle(Paint.Style.STROKE);
        calibrate.setColor(fiberColor);
        calibrate.setStrokeWidth(1f);
    }
    public float[] calculateCalibrate(){
        float []tem =new float[getFiberLength()];
        for (int i=0;i<getFiberLength();i++){
           // Log.e("第"+Integer.valueOf(i).toString()," ="+Integer.valueOf(optical1663Data[i]).toString());
            if (optical1663Data[i]==0)
                tem[i]=0;
            else
            tem[i]=(float)optical1440Data[i]/optical1663Data[i];
        }
        return tem;
    }
    public float[] showcCalculateCalibrate(){
        float []tem =new float[getFiberLength()];
        for (int i=0;i<getFiberLength();i++){
            // Log.e("第"+Integer.valueOf(i).toString()," ="+Integer.valueOf(optical1663Data[i]).toString());
            if (optical1663Data[i]==0)
                tem[i]=0;
            else
                tem[i]=-(float)optical1440Data[i]/optical1663Data[i];
        }
        return tem;
    }
}
