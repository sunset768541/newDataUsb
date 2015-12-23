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
public class dataShowTubeB1 extends android.app.Fragment {
    private SurfaceHolder holder;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datatubeshowb1, container, false);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//
        /*Button con = (Button) getActivity().findViewById(R.id.conb1);
        SurfaceView sur = (SurfaceView) getActivity().findViewById(R.id.surb1);
        holder = sur.getHolder();
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(1);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        PathEffect pe = new CornerPathEffect(10);
        p.setPathEffect(pe);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // drawC(holder);
                VV v1 = new VV(getActivity(), holder);
                v1.surfaceChanged(holder,0,300,200  );
            }
        });*/
        /*Bitmap b = Bitmap.createBitmap(1000, 100, Bitmap.Config.RGB_565);
        ImageView im = (ImageView) getActivity().findViewById(R.id.cva);
        Canvas cc = new Canvas(b);
        VV v = new VV(getActivity());
        v.draw(cc);
        // Log.d("绘图", "执行完了");
        im.setImageBitmap(b);*/


    }

    private void drawC(SurfaceHolder holder) {
        Canvas cc1 = holder.lockCanvas();
        cc1.drawColor(Color.WHITE);
        Paint pp = new Paint();
        pp.setColor(Color.RED);
        pp.setStrokeWidth(2);
        cc1.drawLine(5, 160, 500, 160, pp);
        cc1.drawLine(5, 40, 5, 200, pp);
        holder.unlockCanvasAndPost(cc1);
        holder.lockCanvas(new Rect(0, 0, 0, 0));
        holder.unlockCanvasAndPost(cc1);
    }

    class VV extends SurfaceView implements SurfaceHolder.Callback {
        private MyThreadB1 myThread;

        public VV(Context context, SurfaceHolder holder1) {
            super(context);
            // TODO Auto-generated constructor stub
            //holder1 = this.getHolder();
           /* holder1.addCallback(this);
            myThread = new MyThread(holder1);//创建一个绘图线程
            myThread.start();
            Log.d("Thread", "kaiqi");*/
        }

        public void surfaceChanged(SurfaceHolder holder1, int format, int width, int height) {
            holder1.addCallback(this);
            myThread = new MyThreadB1(holder1);//创建一个绘图线程
            myThread.start();

            Log.d("ThreadB1", "RUN");
        }

        public void surfaceCreated(SurfaceHolder holder) {
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }

       /* public void run() {
            Path pp1 = new Path();
            pp1.moveTo(20, 20);
            for (int i = 1; i < 1000; i++) {
                pp1.lineTo(i * 10, (float) Math.random() * 50);
            }
            Paint pp = new Paint();
            pp.setColor(Color.RED);
            pp.setStrokeWidth(2);
            Canvas cc12 = holder1.lockCanvas(new Rect(5, 160, 500, 100));
            cc12.drawPath(pp1, pp);
            holder1.unlockCanvasAndPost(cc12);*/
    }


}


class MyThreadB1 extends Thread {
    private SurfaceHolder holder;
    public boolean isRun;
    Canvas c12=new Canvas();
    public MyThreadB1(SurfaceHolder holder) {
        this.holder = holder;
        isRun = true;
    }

    @Override
    public void run() {


        synchronized (holder) {
//                holder.unlockCanvasAndPost(c12);
            Path pp1 = new Path();
            Paint pp = new Paint();
            pp.setColor(Color.RED);
            pp.setStyle(Paint.Style.STROKE);
            pp.setAntiAlias(true);
            pp.setStrokeWidth(1);
            PathEffect pe = new CornerPathEffect(10);
            pp.setPathEffect(pe);
            c12 = holder.lockCanvas();
            pp1.moveTo(40, 40);
            for (int i = 1; i < 1024; i++) {
                pp1.lineTo(i*20, (float) Math.random() * 100);
            }
            //c12 = holder.lockCanvas(new Rect(5, 160, 500, 100));
            c12.drawPath(pp1, pp);
            holder.unlockCanvasAndPost(c12);//结束锁定画图，并提交改变。
               /* Log.d("Run", "yunxing");
                try {
                    Thread.sleep(1000);//睡眠时间为1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
*/
        }
    }



}
