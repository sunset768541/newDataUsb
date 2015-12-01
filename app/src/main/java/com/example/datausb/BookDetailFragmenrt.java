package com.example.datausb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.example.datausb.R;

import java.util.List;

public class BookDetailFragmenrt extends FragmentActivity {
    private FragmentTabHost mTabHost = null;;
    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragments_tabs);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realContent);

        mTabHost.addTab(mTabHost.newTabSpec("0").setIndicator("设备信息"), RightFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("1").setIndicator("数据显示"), dataShowFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("2").setIndicator("数据储存"), dataSaveFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("3").setIndicator("帮助文档"),helpFragment.class,null);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mTabHost = null;
    }
}