package com.example.igor.canvastest;

import android.graphics.Paint;
import android.graphics.PointF;

import java.util.List;

/**
 * Created by glotemz on 01.03.16.
 */
public abstract class ShapeSnapshot {

    public Paint paint;
    public List<PointF> handlePoints;

    public ShapeSnapshot() {
    }

    public ShapeSnapshot(Paint paint, List<PointF> handlePoints) {
        this.paint = paint;
        this.handlePoints = handlePoints;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShapeSnapshot)) {
            return false;
        }

        final ShapeSnapshot snapshot = (ShapeSnapshot) o;

        if (!paint.equals(snapshot.paint)) {
            return false;
        }
        return handlePoints.equals(snapshot.handlePoints);

    }

    @Override
    public int hashCode() {
        int result = paint.hashCode();
        result = 31 * result + handlePoints.hashCode();
        return result;
    }
}

