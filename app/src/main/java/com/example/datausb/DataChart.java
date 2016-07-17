package com.example.datausb;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;


import java.util.List;

/**
 * Created by sunset on 16/7/16.
 */
public class DataChart {
    private double xMax;
    private double xMin;
    private double yMax;
    private double yMin;
    private int xAxisColor;
    private int yAxisColor;
    private int scale=1;
    private Point center;
    private int [] margin;//new int[]{40,40,20,20};//左右下上
    private String xLabel="x";
    private String yLabel="y";
    private int textColor;
    private int xGridNumber;//
    private int yGridNumber;
    private int gridColor;
    private int backGroundColor;
    private int marginBackGroundColor;
    private double xPixelsPerUnit;
    private double yPixelsPerUnit;


    public DataChart() {
        ini();
    }
    public  void  drawAll(Canvas canvas,List<float[]> data,int[] dataColors){
        canvas.drawRGB(15, 15, 15);//清空屏幕
        drawAxis(canvas,getxAxisColor(),getMargin());
        drawGrid(canvas,getGridColor(),getMargin(),getxMax(),getxMin(),getyMax(),getyMin(),getxGridNumber(),getyGridNumber());
        drawXYText(canvas,getTextColor(),getMargin(),getxLabel(),getyLabel());
        drawPath(canvas,data,true,dataColors,getMargin());
    }
    private void ini(){
        setxMax(8192);
        setxMin(0);
        setyMax(70000);
        setyMin(0);
        setxAxisColor(Color.WHITE);
        setyAxisColor(Color.WHITE);
        setScale(1);
        setMargin(new int[]{40,40,20,20});
        setxLabel("x");
        setyLabel("y");
        setTextColor(Color.WHITE);
        setxGridNumber(20);
        setyGridNumber(20);
        setGridColor(Color.argb(255,83,83,83));
        setBackGroundColor(Color.argb(255,15,15,15));
        setMarginBackGroundColor(Color.argb(255,15,15,15));

    }

    private float[] adapterArray(float []needAdapter,int[] marigins,double xMin,double yMin){
        float []afterAdapter=new float[needAdapter.length*2];
        int mm=0;
        for (int i=0;i<needAdapter.length;i++){
            afterAdapter[mm]=(float) (getxPixelsPerUnit()* (i - xMin));
           // Log.e("needx",Float.valueOf((float) (marigins[0] + getxPixelsPerUnit()* (i - xMin))).toString());
            afterAdapter[mm+1]=(float) (getyPixelsPerUnit()* (needAdapter[i] - yMin));
            //Log.e("needx",Float.valueOf((float) (marigins[3] -getyPixelsPerUnit()* (needAdapter[i] - yMin))).toString());
            mm=mm+2;
        }
        return afterAdapter;
    }
    private  float[] calculateDrawPoints(float p1x, float p1y, float p2x, float p2y) {//调用次数最多60%
        float drawP1x;
        float drawP1y;
        float drawP2x;
        float drawP2y;
//        if (p1y > screenHeight) {
//            // Intersection with the top of the screen
//            float m = (p2y - p1y) / (p2x - p1x);
//            drawP1x = (screenHeight - p1y + m * p1x) / m;
//            drawP1y = screenHeight;
//
//            if (drawP1x < 0) {
//                // If Intersection is left of the screen we calculate the intersection
//                // with the left border
//                drawP1x = 0;
//                drawP1y = p1y - m * p1x;
//            } else if (drawP1x > screenWidth) {
//                // If Intersection is right of the screen we calculate the intersection
//                // with the right border
//                drawP1x = screenWidth;
//                drawP1y = m * screenWidth + p1y - m * p1x;
//            }
//        } else if (p1y < 0) {
//            float m = (p2y - p1y) / (p2x - p1x);
//            drawP1x = (-p1y + m * p1x) / m;
//            drawP1y = 0;
//            if (drawP1x < 0) {
//                drawP1x = 0;
//                drawP1y = p1y - m * p1x;
//            } else if (drawP1x > screenWidth) {
//                drawP1x = screenWidth;
//                drawP1y = m * screenWidth + p1y - m * p1x;
//            }
//        } else {
//            // If the point is in the screen use it
//            drawP1x = p1x;
//            drawP1y = p1y;
//        }
//
//        if (p2y > screenHeight) {
//            float m = (p2y - p1y) / (p2x - p1x);
//            drawP2x = (screenHeight - p1y + m * p1x) / m;
//            drawP2y = screenHeight;
//            if (drawP2x < 0) {
//                drawP2x = 0;
//                drawP2y = p1y - m * p1x;
//            } else if (drawP2x > screenWidth) {
//                drawP2x = screenWidth;
//                drawP2y = m * screenWidth + p1y - m * p1x;
//            }
//        } else if (p2y < 0) {
//            float m = (p2y - p1y) / (p2x - p1x);
//            drawP2x = (-p1y + m * p1x) / m;
//            drawP2y = 0;
//            if (drawP2x < 0) {
//                drawP2x = 0;
//                drawP2y = p1y - m * p1x;
//            } else if (drawP2x > screenWidth) {
//                drawP2x = screenWidth;
//                drawP2y = m * screenWidth + p1y - m * p1x;
//            }
//        } else {
//            // If the point is in the screen use it
//            drawP2x = p2x;
//            drawP2y = p2y;
//        }

        // return new float[] { drawP1x, drawP1y, drawP2x, drawP2y };
        return new float[]{p1x,p1y,p2x,p2y};
    }

