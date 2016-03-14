package com.example.igor.canvastest;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

import java.util.EnumMap;

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
        setRectBounds();
        this.handlePoints.addAll(shapePoints);
        this.handlePoints.add(new PointF(end.x, start.y));
        this.handlePoints.add(new PointF(start.x, end.y));
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
    protected void onTransform() {
        if (!canMove) {
            for (int i = 0; i < handles.size(); i++) {
                View handle = handles.get(i);
                if (handle instanceof ToolHandleView) {
                    ToolHandleView toolHandleView = (ToolHandleView) handle;
                    toolHandleView.setPlace(handlePoints.get(i));
                }
            }
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
        return new RectangleShapeSnapShot(
                new Paint(paint),
                new PointF(start.x, start.y),
                new PointF(end.x, end.y),
                new PointF(left.x, left.y),
                new PointF(right.x, right.y)
        );
    }

    @Override
    public void restoreFromSnapshot(final ShapeSnapshot shapeSnapshot) {
        if(shapeSnapshot instanceof RectangleShapeSnapShot) {
            RectangleShapeSnapShot rectangleShapeSnapShot = (RectangleShapeSnapShot) shapeSnapshot;
            this.paint = rectangleShapeSnapShot.paint;
            this.start = rectangleShapeSnapShot.start;
            this.end = rectangleShapeSnapShot.end;
            this.left = rectangleShapeSnapShot.left;
            this.right = rectangleShapeSnapShot.right;
            this.canMove = true;
            setRectBounds();
            onTransform();
        }
    }

    @Override
    protected PointF calculateDelta(PointF move, PointF canvasBounds) {
        return super.calculateDelta(move, canvasBounds);
    }

    public static class RectangleShapeSnapShot implements ShapeSnapshot {
        private Paint paint;
        private PointF start;
        private PointF end;
        private PointF left;
        private PointF right;

        public RectangleShapeSnapShot(final Paint paint,
                                      final PointF start,
                                      final PointF end,
                                      final PointF left,
                                      final PointF right) {
            this.paint = paint;
            this.start = start;
            this.end = end;
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof RectangleShapeSnapShot)) {
                return false;
            }

            final RectangleShapeSnapShot that = (RectangleShapeSnapShot) o;

            if (!paint.equals(that.paint)) {
                return false;
            }
            if (!start.equals(that.start)) {
                return false;
            }
            if (!end.equals(that.end)) {
                return false;
            }
            if (!left.equals(that.left)) {
                return false;
            }
            return right.equals(that.right);

        }

        @Override
        public int hashCode() {
            int result = paint.hashCode();
            result = 31 * result + start.hashCode();
            result = 31 * result + end.hashCode();
            result = 31 * result + left.hashCode();
            result = 31 * result + right.hashCode();
            return result;
        }
    }
}
