package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by serge on 25.02.16.
 */
public class RectangleShape extends AbstractShape {

    private ArrayList<View> handlers = new ArrayList<>();
    private Paint paint = new Paint();
    private ArrayList<PointF[]> undoPoints = new ArrayList<PointF[]>();
    private boolean canMove = false;
    private PointF top;
    private PointF end;
    private int strokeWidth;

    public RectangleShape(PointF start, PointF end) {
        this.top = start;
        this.end = end;
        initPaint();
    }

    public void initPaint() {
        paint.setColor(Color.rgb(255, 0, 0));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        strokeWidth = 10;
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawRect(end.x, end.y, top.x, top.y, paint);
    }

    @Override
    public PointF getShapePoint(final int index) {
        switch (index) {
            case 0:
                return top;
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
                top = value;
                break;
            case 1:
                end = value;
                break;
        }
    }

    @Override
    public int getHandlersCount() {
        return 4;
    }

    @Override
    public boolean onShape(float xC, float yC, float r) {
        //Игорь пиши комментарии, будь мужиком блеать
        //Акей))00
        //        PointF top = points[0];
        //        PointF end = points[1];
        //        float x0 = top.x;
        //        float y0 = top.y;
        //        float x = end.x;
        //        float y = end.y;
        //
        //        final float _x1 = x0 - xC;
        //        final float _x2 = x - xC;
        //        final float _y1 = y0 - yC;
        //        final float _y2 = y - yC;
        //
        //        final float dx = _x2 - _x1;
        //        final float dy = _y2 - _y1;
        //
        //        final float a = dx * dx + dy * dy;
        //        final float b = 2.0F * (_x1 * dx + _y1 * dy);
        //        final float c = _x1 * _x1 + _y1 * _y1 - (float) Math.pow((double) r, 2.0);
        //
        //        if (-b < 0.0F) {
        //            return c < 0.0F;
        //        }
        //        if (-b < 2.0F * a) {
        //            return 4.0F * a * c - b * b < 0.0F;
        //        }
        //
        //        return a + b + c < 0.0F;

        /* Coords are on the shape if they are inside drawn path within stroke width */
        boolean inBoundsTopLine = top.x - strokeWidth >= xC && xC <= top.x + strokeWidth;
        boolean inBoundsLeftLine = top.y - strokeWidth >= yC && yC <= top.y + strokeWidth;
        boolean inBoundsBottomLine = end.x - strokeWidth >= xC && xC <= end.x + strokeWidth;
        boolean inBoundsRightLine = end.y - strokeWidth >= yC && yC <= end.y + strokeWidth;

        return inBoundsBottomLine || inBoundsLeftLine || inBoundsRightLine || inBoundsTopLine;
    }

    @Override
    public List<View> getHandlers() {
        return handlers;
    }

    @Override
    public void enableSelect(final boolean enable) {
        for (View view : handlers) {
            view.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
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
