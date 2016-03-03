package com.example.datausb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sunset on 16/3/3.
 */
public class threeDimModel extends android.app.Fragment {
    private  MySurfaceView mv;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mv=new MySurfaceView((main1) getActivity());
        View view = mv;
        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//
    }
}

