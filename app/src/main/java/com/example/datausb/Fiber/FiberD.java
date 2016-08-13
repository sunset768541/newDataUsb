package com.example.datausb.Fiber;

import android.graphics.Color;

/**
 * Created by sunset on 16/7/28.
 */
public class FiberD extends Fiber {
    private  static FiberD fiberD;
    private  FiberD(){

    }
    public static FiberD createFiberD(){
        if (fiberD==null){
            fiberD=new FiberD();
            fiberD.setFiberName("FiberD");
        }
        return fiberD;
    }

    @Override
    public void setFiberColor() {
        fiberColor=Color.YELLOW;

    }
}
