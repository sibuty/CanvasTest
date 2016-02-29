package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.View;

import java.util.List;

/**
 * Created by glotemz on 18.02.16.
 */
public abstract class AbstractShape {

    protected boolean selected = false;

    public abstract void draw(Canvas canvas);

    public abstract PointF getShapePoint(int index);

    /** Used for resizing */
    public abstract void setShapePoint(int index, PointF value);

    public abstract int getHandlersCount();

    public void reset() {}

    /**
     * True if xC, yC are on the shape
     *
     * @param r finger radius to add into touchable area near the shape
     */
    public abstract boolean onShape(float xC, float yC, float r);

    public abstract boolean canMove();

    public abstract void enableMove(boolean enable);

    public abstract List<View> getHandlers();

    public abstract void enableSelect(boolean enable);
}
