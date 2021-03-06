package com.example.igor.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by serge on 25.02.16.
 */
public class RectangleShape extends AbstractRectangleShape {

    public RectangleShape(Context context, PointF start, PointF end) {
        super(context, start, end);
    }

    @Override
    public void initPaint() {
        paint.setColor(Color.rgb(0, 0, 255));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawRect(rect, paint);
    }
}
