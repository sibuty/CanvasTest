package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by glotemz on 18.02.16.
 */
public abstract class AbstractShape {

    protected boolean selected = false;
    protected Paint paint = new Paint();
    protected ArrayList<PointF[]> undoPoints = new ArrayList<PointF[]>();
    protected ArrayList<View> handlers = new ArrayList<>();

    public boolean canMove = false;

    public void enableSelect(final boolean enable) {
        this.selected = enable;
        for (View view : handlers) {
            view.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    public void reset() {}

    public abstract void draw(Canvas canvas);

    public abstract PointF getShapePoint(int index);

    /** Used for resizing */
    public abstract void setShapePoint(int index, PointF value);

    public abstract int getHandlersCount();

    /**
     * True if xC, yC are on the shape
     *
     * @param r finger radius to add into touchable area near the shape
     */
    public abstract boolean onShape(float xC, float yC, float r);
}
