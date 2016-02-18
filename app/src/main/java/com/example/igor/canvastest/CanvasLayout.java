package com.example.igor.canvastest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 17.02.2016.
 */
public class CanvasLayout extends FrameLayout implements View.OnTouchListener {

    List<Shape> shapes = new ArrayList<>();

    public CanvasLayout(Context context) {
        super(context);
        initView();
    }

    public CanvasLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CanvasLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CanvasLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    protected void initView() {
        setWillNotDraw(false);
        setOnTouchListener(this);
    }
    /*

    float x0 = 550;
    float y0 = 550;

    float x1 = 1000;
    float y1 = 200;
    float t = 0;

    Shape arrow = new ArrowShape(new PointF(x0, y0), new PointF(x1, y1));
    PointF point = new PointF(x0, y0);*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < shapes.size(); i++) {
            shapes.get(i).draw(canvas);
        }
/*
        t += 0.01;
        x0 = (float) (300 + Math.sin(t) * 300);
        y0 = (float) (400 + Math.cos(t) * 300);
        point.x = x0;
        point.y = y0;
        arrow.setShapePoint(0, point);
        arrow.draw(canvas);*/
        postInvalidate();
    }

    protected void addShape(final Shape shape) {
        for (int i = 0; i < shape.getShapePointsCount(); i++) {
            PointF point = shape.getShapePoint(i);
            ToolHandleView toolHandleView = new ToolHandleView(getContext());
            toolHandleView.setX(point.x);
            toolHandleView.setY(point.y);
            final int finalI = i;
            toolHandleView.setPositionListener(new PositionListener() {
                @Override
                public void onPositionChanged(PointF pointF) {
                    shape.setShapePoint(finalI, pointF);
                }
            });
            addView(toolHandleView);
        }
        shapes.add(shape);
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        for (int i = 0; i < shapes.size(); i++) {
            Shape shape = shapes.get(i);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    shape.enableMove(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Paint paint = shape.getPaint();
                    if (shape.canMove() && shape.checkCircle(event.getX(), event.getY())) {

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    shape.enableMove(false);
                    break;
            }
        }
        return true;
    }
}
