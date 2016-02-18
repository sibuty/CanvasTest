package com.example.igor.canvastest;

import android.graphics.*;

import java.util.ArrayList;

/**
 * Created by glotemz on 18.02.16.
 */
public class ArrowShape implements Shape {

    private static final int COUNT_POINTS = 2;
    private static final double ANGLE_OFFSET = Math.toRadians(45.0);
    private Paint paint = new Paint();
    private Path arrowPath = new Path();
    private Path barbsPath = new Path();
    private ArrayList<PointF[]> undoPoints = new ArrayList<PointF[]>();

    private boolean canMove = false;

    private PointF[] points;

    public ArrowShape() {
        this.points = new PointF[COUNT_POINTS];
        initPaint();
    }

    public ArrowShape(PointF start, PointF end) {
        this.points = new PointF[COUNT_POINTS];
        points[0] = start;
        points[1] = end;
        initPaint();
    }

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
        PointF start = points[0];
        PointF end = points[1];
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
        return index >= 0 && index < COUNT_POINTS ? points[index] : null;
    }

    @Override
    public void setShapePoint(final int index, final PointF value) {
        if (index >= 0 && index < COUNT_POINTS) {
            points[index] = value;
        }
    }

    @Override
    public PointF[] getShapePoints() {
        return points;
    }

    @Override
    public int getShapePointsCount() {
        return points != null ? points.length : 0;
    }

    @Override
    public void setPaint(final Paint paint) {
        this.paint = paint;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public void reset() {
        arrowPath.reset();
        barbsPath.reset();
    }

    @Override
    public boolean checkCircle(float xC, float yC) {
        PointF start = points[0];
        PointF end = points[1];
        float x0 = start.x;
        float y0 = start.y;
        float x = end.x;
        float y = end.y;

        final float _x1 = x0 - xC;
        final float _x2 = x - xC;
        final float _y1 = y0 - yC;
        final float _y2 = y - yC;

        final float dx = _x2 - _x1;
        final float dy = _y2 - _y1;

        final float a = dx * dx + dy * dy;
        final float b = 2.0F * (_x1 * dx + _y1 * dy);
        final float c = _x1 * _x1 + _y1 * _y1 - 64.0F;

        if (-b < 0.0F) {
            return c < 0.0F;
        }
        if (-b < 2.0F * a) {
            return 4.0F * a * c - b * b < 0.0F;
        }

        return a + b + c < 0.0F;
    }

    @Override
    public boolean canMove() {
        return canMove;
    }

    @Override
    public void enableMove(final boolean enable) {
        this.canMove = enable;
    }
}
