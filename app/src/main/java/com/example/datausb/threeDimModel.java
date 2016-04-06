package com.example.datausb;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.datausb.MySurfaceView;

/**
 * Created by sunset on 16/3/3.
 */
public class threeDimModel extends android.app.Fragment {
    private MySurfaceView mv;
    static float WIDTH;
    static float HEIGHT;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FrameLayout ff = (FrameLayout) ((main1) getActivity()).findViewById(R.id.contineer);
        float x=ff.getWidth();
        float y=ff.getHeight();
        if(x>y)
        {
            WIDTH=x;
            HEIGHT=y;
        }
        else
        {
            WIDTH=y;
            HEIGHT=x;
        }
        mv=new MySurfaceView((main1) getActivity());
        mv.requestFocus();//获取焦点
        mv.setFocusableInTouchMode(true);//设置为可触控
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = mv;
        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//
    }
    @Override
    public void onResume() {
        super.onResume();
        mv.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mv.onPause();
    }
}

