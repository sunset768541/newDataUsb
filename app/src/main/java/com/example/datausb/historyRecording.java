package com.example.datausb;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sunset on 16/5/5.
 */
public class historyRecording extends android.app.Fragment {

        private Spinner spinner;
        private Spinner spinner2;
        private Spinner spinner3;
        private ListView listView;
        ArrayAdapter <String>adapter;

        String yea;
        String mon;
        String da;
        String [] datalist;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.historyrecord, container, false);

            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            listView=(ListView)getActivity().findViewById(R.id.listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("点击了", datalist[position].toString());
                }
            });
            spinner=(Spinner)getActivity().findViewById(R.id.spinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    yea = spinner.getSelectedItem().toString();
                    mon = spinner2.getSelectedItem().toString();
                    da = spinner3.getSelectedItem().toString();
                   datalist= DataWR.read(yea, mon, da, ((main1) getActivity()).getApplication());
                    String ss=null;
                    if (datalist!=null){
                        ss=datalist[1]+datalist[2]+datalist[datalist.length-1];
                        adapter=new ArrayAdapter<String>(((main1)getActivity()).getApplication(),R.layout.lis,datalist);
                        listView.setAdapter(adapter);
                    }
                    else {
                        Toast.makeText(((main1)getActivity()).getApplication(), "当前日期没有数据存储", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("ss", yea+"-"+mon+"-"+da+"-"+ss);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinner2=(Spinner)getActivity().findViewById(R.id.spinner2);
            spinner3=(Spinner)getActivity().findViewById(R.id.spinner3);

    }

}