    protected void drawLengend(Canvas canvas, float[] points, Paint paint, boolean circular) {

    }
    protected void drawXYText(Canvas canvas,int textColor,int[] margins,String xLabelText,String yLabelText) {
        Paint xyLabel=new Paint();
        xyLabel.setStrokeWidth(1);
        xyLabel.setColor(textColor);//字的颜色和xy轴的颜色一致
        canvas.drawText(xLabelText, (canvas.getWidth()-margins[0]-margins[1])/2+margins[0], canvas.getHeight()-margins[2],xyLabel);
        canvas.drawText(yLabelText, margins[0], (canvas.getHeight()-margins[2]-margins[3])/2+margins[3],xyLabel);
    }
    /**
     * 绘制横纵坐标轴
     */
    protected void drawAxis(Canvas canvas,int axisColor,int[] margins) {
        Paint xyAxis=new Paint();
        xyAxis.setColor(axisColor);
        xyAxis.setStrokeWidth(1);
        //绘制纵坐标
        canvas.drawLine(margins[0], margins[3], margins[0], canvas.getHeight() - margins[2], xyAxis);
        //绘制横坐标
        canvas.drawLine(margins[0], canvas.getHeight() - margins[2], canvas.getWidth() - margins[1], canvas.getHeight() - margins[2], xyAxis);//绘制坐标轴
    }
    protected void drawGrid(Canvas canvas,int gridColor,int[] margins,double xMax,double xMin,double yMax,double yMin,int xGridNumber,int yGridNumber) {
        Paint grid=new Paint();
        grid.setColor(gridColor);
        grid.setStrokeWidth(1);
        //绘制纵Grid
        float yGridPan=(canvas.getWidth()-margins[0]-margins[1])/yGridNumber;
        for (int i=0;i<yGridNumber;i++){
            canvas.drawLine(margins[0]+i*yGridPan,margins[3],margins[0]+i*yGridPan,canvas.getHeight()-margins[2],grid);
            canvas.drawText(Integer.valueOf((int) (i*((xMax-xMin)/(xGridNumber-1)))).toString(),margins[0]+i*yGridPan,canvas.getHeight()-margins[2],grid);
        }
        //绘制横Grid
        float xGridPan=(canvas.getHeight()-margins[2]-margins[3])/xGridNumber;
        for (int j=0;j<xGridNumber;j++){
            canvas.drawLine(margins[0],margins[3]+j*xGridPan,canvas.getWidth()-margins[1],margins[3]+j*xGridPan,grid);
            canvas.drawText(Double.valueOf(j*((yMax-yMin)/(yGridNumber-1))).toString(),margins[0],margins[3]+j*xGridPan,grid);
        }


    }
    protected void drawPath(Canvas canvas, List<float[]> data, boolean circular,int[] dataColors,int []margins) {

        if (data.size()<1){
            return;
        }

        setxPixelsPerUnit(canvas,margins);
        setyPixelsPerUnit(canvas,margins);
        canvas.translate(margins[0],canvas.getHeight()-margins[2]);
        for (int datalength=0;datalength<data.size();datalength++){
            Path path = new Path();

            Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(dataColors[datalength]);
            paint.setStrokeWidth(1);
            float[]points=adapterArray(data.get(datalength),margins,getxMin(),getyMin());
            float[] tempDrawPoints;
            if (points.length < 4) {
                return;
            }
            tempDrawPoints = calculateDrawPoints(points[0], points[1], points[2], points[3]);
            path.moveTo(tempDrawPoints[0], tempDrawPoints[1]);
            path.lineTo(tempDrawPoints[2], tempDrawPoints[3]);
//
            int length = points.length;
            for (int i = 4; i < length; i += 2) {
//            if ((points[i - 1] < 0 && points[i + 1] < 0)
//                    || (points[i - 1] > height && points[i + 1] > height)) {
//                continue;
//            }
//                tempDrawPoints = calculateDrawPoints(points[i - 2], points[i - 1], points[i], points[i + 1],
//                        height, width);
                if (!circular) {
                    path.moveTo(points[i - 2], points[i - 1]);
                }
                path.lineTo(points[i], points[i + 1]);
              //  Log.e("x"+Integer.valueOf(i).toString()+"= ",Float.valueOf(points[i]).toString());
                //Log.e("y="+Integer.valueOf(i).toString()+"= ",Float.valueOf(points[i+1]).toString());
               // Log.e("datalength",Integer.valueOf(i).toString());
            }
            if (circular) {
                path.lineTo(points[0], points[1]);
              //
            }

            canvas.drawPath(path, paint);
            //

        }
    }

