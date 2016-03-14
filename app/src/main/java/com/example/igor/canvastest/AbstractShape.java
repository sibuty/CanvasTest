package com.example.igor.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glotemz on 18.02.16.
 */
public abstract class AbstractShape {

    public interface MoveListener {
        void onMove(PointF delta);
    }

    /** Fired when shape is changed (move, transform, etc.). Designed to redraw canvas. */
    public interface DrawCompleteListener {
        void onDrawComplete();
    }

    /*
    Point shortcuts

    ST__RI
    |    |
    |    |
    LE__EN
     */
    public final int START = 0;
    public final int END = 1;
    public final int LEFT = 2;
    public final int RIGHT = 3;

    protected boolean selected = false;
    /** Points for drawing on canvas */
    protected List<PointF> shapePoints = new ArrayList<>();
    /** Points for placing handles */
    protected List<PointF> handlePoints = new ArrayList<>();
    /** Bounds of shape including all linked objects (handles, recycle bin, etc.). */
    protected RectF bounds = new RectF();
    protected Context context;

    public boolean canMove = false;
    public Paint paint = new Paint();
    public List<View> handles = new ArrayList<>();
    public DrawCompleteListener drawCompleteListener;
    public ShapeSnapshotListener shapeSnapshotListener;
    public MoveListener moveListener;

    public AbstractShape(Context context) {
        this.context = context;
        initPaint();
    }

    protected void initHandles() {
        int count = getHandlesCount();
        for (int i = 0; i < count; i++) {
            PointF point = getHandlePoint(i);
            if (point != null) {
                ToolHandleView toolHandleView = new ToolHandleView(this.context);
                toolHandleView.setX(point.x);
                toolHandleView.setY(point.y);
                final int finalI = i;
                toolHandleView.setPositionListener(new PositionListener() {
                    @Override
                    public void onPositionChanged(PointF pointF) {
                        AbstractShape.this.setHandlePoint(finalI, pointF);
                        drawCompleteListener.onDrawComplete();
                    }
                });
                toolHandleView.setShapeSnapshotListener(shapeSnapshotListener);
                handles.add(toolHandleView);
            }
        }
    }

    protected void updateHandlePlaces() {
        for (int i = 0; i < handles.size(); i++) {
            View handle = handles.get(i);
            if (handle instanceof ToolHandleView) {
                ToolHandleView toolHandleView = (ToolHandleView) handle;
                toolHandleView.setPlace(handlePoints.get(i));
            }
        }
    }

    protected void calculateBounds(PointF move) {
        float minX = shapePoints.get(START).x;
        float minY = shapePoints.get(START).y;
        float maxX = shapePoints.get(END).x;
        float maxY = shapePoints.get(END).y;

        float margin = handles.size() > 0 ? (float) (handles.get(0).getWidth() / 2) : 10;

        for (PointF handlePoint : handlePoints) {
            float x = handlePoint.x;
            float y = handlePoint.y;
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }

        bounds.left = minX + move.x - margin;
        bounds.right = maxX + move.x + margin + 1F;
        bounds.top = minY + move.y - margin;
        bounds.bottom = maxY + move.y + margin + 1F;
    }

    /**
     * Calculates delta of move or transform???? coordinates according to shape bounds
     *
     * @param move coordinates from finger move
     * @return coordinates for move limited by canvas bounds - shape and its controls can't
     * move out from screen
     */
    protected PointF calculateDelta(PointF move, PointF canvasBounds) {
        calculateBounds(move);
        float minX = bounds.left;
        float minY = bounds.top;
        float maxX = bounds.right;
        float maxY = bounds.bottom;

        float dx;

        if (minX < 0.0F) {
            dx = move.x - minX;
        } else if (maxX > canvasBounds.x) {
            dx = canvasBounds.x - maxX + move.x;
        } else {
            dx = move.x;
        }

        float dy;

        if (minY < 0.0F) {
            dy = move.y - minY;
        } else if (maxY > canvasBounds.y) {
            dy = canvasBounds.y - maxY + move.y;
        } else {
            dy = move.y;
        }

        return new PointF(dx, dy);
    }

    public void enableSelect(final boolean enable) {
        this.selected = enable;
        for (View view : handles) {
            view.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    @Nullable
    public PointF getHandlePoint(final int index) {
        if(index >= 0 || index < handlePoints.size()) {
            return handlePoints.get(index);
        } else {
            return null;
        }
    }

    /** Used for resizing */
    public void setHandlePoint(final int index, final PointF value) {
        if(index >= 0 || index < handlePoints.size()) {
            handlePoints.set(index, value);
            onTransform(index);
        }
    }

    /**
     * Moving entire shape with all linked objects inside parent view
     * <p>
     * Should be called by event in canvas layout
     *
     * @param move coordinate difference to move shape to
     */
    public void move(PointF move) {
        if (moveListener != null) {
            moveListener.onMove(move);
        }
    }

    protected abstract void initPaint();

    /**
     * May not be implemented
     * <p>
     * Be case handles was not be resize any handles
     * @param index index point in handlePoints
     */
    protected abstract void onTransform(int index);

    public abstract void reset();

    public abstract void draw(Canvas canvas);

    public abstract int getHandlesCount();

    /**
     * True if xC, yC are on the shape. Used to determine whether shape is being touched to move.
     *
     * @param r finger radius to add into touchable area near the shape
     */
    public abstract boolean onShape(float xC, float yC, float r);

    public abstract ShapeSnapshot makeSnapshot();

    public abstract void restoreFromSnapshot(ShapeSnapshot shapeSnapshot);
}
