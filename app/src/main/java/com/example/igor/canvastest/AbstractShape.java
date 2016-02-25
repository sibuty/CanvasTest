package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.View;

import java.util.List;

/**
 * Created by glotemz on 18.02.16.
 */
public abstract class AbstractShape {
    // TODO: 25.02.16 consider if poly shapes needed make final and assign=2 otherwise
    protected int pointsCount;

    public abstract void draw(Canvas canvas);

    public abstract PointF getShapePoint(int index);

    public abstract void setShapePoint(int index, PointF value);

    public abstract int getShapePointsCount();

    public void reset() {};

    /** True if specified coords are on the shape */
    public abstract boolean onShape(float xC, float yC, float r);

    public abstract boolean canMove();

    public abstract void enableMove(boolean enable);

    public abstract List<View> getHandlers();

    public abstract void enableSelect(boolean enable);
}
