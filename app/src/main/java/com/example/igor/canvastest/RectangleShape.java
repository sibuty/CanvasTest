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
    private PointF start;
    private PointF end;

    public RectangleShape(PointF start, PointF end) {
        this.start = start;
        this.end = end;
        initPaint();
    }

    public void initPaint() {
        paint.setColor(Color.rgb(255, 0, 0));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawRect(start.x, start.y, end.x, end.y, paint);
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
                start = value;
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
        /* Coords are on the shape if they are inside drawn rectangle within finger raduis */
        boolean inBoundsX = (start.x - r) <= xC && xC <= (end.x + r);
        boolean inBoundsY = (start.y - r) <= yC && yC <= (end.y + r);

        return inBoundsX && inBoundsY;
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
