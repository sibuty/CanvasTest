package com.example.igor.canvastest;

import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

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
    protected PointF start;
    protected PointF end;
    protected PointF left;
    protected PointF right;

    public AbstractRectangleShape(PointF start, PointF end) {
        super();
        this.rect = new RectF();
        this.start = start;
        this.end = end;
        setRectBounds();
        this.right = new PointF(end.x, start.y);
        this.left = new PointF(start.x, end.y);
    }

    @Override
    public int getHandlersCount() {
        return 4;
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
            updateHandlersPlaces();
        }
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
