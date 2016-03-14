package com.example.igor.canvastest;

import android.content.Context;
import android.graphics.*;

/**
 * Created by Igor on 29.02.2016.
 */
public class OvalShape extends AbstractRectangleShape {

    public OvalShape(Context context, PointF start, PointF end) {
        super(context, start, end);
    }

    @Override
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
        if(this.selected) {
            paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
            paint.setColor(Color.rgb(155, 155, 155));
            paint.setStrokeWidth(5);
            canvas.drawRect(rect, paint);
        }
        paint.setStrokeWidth(10);
        paint.setColor(Color.rgb(255, 0, 0));
        paint.setPathEffect(null);
        canvas.drawOval(rect, paint);
    }
}
