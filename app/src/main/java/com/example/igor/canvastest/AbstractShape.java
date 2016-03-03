package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by glotemz on 18.02.16.
 */
public abstract class AbstractShape {

    protected boolean selected = false;

    public boolean canMove = false;
    public Paint paint = new Paint();
    public ArrayList<View> handlers = new ArrayList<>();

    public AbstractShape() {
        initPaint();
    }

    public void enableSelect(final boolean enable) {
        this.selected = enable;
        for (View view : handlers) {
            view.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Must not be implemented
     *
     * Why?
     */
    protected abstract void updateHandlersPlaces();

    public abstract void reset();

    public abstract void draw(Canvas canvas);

    @Nullable
    public abstract PointF getShapePoint(int index);

    /** Used for resizing */
    public abstract void setShapePoint(int index, PointF value);

    public abstract int getHandlersCount();

    /**
     * True if xC, yC are on the shape. Used to determine whether shape is being touched to move.
     *
     * @param r finger radius to add into touchable area near the shape
     */
    public abstract boolean onShape(float xC, float yC, float r);

    protected abstract void initPaint();

    public abstract ShapeSnapshot makeSnapshot();

    public abstract void restoreFromSnapshot(ShapeSnapshot shapeSnapshot);
}
