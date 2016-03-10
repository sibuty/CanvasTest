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
    /** One of the shape points which coordinates should be used to translate the shape */
    protected PointF basePoint;

    public boolean canMove = false;
    public Paint paint = new Paint();
    public ArrayList<View> handlers = new ArrayList<>();

    public AbstractShape() {
        initPaint();
    }

    protected abstract void initPaint();

    /**
     * Must not be implemented
     *
     * Why?
     */
    protected abstract void updateHandlersPlaces();

    public void enableSelect(final boolean enable) {
        this.selected = enable;
        for (View view : handlers) {
            view.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

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

    public abstract ShapeSnapshot makeSnapshot();

    public abstract void restoreFromSnapshot(ShapeSnapshot shapeSnapshot);

    /** Should be called by event in canvas layout */
//    public abstract void move();
}
