package com.example.datausb;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.PagerTitleStrip;
import android.util.AttributeSet;

/**
 * Created by sunset on 15/11/22.
 */
public  class pa extends PagerTitleStrip {
    private int mIndicatorColor;
    private final Paint mTabPaint = new Paint();
    public pa(Context context) {
        super(context);
    }
    public pa(Context context, AttributeSet attrs){

        super(context, attrs);

    }
    public pa(Context context, AttributeSet attrs, int defStyle){

super(context,attrs);


    }
    public void setTextSpacing(int textSpacing) {
        //if (textSpacing < mMinTextSpacing) {
        //  textSpacing = mMinTextSpacing;
        //}
        super.setTextSpacing(textSpacing);
    }
    public void setTabIndicatorColor(int color) {
        mIndicatorColor = color;
        mTabPaint.setColor(mIndicatorColor);
        invalidate();
    }

}