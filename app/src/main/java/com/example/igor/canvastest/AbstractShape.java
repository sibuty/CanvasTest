package com.example.igor.canvastest;

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

    protected boolean selected = false;
    /** Points for drawing on canvas */
    protected List<PointF> shapePoints = new ArrayList<>();
    /** Points for placing handlers */
    protected List<PointF> handlerPoints = new ArrayList<>();
    /** Bounds of shape including all linked objects (handlers, recycle bin, etc.) */
    protected RectF bounds;

    public boolean canMove = false;
    public Paint paint = new Paint();
    public List<View> handlers = new ArrayList<>();
    public MoveListener moveListener;

    public AbstractShape() {
        initPaint();
    }

    protected abstract void initPaint();

    public void enableSelect(final boolean enable) {
        this.selected = enable;
        for (View view : handlers) {
            view.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Must not be implemented
     * <p>
     * todo Why?
     */
    protected abstract void updateHandlesPlaces();

    protected abstract void calculateBounds();

    public abstract void reset();

    public abstract void draw(Canvas canvas);

    @Nullable
    public abstract PointF getHandlePoint(int index);

    /** Used for resizing */
    public abstract void setHandlePoint(int index, PointF value);

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
     * Transform shape dragging by handle
     *
     * @param transform coodrinate difference
     */
    public void transform(ToolHandleView handle, PointF transform) {
        
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
        /*float minX = handlers.get(0).getX();
        float minY = handlers.get(0).getY();
        float maxX = handlers.get(0).getX();
        float maxY = handlers.get(0).getY();

        for (View view : handlers) {
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
