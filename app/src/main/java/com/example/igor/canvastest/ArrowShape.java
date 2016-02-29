package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;

/**
 * Created by glotemz on 18.02.16.
 */
public class ArrowShape extends AbstractShape {

    private static final double ANGLE_OFFSET = Math.toRadians(45.0);
    private PointF start;
    private PointF end;
    private Path arrowPath = new Path();
    private Path barbsPath = new Path();

    public ArrowShape(PointF start, PointF end) {
        super();
        this.start = start;
        this.end = end;
    }

    @Override
    public void initPaint() {
        paint.setColor(Color.rgb(0, 255, 0));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
    }

    @Override
    public void draw(final Canvas canvas) {
        reset();

        float x0 = start.x;
        float y0 = start.y;
        float x = end.x;
        float y = end.y;

        float size = (float)
                Math.sqrt(Math.pow((double) (x - x0), 2.0) + Math.pow((double) (y - y0), 2.0));
        float h = size * 0.2f;

        double angle = Math.PI - Math.atan2((double) (x0 - x), (double) (y - y0));

        float arrowLeftX = (float) (Math.sin(angle - ANGLE_OFFSET) * h) + x;
        float arrowLeftY = (float) (Math.cos(angle - ANGLE_OFFSET) * h) + y;

        float arrowRightX = (float) (Math.sin(angle + ANGLE_OFFSET) * h) + x;
        float arrowRightY = (float) (Math.cos(angle + ANGLE_OFFSET) * h) + y;

        arrowPath.moveTo(x0, y0);
        arrowPath.lineTo(x, y);
        barbsPath.moveTo(arrowLeftX, arrowLeftY);
        barbsPath.lineTo(x, y);
        barbsPath.lineTo(arrowRightX, arrowRightY);
        arrowPath.addPath(barbsPath);

        canvas.drawPath(arrowPath, paint);
    }

    @Override
    public PointF getShapePoint(final int index) {
        switch (index) {
            case 0:
                return start;
            case 1:
                return end;
            default:
                return null;
        }
    }

    @Override
    public void setShapePoint(final int index, final PointF value) {
        switch (index) {
            case 0:
                this.start = value;
                break;
            case 1:
                this.end = value;
                break;
        }
    }

    @Override
    public int getHandlersCount() {
        return 2;
    }

    @Override
    public void reset() {
        arrowPath.reset();
        barbsPath.reset();
    }

    @Override
    public boolean onShape(float xC, float yC, float r) {
        float x0 = this.start.x;
        float y0 = this.start.y;
        float x = this.end.x;
        float y = this.end.y;

        final float _x1 = x0 - xC;
        final float _x2 = x - xC;
        final float _y1 = y0 - yC;
        final float _y2 = y - yC;

        final float dx = _x2 - _x1;
        final float dy = _y2 - _y1;

        final float a = dx * dx + dy * dy;
        final float b = 2.0F * (_x1 * dx + _y1 * dy);
        final float c = _x1 * _x1 + _y1 * _y1 - (float) Math.pow((double) r, 2.0);

        if (-b < 0.0F) {
            return c < 0.0F;
        }
        if (-b < 2.0F * a) {
            return 4.0F * a * c - b * b < 0.0F;
        }

        return a + b + c < 0.0F;
    }

    @Override
    public void enableSelect(final boolean enable) {
        this.selected = enable;
        for (View view : handlers) {
            view.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }
}
