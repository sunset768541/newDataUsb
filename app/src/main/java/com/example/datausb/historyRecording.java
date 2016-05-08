package com.example.datausb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by sunset on 16/5/5.
 */
public class historyRecording extends android.app.Fragment {

        private Spinner spinner;
        private Spinner spinner2;
        private Spinner spinner3;


        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.historyrecord, container, false);

            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            spinner=(Spinner)getActivity().findViewById(R.id.spinner);
            spinner2=(Spinner)getActivity().findViewById(R.id.spinner2);
            spinner3=(Spinner)getActivity().findViewById(R.id.spinner3);

    }

}
