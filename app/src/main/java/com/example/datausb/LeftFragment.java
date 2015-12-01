package com.example.datausb;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by sunset on 15/11/19.
 */

public class LeftFragment extends Fragment {
   /* private Button deviceInfo;
    private Button dataShow;
    private Button dataSave;
    private Button help;*/
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.left_fragment,container,false);
        return view;


    }
  /*  public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deviceInfo=(Button)getActivity().findViewById(R.id.deviceInfo);
        deviceInfo.setOnClickListener(new chang_flagment());
    }
}

class  chang_flagment implements View.OnClickListener{
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.deviceInfo:

                }
        }*/
    }

