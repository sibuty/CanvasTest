package com.example.igor.canvastest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
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

    private List<AbstractShape> shapes = new ArrayList<>();
    private PointF moveShapePoint = new PointF();

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

    protected void addShape(final AbstractShape shape) {
        for (int i = 0; i < shape.getHandlersCount(); i++) {
            PointF point = shape.getShapePoint(i);
            if(point != null) {
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
                shape.getHandlers().add(toolHandleView);
            }
        }
        shapes.add(shape);
    }

    public void ensureBounds(List<View> handlers, PointF delta) {
        float minX = handlers.get(0).getX();
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
        maxY = maxY + delta.y;

        float dx = 0.0F;

        if (minX < 0.0F) {
            dx = delta.x - minX;
        } else if (maxX > (float) getWidth()) {
            dx = (float) getWidth() - maxX + delta.x;
        } else {
            dx = delta.x;
        }

        float dy = 0.0F;

        if (minY < 0.0F) {
            dy = delta.y - minY;
        } else if (maxY > (float) getHeight()) {
            dy = (float) getHeight() - maxY + delta.y;
        } else {
            dy = delta.y;
        }

        for (View view : handlers) {
            if (view instanceof ToolHandleView) {
                ToolHandleView toolHandleView = (ToolHandleView) view;
                toolHandleView.move(new PointF(dx, dy));
            }
        }
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        for (int i = 0; i < shapes.size(); i++) {
            AbstractShape shape = shapes.get(i);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (shape.onShape(event.getX(), event.getY(), 20.0F)) {
                        shape.enableMove(true);
                        shape.enableSelect(true);
                        moveShapePoint.set(event.getX(), event.getY());
                    } else {
                        shape.enableSelect(false);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (shape.canMove()) {
                        ensureBounds(shape.getHandlers(),
                                new PointF(event.getX() - moveShapePoint.x, event.getY() - moveShapePoint.y));
                        moveShapePoint.set(event.getX(), event.getY());
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
