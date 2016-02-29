package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Sergey Prokofev
 * on 29.02.16
 * sergey.prokofev@altarix.ru
 * skype masterw0rks
 */
public class TextShape extends AbstractShape {

    private PointF base;
    private String text = "kajfasdfa";
    private Paint bgRectPaint;

    public TextShape(PointF base) {
        super();
        bgRectPaint = new Paint();
        this.base = base;
    }

    @Override
    public void initPaint() {
        paint.setColor(Color.rgb(200, 100, 100));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        //todo add screen density deps
        paint.setTextSize(40);

//        bgRectPaint.setColor(Color.WHITE);
//        bgRectPaint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
    }

    @Override
    public void reset() {
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, base.x, base.y, paint);

        float textWidth = paint.measureText(text);

    }

    @Override
    public PointF getShapePoint(int index) {
        return null;
    }

    @Override
    public void setShapePoint(int index, PointF value) {

    }

    @Override
    public int getHandlersCount() {
        return 0;
    }

    @Override
    public boolean onShape(float xC, float yC, float r) {
        return false;
    }
}
