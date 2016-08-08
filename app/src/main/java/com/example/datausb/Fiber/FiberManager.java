package com.example.datausb.Fiber;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunset on 16/7/28.
 */

public class FiberManager {
    private Map<String,Fiber> fiberMap= new HashMap<>();

    public void addFiber(char tunnelCode){
        switch (tunnelCode){
            case 'A':
                getFiberMap().put(String.valueOf(tunnelCode),FiberA.createFiberA());
                break;
            case 'B':
                getFiberMap().put(String.valueOf(tunnelCode),FiberB.createFiberB());
                break;
            case 'C':
                getFiberMap().put(String.valueOf(tunnelCode),FiberC.createFiberC());
                break;
            case 'D':
                getFiberMap().put(String.valueOf(tunnelCode),FiberD.createFiberD());
                break;
            default:throw new IllegalArgumentException("错误的通道名字，只可以为A,B,C,D");
        }
    }
    public void removeFiber(String tunnelCode){
        try {
            getFiberMap().remove(tunnelCode);
        }
        catch (Exception e){
            Log.e("删除Fiber异常","Fiber没有加入到FiberManger中");
        }
    }
    public void decodeData(int [] data){//解析数据，根据fibeMap里光纤的head14和head16得到各光纤的数据，将得到的数据保存在各自光纤中
        for(int i=0;i<data.length;i++){

            for (Map.Entry<String,Fiber>item: getFiberMap().entrySet()) {//遍历HashMap获得其中光纤的引用
                if (item.getValue().getOptical1450Head()==data[i]){//获得一个光纤后，取出识别码并且与遍历的数据对比
                    item.getValue().setOptical1450Data(Arrays.copyOfRange(data,i,i+item.getValue().getFiberLength()));//，找到后就将这个识别码后的光纤长度个数据存入相应的光纤item中
                    i=i+item.getValue().getFiberLength();//跳过指定长度的数据继续遍历
                }
                if (item.getValue().getOptical1663Head()==data[i]){
                    item.getValue().setOptical1663Data(Arrays.copyOfRange(data,i,i+item.getValue().getFiberLength()));
                    i=i+item.getValue().getFiberLength();
                }

            }

            }


        }

    public Map<String, Fiber> getFiberMap() {
        return fiberMap;
    }


}
