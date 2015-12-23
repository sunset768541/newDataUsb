/**
 * 这个Fragment被嵌入到MainActivity中，用于显示数据的图像
 */
package com.example.datausb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
//import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sunset on 15/11/19.
 */
public class dataShowTubeA1 extends android.app.Fragment {
    private SurfaceHolder holder;
    Bundle TubeA1data;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datashowtubea1, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//
        Button con = (Button) getActivity().findViewById(R.id.con1);
        SurfaceView sur = (SurfaceView) getActivity().findViewById(R.id.sur1);
        holder = sur.getHolder();
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VV v1 = new VV(getActivity(), holder);
                v1.surfaceChanged(holder, 0, 300, 200);
            }
        });
    }

    private class VV extends SurfaceView implements SurfaceHolder.Callback {
        private MyThreadA1 myThread;

        public VV(Context context, SurfaceHolder holder1) {
            super(context);
        }

        public void surfaceChanged(SurfaceHolder holder1, int format, int width, int height) {
            holder1.addCallback(this);
            TubeA1data = ((MainActivity) getActivity()).get_TubeA1_data();//从Avtivity中获取数据
            myThread = new MyThreadA1(holder1, TubeA1data);//创建一个绘图线程
            myThread.start();

            Log.d("ThreadA1", "RUN");
        }

        public void surfaceCreated(SurfaceHolder holder) {
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }
}

class MyThreadA1 extends Thread {
    private SurfaceHolder holder;
    public boolean isRun;
    Canvas c12 = new Canvas();
    int[] data;
    Bundle tube_data;

    public MyThreadA1(SurfaceHolder holder, Bundle tube_data) {//线程的构造函数传入Bundle携带的数据
        this.holder = holder;
        this.tube_data = tube_data;
        isRun = true;
    }

    @Override
    public void run() {
        synchronized (holder) {
            data = tube_data.getIntArray("data");//读取Bundle中携带的数据
            Path pp1 = new Path();
            Paint pp = new Paint();
            pp.setColor(Color.GREEN);
            pp.setStyle(Paint.Style.STROKE);
            pp.setAntiAlias(true);
            pp.setStrokeWidth(1);
            PathEffect pe = new CornerPathEffect(10);
            pp.setPathEffect(pe);
            c12 = holder.lockCanvas();
            pp1.moveTo(0, 0);
            for (int i = 1; i < data.length; i++) {
                Log.d(Integer.toString(i), Integer.toString(data[i]));
                pp1.lineTo(data[i - 1], data[i]);
            }
            c12.drawPath(pp1, pp);
            holder.unlockCanvasAndPost(c12);//结束锁定画图，并提交改变。

        }
    }


}
