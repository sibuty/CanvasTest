package com.example.igor.canvastest;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Shape based on 2 points start and end
 * <p/>
 * Created by Sergey Prokofev
 * on 29.02.16
 * sergey.prokofev@altarix.ru
 * skype masterw0rks
 */
public abstract class AbstractRectangleShape extends AbstractShape {
    protected RectF rect;

    public AbstractRectangleShape(Context context, PointF start, PointF end) {
        super(context);
        this.rect = new RectF();
        this.shapePoints.add(start);
        this.shapePoints.add(end);
        this.handlePoints.addAll(shapePoints);
        this.handlePoints.add(new PointF(end.x, start.y));
        this.handlePoints.add(new PointF(start.x, end.y));
        setRectBounds();
        initHandles();
    }

    @Override
    public int getHandlesCount() {
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
    public void reset() {
        // TODO: 29.02.16 implement
    }

    @Override
    protected void onTransform(int index) {
        if (!canMove) {
            PointF start = handlePoints.get(0);
            PointF end = handlePoints.get(1);
            PointF right = handlePoints.get(2);
            PointF left = handlePoints.get(3);
            switch (index) {
                case 0:
                    right.y = start.y;
                    left.x = start.x;
                    break;
                case 1:
                    right.x = end.x;
                    left.y = end.y;
                    break;
                case 2:
                    end.x = right.x;
                    start.y = right.y;
                    break;
                case 3:
                    start.x = left.x;
                    end.y = left.y;
                    break;
            }
            updateHandlePlaces();
        }
        setRectBounds();
    }

    protected void setRectBounds() {
        PointF start = handlePoints.get(0);
        PointF end = handlePoints.get(1);
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

    @Override
    public ShapeSnapshot makeSnapshot() {
        return new RectangleShapeSnapShot(new Paint(paint), new ArrayList<PointF>(handlePoints));
    }

    @Override
    public void restoreFromSnapshot(final ShapeSnapshot shapeSnapshot) {
        if (shapeSnapshot instanceof RectangleShapeSnapShot) {
            RectangleShapeSnapShot rectangleShapeSnapShot = (RectangleShapeSnapShot) shapeSnapshot;
            this.paint = rectangleShapeSnapShot.paint;
            this.handlePoints = rectangleShapeSnapShot.handlePoints;
            onTransform(-1);
        }
    }

    @Override
    protected PointF calculateDelta(PointF move, PointF canvasBounds) {
        return super.calculateDelta(move, canvasBounds);
    }

    public static class RectangleShapeSnapShot extends ShapeSnapshot {

        public RectangleShapeSnapShot(final Paint paint, final ArrayList<PointF> handlePoints) {
            super(paint, handlePoints);
        }
    }
}
