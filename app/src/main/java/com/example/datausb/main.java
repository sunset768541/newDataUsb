
package com.example.datausb;

/*
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


*/
/**
 * Created by sunset on 15/11/19.
 *//*


public class main extends Activity{

    FragmentManager rf1 = getFragmentManager();
    FragmentTransaction ft1 = rf1.beginTransaction();
    FragmentManager rf2 = getFragmentManager();
    FragmentTransaction ft2 = rf2.beginTransaction();
    FragmentManager rf3 = getFragmentManager();
    FragmentTransaction ft3 = rf3.beginTransaction();
    FragmentManager rf4 = getFragmentManager();
    FragmentTransaction ft4 = rf4.beginTransaction();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button deviceInfo = (Button) findViewById(R.id.deviceInfo);
        deviceInfo.setOnClickListener(new  devInf());
        Button dataShow = (Button) findViewById(R.id.dataShow);
        dataShow.setOnClickListener(new daSh());
        Button dataSave = (Button) findViewById(R.id.dataSave);
        dataSave.setOnClickListener(new daSa());
        Button help = (Button) findViewById(R.id.help);
        help.setOnClickListener(new hp());
        ft1.hide(rf1.findFragmentById(R.id.right_fragment));
        ft2.hide(rf2.findFragmentById(R.id.data_Show_Frag));
        ft3.hide(rf3.findFragmentById(R.id.data_Save_Frag));
        ft4.hide(rf4.findFragmentById(R.id.help_fragment));

    }
    class devInf implements View.OnClickListener {
        public void onClick(View v) {
            ft1.hide(rf.findFragmentById(R.id.data_Show_Frag));
            ft2.hide(rf.findFragmentById(R.id.data_Save_Frag));
            ft3.hide(rf.findFragmentById(R.id.help_fragment));
            ft4.show(rf.findFragmentById(R.id.right_fragment));
            ft1.commit();
            }
        }
    class daSh implements View.OnClickListener {
        public void onClick(View v) {
            ft.hide(rf.findFragmentById(R.id.data_Save_Frag));
            ft.hide(rf.findFragmentById(R.id.help_fragment));
            ft.hide(rf.findFragmentById(R.id.right_fragment));
            ft.show(rf.findFragmentById(R.id.data_Show_Frag));
            ft.commit();
        }
    }
    class daSa implements View.OnClickListener {
        public void onClick(View v) {

            ft.hide(rf.findFragmentById(R.id.help_fragment));
            ft.hide(rf.findFragmentById(R.id.right_fragment));
            ft.hide(rf.findFragmentById(R.id.data_Show_Frag));
            ft.show(rf.findFragmentById(R.id.data_Save_Frag));
        }
    }
    class hp implements View.OnClickListener {
        public void onClick(View v) {
            ft.hide(rf.findFragmentById(R.id.right_fragment));
            ft.hide(rf.findFragmentById(R.id.data_Show_Frag));
            ft.hide(rf.findFragmentById(R.id.data_Save_Frag));
            ft.show(rf.findFragmentById(R.id.help_fragment));
        }
    }




}
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class main extends FragmentActivity {
    private ViewPager m_vp;
    private RightFragment mfragment1;
    private dataShowFragment mfragment2;
    private dataSaveFragment mfragment3;
    private helpFragment mfragment4;
    //页面列表
    private ArrayList<Fragment> fragmentList;
    //标题列表
    ArrayList<String>   titleList    = new ArrayList<String>();
    //通过pagerTabStrip可以设置标题的属性
    private pa pagerTabStrip;

    private PagerTitleStrip pagerTitleStrip;
    //ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
    //pager.setAdapter(new TestAdapter(getSupportFragmentManager()));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        m_vp = (ViewPager)findViewById(R.id.viewpager);
        pagerTabStrip=new pa(this);
        m_vp.addView(pagerTabStrip);
        //pagerTabStrip=(pa) findViewById(R.id.pagertab);
        //设置下划线的颜色
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(android.R.color.holo_green_dark));
        //设置背景的颜色
        pagerTabStrip.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        pagerTabStrip.setTextSpacing(64);
        //m_vp.setVisibility(View.GONE);
        //setContentView(pagerTabStrip);
        pagerTabStrip.setVisibility(View.VISIBLE);
        pagerTabStrip.setGravity(Gravity.BOTTOM);
        int bb=pagerTabStrip.getTextSpacing();
        Log.d("ssss",Integer.toString(bb));
//		pagerTitleStrip=(PagerTitleStrip) findViewById(R.id.pagertab);
//		//设置背景的颜色
//		pagerTitleStrip.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        mfragment1 = new RightFragment();
        mfragment2 = new dataShowFragment();
        mfragment3 = new dataSaveFragment();
        mfragment4 = new helpFragment();

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(mfragment1);
        fragmentList.add(mfragment2);
        fragmentList.add(mfragment3);
        fragmentList.add(mfragment4);
        titleList.add("设备状态");
        titleList.add("数据显示");
        titleList.add("数据储存");
        titleList.add("帮助文档");
        m_vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));

    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter{
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }


        public android.support.v4.app.Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titleList.get(position);
        }


    }

}