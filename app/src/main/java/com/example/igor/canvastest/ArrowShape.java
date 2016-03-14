package com.example.igor.canvastest;

import android.content.Context;
import android.graphics.*;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glotemz on 18.02.16.
 */
public class ArrowShape extends AbstractShape {

    private static final double ANGLE_OFFSET = Math.toRadians(45.0);
    private PointF start;
    private PointF end;
    private Path arrowPath = new Path();
    private Path barbsPath = new Path();

    public ArrowShape(Context context, PointF start, PointF end) {
        super(context);
        this.start = start;
        this.end = end;
        this.shapePoints.add(start);
        this.shapePoints.add(end);
        this.handlePoints.addAll(shapePoints);
        initHandles();
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
    protected void onTransform(int index) {
        this.start = handlePoints.get(0);
        this.end = handlePoints.get(1);
        updateHandlePlaces();
    }

    @Override
    public ShapeSnapshot makeSnapshot() {
        return new ArrowShapeSnapShot(new Paint(this.paint), new ArrayList<PointF>(handlePoints));
    }

    @Override
    public void restoreFromSnapshot(final ShapeSnapshot shapeSnapshot) {
        if (shapeSnapshot instanceof ArrowShapeSnapShot) {
            ArrowShapeSnapShot rectangleShapeSnapShot = (ArrowShapeSnapShot) shapeSnapshot;
            this.paint = rectangleShapeSnapShot.paint;
            this.handlePoints = rectangleShapeSnapShot.handlePoints;
        }
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
    public int getHandlesCount() {
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
        for (View view : handles) {
            view.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    public static class ArrowShapeSnapShot extends ShapeSnapshot {

        public ArrowShapeSnapShot(final Paint paint, final List<PointF> handlePoints) {
            super(paint, handlePoints);
        }
    }
}