    public double getxMax() {
        return xMax;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public double getxMin() {
        return xMin;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    public double getyMin() {
        return yMin;
    }

    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    public int getxAxisColor() {
        return xAxisColor;
    }

    public void setxAxisColor(int xAxisColor) {
        this.xAxisColor = xAxisColor;
    }

    public int getyAxisColor() {
        return yAxisColor;
    }

    public void setyAxisColor(int yAxisColor) {
        this.yAxisColor = yAxisColor;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int[] getMargin() {
        return margin;
    }

    public void setMargin(int[] margin) {
        this.margin = margin;
    }

    public String getxLabel() {
        return xLabel;
    }

    public void setxLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    public String getyLabel() {
        return yLabel;
    }

    public void setyLabel(String yLabel) {
        this.yLabel = yLabel;
    }

    public int getxGridNumber() {
        return xGridNumber;
    }

    public void setxGridNumber(int xGridNumber) {
        this.xGridNumber = xGridNumber;
    }

    public int getyGridNumber() {
        return yGridNumber;
    }

    public void setyGridNumber(int yGridNumber) {
        this.yGridNumber = yGridNumber;
    }

    public int getGridColor() {
        return gridColor;
    }

    public void setGridColor(int gridColor) {
        this.gridColor = gridColor;
    }

    public int getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public int getMarginBackGroundColor() {
        return marginBackGroundColor;
    }

    public void setMarginBackGroundColor(int marginBackGroundColor) {
        this.marginBackGroundColor = marginBackGroundColor;
    }

    private double getxPixelsPerUnit() {
        return xPixelsPerUnit;
    }

    private void setxPixelsPerUnit(Canvas canvas,int[]marigins) {
        xPixelsPerUnit= (canvas.getWidth() - marigins[0]-marigins[1]) / (xMax - xMin);//maxX为横坐标的最大值，minX为横坐标最小值

    }

    private double getyPixelsPerUnit() {
        return yPixelsPerUnit;
    }

    private void setyPixelsPerUnit(Canvas canvas,int[]marigins) {
         yPixelsPerUnit= (float) ((canvas.getHeight() - marigins[2]-marigins[3]) / (yMax - yMin));

    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
