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

    protected boolean selected = false;
    /** Points for drawing on canvas */
    protected List<PointF> shapePoints = new ArrayList<>();
    /** Points for placing handles */
    protected List<PointF> handlePoints = new ArrayList<>();
    /** Bounds of shape including all linked objects (handles, recycle bin, etc.). */
    protected RectF bounds;
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
        initHandles();
    }

    public AbstractShape(Context context,
                         DrawCompleteListener drawCompleteListener,
                         ShapeSnapshotListener shapeSnapshotListener) {
        this.context = context;
        this.drawCompleteListener = drawCompleteListener;
        this.shapeSnapshotListener = shapeSnapshotListener;
        initPaint();
        initHandles();
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
            onTransform();
        }
    }

    protected abstract void initPaint();

    /**
     * Must not be implemented
     * <p>
     * todo Why?
     */
    protected abstract void onTransform();

    protected abstract void calculateBounds();

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

    /**
     * Calculates delta of move or transform???? coordinates according to shape bounds
     *
     * @param move coordinates from finger move
     * @return coordinates for move limited by canvas bounds - shape and its controls can't
     * move out from screen
     */
    protected PointF calculateDelta(PointF move, PointF canvasBounds) {
        float minX = bounds.left;
        float minY = bounds.top;
        float maxX = bounds.right;
        float maxY = bounds.bottom;
        /*float minX = handles.get(0).getX();
        float minY = handles.get(0).getY();
        float maxX = handles.get(0).getX();
        float maxY = handles.get(0).getY();

        for (View view : handles) {
            float x = view.getX();
            float y = view.getY();
            float widht = (float) view.getWidth();
            float height = (float) view.getHeight();
            if (x < minX) {
                minX = x;
            }
            if (x + widht > maxX) {
                maxX = x + widht;
            }
            if (y < minY) {
                minY = y;
            }
            if (y + height > maxY) {
                maxY = y + height;
            }
        }

        minX = minX + delta.x;
        maxX = maxX + delta.x;
        minY = minY + delta.y;
        maxY = maxY + delta.y;*/

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
}
