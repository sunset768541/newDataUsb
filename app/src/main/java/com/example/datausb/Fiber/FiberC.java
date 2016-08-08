package com.example.datausb.Fiber;

import android.graphics.Color;

/**
 * Created by sunset on 16/7/28.
 */
public class FiberC extends Fiber {
    public final char tunnelCode='C';
    public int lineColor= Color.GREEN;
    private  static FiberC fiberC;
    private  FiberC(){

    }
    public static FiberC createFiberC(){
        if (fiberC==null){
            fiberC=new FiberC();
        }
        return fiberC;
    }

    @Override
    public void setFiberColor() {
        fiberColor=Color.GREEN;
    }
}
