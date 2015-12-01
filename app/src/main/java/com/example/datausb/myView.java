package com.example.datausb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.View;

/**
 * Created by sunset on 15/11/23.
 */
public class myView extends View {
    private ShapeDrawable mDrawable;

    public myView(Context context){
        super(context);
        int x = 10;
        int y = 10;
        int width = 300;
        int height = 50;

        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setColor(0xff74AC23);
        mDrawable.setBounds(x, y, x + width, y + height);
    }
    protected void onDraw(Canvas canvas){
        // super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        Paint p=new Paint();
        p.setColor(Color.BLUE);
        p.setStrokeWidth(10);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        int viewWidth=this.getWidth();
        mDrawable.draw(canvas);
        Log.d("很好", "哈哈");
        canvas.drawCircle(viewWidth / 10 + 10, viewWidth / 10 + 10, viewWidth / 10, p);
        Log.d("整到", "花完了把");

    }


}


