package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Igor on 29.02.2016.
 */
public class OvalShape extends RectangleShape {

    public OvalShape(PointF start, PointF end) {
        super(start, end);
    }

    public void initPaint() {
        paint.setColor(Color.rgb(255, 0, 0));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawOval(rect, paint);
    }

    @Override
    public int getHandlersCount() {
        return 4;
    }
}
