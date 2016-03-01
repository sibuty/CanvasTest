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

import java.util.*;

/**
 * Created by Igor on 17.02.2016.
 */
public class CanvasLayout extends FrameLayout implements View.OnTouchListener {

    protected final Stack<Map.Entry<AbstractShape, ShapeSnapshot>> shapeSnapshotStack =
            new Stack<Map.Entry<AbstractShape, ShapeSnapshot>>();
    protected final List<AbstractShape> shapes = new ArrayList<>();

    protected PointF moveShapePoint = new PointF();
    protected AbstractShape target = null;

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

    public boolean undo() {
        if (!shapeSnapshotStack.isEmpty()) {
            Map.Entry<AbstractShape, ShapeSnapshot> shapeSnapshotEntry = shapeSnapshotStack.pop();
            shapeSnapshotEntry.getKey().restoreFromSnapshot(shapeSnapshotEntry.getValue());
            postInvalidate();
            return true;
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = shapes.size() - 1; i >= 0; i--) {
            shapes.get(i).draw(canvas);
        }
    }

    protected void addShape(final AbstractShape shape) {
        for (int i = 0; i < shape.getHandlersCount(); i++) {
            PointF point = shape.getShapePoint(i);
            if (point != null) {
                ToolHandleView toolHandleView = new ToolHandleView(getContext());
                toolHandleView.setX(point.x);
                toolHandleView.setY(point.y);
                final int finalI = i;
                toolHandleView.setPositionListener(new PositionListener() {
                    @Override
                    public void onPositionChanged(PointF pointF) {
                        shape.setShapePoint(finalI, pointF);
                        CanvasLayout.this.postInvalidate();
                    }
                });
                toolHandleView.setShapeSnapshotListener(new ShapeSnapshotListener() {
                    @Override
                    public void onSnapshotMade() {
                        saveSnapshot(target);
                    }
                });
                addView(toolHandleView);
                shape.handlers.add(toolHandleView);
            }
        }
        saveSnapshot(shape);
        shapes.add(shape);
    }

    protected void ensureBounds(List<View> handlers, PointF delta) {
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
        CanvasLayout.this.postInvalidate();
    }

    protected void saveSnapshot(AbstractShape shape) {
        ShapeSnapshot shapeSnapshot = null;
        if (!shapeSnapshotStack.isEmpty()) {
            Map.Entry<AbstractShape, ShapeSnapshot> shapeSnapshotEntry = shapeSnapshotStack.peek();
            shapeSnapshot = shapeSnapshotEntry.getValue();
        }
        if(shape != null) {
            ShapeSnapshot targetShapeSnapshot = shape.makeSnapshot();
            if (targetShapeSnapshot != null && !targetShapeSnapshot.equals(shapeSnapshot)) {
                shapeSnapshotStack.push(new AbstractMap.SimpleEntry<AbstractShape, ShapeSnapshot>(shape,
                        targetShapeSnapshot));

            }
        }
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < shapes.size(); i++) {
                    AbstractShape shape = shapes.get(i);
                    if (target == null) {
                        if (shape.onShape(event.getX(), event.getY(), 20.0F)) {
                            target = shape;
                            shapes.add(0, target);
                            shapes.remove(i + 1);
                            target.canMove = true;
                            target.enableSelect(true);
                            moveShapePoint.set(event.getX(), event.getY());
                        } else if (target != shape) {
                            shape.enableSelect(false);
                        }
                    } else {
                        if (target.onShape(event.getX(), event.getY(), 20.0F)) {
                            target.canMove = true;
                            target.enableSelect(true);
                            moveShapePoint.set(event.getX(), event.getY());
                        } else {
                            target.enableSelect(false);
                            target = null;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (target != null && target.canMove) {
                    ensureBounds(target.handlers,
                            new PointF(event.getX() - moveShapePoint.x, event.getY() - moveShapePoint.y));
                    moveShapePoint.set(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (target != null) {
                    target.canMove = false;
                    saveSnapshot(target);
                }
                break;
        }
        return true;
    }
}
