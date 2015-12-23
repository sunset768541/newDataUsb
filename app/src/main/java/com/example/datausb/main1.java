package com.example.datausb;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by sunset on 15/12/11.
 */

public class main1 extends Activity {
    /**
     * 实例化3个用于显示不同界面的Fragment
     * da为数据显示界面
     * db为数据标定界面
     * dc为温度和距离的显示界面
     */
    dataShowTubeA da = new dataShowTubeA();
    dataShowTubeB db = new dataShowTubeB();
    dataShowTubeB1 dc = new dataShowTubeB1();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        /**
         * 实例化3个按钮用来切换不同的Fragment
         * datamodle用来切换到数据显示模式
         * calibration用于切换到标定模式
         * temperature用来切换到温度距离显示模式
         * 并为按钮添加监听函数
         */
        Button datamodle = (Button) findViewById(R.id.button);
        datamodle.setOnClickListener(new rd());
        Button calibration = (Button) findViewById(R.id.button2);
        calibration.setOnClickListener(new ca());
        Button temperature = (Button) findViewById(R.id.button3);
        temperature.setOnClickListener(new te());
    }

    /**
     * 按钮datamodle的监听函数
     * 每个FragmentTransanction的commit只能提交一次，所以我们在按下每个按钮时从新实例化一个FragmentTransanction进行提交操作
     */
    class rd implements View.OnClickListener {
        public void onClick(View v) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.contineer, da);
            transaction.addToBackStack(null);
            transaction.commit();
            Log.d("hh", "hha");
        }


    }
    /**
     * 按钮calibration的监听函数
     */
    class ca implements View.OnClickListener {
        public void onClick(View v) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contineer, db);
            transaction.addToBackStack(null);
            transaction.commit();
            Log.d("l", "hha");

        }


    }
    /**
     * 按钮temperature的监听函数
     */
    class te implements View.OnClickListener {
        public void onClick(View v) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contineer, dc);
            transaction.addToBackStack(null);
            transaction.commit();
            Log.d("dh", "hha");

        }


    }
}
