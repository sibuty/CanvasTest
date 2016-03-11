package com.example.igor.canvastest;

import android.graphics.*;
import android.view.View;

/**
 * Created by glotemz on 18.02.16.
 */
public class ArrowShape extends AbstractShape {

    private static final double ANGLE_OFFSET = Math.toRadians(45.0);
    private PointF start;
    private PointF end;
    private Path arrowPath = new Path();
    private Path barbsPath = new Path();

    public ArrowShape(PointF start, PointF end) {
        super();
        this.start = start;
        this.end = end;
    }

    @Override
    public void initPaint() {
        paint.setColor(Color.rgb(0, 255, 0));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
    }

    @Override
    protected void updateHandlesPlaces() {
        for (int i = 0; i < handlers.size(); i++) {
            View handle = handlers.get(i);
            if (handle instanceof ToolHandleView) {
                ToolHandleView toolHandleView = (ToolHandleView) handle;
                switch (i) {
                    case 0:
                        toolHandleView.setPlace(start);
                        break;
                    case 1:
                        toolHandleView.setPlace(end);
                        break;
                }
            }
        }
    }

    @Override
    public ShapeSnapshot makeSnapshot() {
        return new ArrowShapeSnapShot(
                new Paint(this.paint),
                new PointF(this.start.x, this.start.y),
                new PointF(this.end.x, this.end.y));
    }

    @Override
    public void restoreFromSnapshot(final ShapeSnapshot shapeSnapshot) {
        if (shapeSnapshot instanceof ArrowShapeSnapShot) {
            ArrowShapeSnapShot rectangleShapeSnapShot = (ArrowShapeSnapShot) shapeSnapshot;
            this.paint = rectangleShapeSnapShot.paint;
            this.start = rectangleShapeSnapShot.start;
            this.end = rectangleShapeSnapShot.end;
        }
    }

    @Override
    public void move(PointF move) {

    }

    @Override
    public void draw(final Canvas canvas) {
        reset();

        float x0 = start.x;
        float y0 = start.y;
        float x = end.x;
        float y = end.y;

        float size = (float)
                Math.sqrt(Math.pow((double) (x - x0), 2.0) + Math.pow((double) (y - y0), 2.0));
        float h = size * 0.2f;

        double angle = Math.PI - Math.atan2((double) (x0 - x), (double) (y - y0));

        float arrowLeftX = (float) (Math.sin(angle - ANGLE_OFFSET) * h) + x;
        float arrowLeftY = (float) (Math.cos(angle - ANGLE_OFFSET) * h) + y;

        float arrowRightX = (float) (Math.sin(angle + ANGLE_OFFSET) * h) + x;
        float arrowRightY = (float) (Math.cos(angle + ANGLE_OFFSET) * h) + y;

        arrowPath.moveTo(x0, y0);
        arrowPath.lineTo(x, y);
        barbsPath.moveTo(arrowLeftX, arrowLeftY);
        barbsPath.lineTo(x, y);
        barbsPath.lineTo(arrowRightX, arrowRightY);
        arrowPath.addPath(barbsPath);

        canvas.drawPath(arrowPath, paint);
    }

    @Override
    public PointF getHandlePoint(final int index) {
        switch (index) {
            case 0:
                return start;
            case 1:
                return end;
            default:
                return null;
        }
    }

    @Override
    public void setHandlePoint(final int index, final PointF value) {
        switch (index) {
            case 0:
                this.start = value;
                break;
            case 1:
                this.end = value;
                break;
        }
    }

    @Override
    public int getHandlesCount() {
        return 2;
    }

    @Override
    public void reset() {
        arrowPath.reset();
        barbsPath.reset();
    }

    @Override
    public boolean onShape(float xC, float yC, float r) {
        float x0 = this.start.x;
        float y0 = this.start.y;
        float x = this.end.x;
        float y = this.end.y;

        final float _x1 = x0 - xC;
        final float _x2 = x - xC;
        final float _y1 = y0 - yC;
        final float _y2 = y - yC;

        final float dx = _x2 - _x1;
        final float dy = _y2 - _y1;

        final float a = dx * dx + dy * dy;
        final float b = 2.0F * (_x1 * dx + _y1 * dy);
        final float c = _x1 * _x1 + _y1 * _y1 - (float) Math.pow((double) r, 2.0);

        if (-b < 0.0F) {
            return c < 0.0F;
        }
        if (-b < 2.0F * a) {
            return 4.0F * a * c - b * b < 0.0F;
        }

        return a + b + c < 0.0F;
    }

    @Override
    public void enableSelect(final boolean enable) {
        this.selected = enable;
        for (View view : handlers) {
            view.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    public static class ArrowShapeSnapShot implements ShapeSnapshot {
        private Paint paint;
        private PointF start;
        private PointF end;

        public ArrowShapeSnapShot(final Paint paint,
                                  final PointF start,
                                  final PointF end) {
            this.paint = paint;
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ArrowShapeSnapShot)) {
                return false;
            }

            final ArrowShapeSnapShot that = (ArrowShapeSnapShot) o;

            if (!paint.equals(that.paint)) {
                return false;
            }
            if (!start.equals(that.start)) {
                return false;
            }
            return end.equals(that.end);

        }

        @Override
        public int hashCode() {
            int result = paint.hashCode();
            result = 31 * result + start.hashCode();
            result = 31 * result + end.hashCode();
            return result;
        }
    }
}
