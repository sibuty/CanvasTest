package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
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
    private RectF rect;
    private PointF start;
    private PointF end;
    private PointF left;
    private PointF right;

    public RectangleShape(PointF start, PointF end) {
        this.rect = new RectF();
        this.start = start;
        this.end = end;
        setRectBounds();
        this.right = new PointF(end.x, start.y);
        this.left = new PointF(start.x, end.y);
        initPaint();
    }

    public void initPaint() {
        paint.setColor(Color.rgb(255, 0, 0));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawRect(rect, paint);
    }

    @Override
    public PointF getShapePoint(final int index) {
        switch (index) {
            case 0:
                return start;
            case 1:
                return right;
            case 2:
                return end;
            case 3:
                return left;
            default:
                return null;
        }
    }

    @Override
    public void setShapePoint(final int index, final PointF value) {
        switch (index) {
            case 0:
                start = value;
                right.y = start.y;
                left.x = start.x;
                break;
            case 1:
                if (!canMove) {
                    right = value;
                    end.x = value.x;
                    start.y = value.y;
                }
                break;
            case 2:
                end = value;
                right.x = end.x;
                left.y = end.y;
                break;
            case 3:
                if (!canMove) {
                    left = value;
                    start.x = value.x;
                    end.y = value.y;
                }
                break;
        }
        if (!canMove) {
            updateHandlersPlaces();
        }
        setRectBounds();
    }

    protected void setRectBounds() {
        if (start.x < end.x && start.y < end.y) {
            rect.set(start.x, start.y, end.x, end.y);
        } else if (start.x > end.x && start.y < end.y) {
            rect.set(end.x, start.y, start.x, end.y);
        } else if (start.x < end.x && start.y > end.y) {
            rect.set(start.x, end.y, end.x, start.y);
        } else {
            rect.set(end.x, end.y, start.x, start.y);
        }
    }

    protected void updateHandlersPlaces() {
        for (int i = 0; i < handlers.size(); i++) {
            View handle = handlers.get(i);
            if (handle instanceof ToolHandleView) {
                ToolHandleView toolHandleView = (ToolHandleView) handle;
                switch (i) {
                    case 0:
                        toolHandleView.setPlace(start);
                        break;
                    case 1:
                        toolHandleView.setPlace(right);
                        break;
                    case 2:
                        toolHandleView.setPlace(end);
                        break;
                    case 3:
                        toolHandleView.setPlace(left);
                        break;
                }
            }
        }
    }

    @Override
    public int getHandlersCount() {
        return 4;
    }

    @Override
    public boolean onShape(float xC, float yC, float r) {
        /* Coords are on the shape if they are inside drawn rectangle within finger raduis */
        boolean inBoundsX = (rect.left - r) <= xC && xC <= (rect.right + r);
        boolean inBoundsY = (rect.top - r) <= yC && yC <= (rect.bottom + r);

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
