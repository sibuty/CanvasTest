package com.example.igor.canvastest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by glotemz on 18.02.16.
 */
public interface Shape {

    void draw(Canvas canvas);

    PointF getShapePoint(int index);

    void setShapePoint(int index, PointF value);

    PointF[] getShapePoints();

    int getShapePointsCount();

    Paint getPaint();

    void setPaint(Paint paint);

    void reset();

    boolean checkCircle(float xC, float yC);

    boolean canMove();

    void enableMove(boolean enable);
}
