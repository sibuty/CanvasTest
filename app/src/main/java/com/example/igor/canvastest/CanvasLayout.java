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
    protected AbstractShape targetShape = null;

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
        shape.drawCompleteListener = new AbstractShape.DrawCompleteListener() {
            @Override
            public void onDrawComplete() {
                CanvasLayout.this.postInvalidate();
            }
        };
        shape.shapeSnapshotListener = new ShapeSnapshotListener() {
            @Override
            public void onSnapshotMade() {
                CanvasLayout.this.saveSnapshot(targetShape);
            }
        };
        for (View handle : shape.handles) {
            CanvasLayout.this.addView(handle);
        }
        saveSnapshot(shape);
        shapes.add(shape);
    }

    protected void saveSnapshot(AbstractShape shape) {
        ShapeSnapshot shapeSnapshot = null;
        if (!shapeSnapshotStack.isEmpty()) {
            Map.Entry<AbstractShape, ShapeSnapshot> shapeSnapshotEntry = shapeSnapshotStack.peek();
            shapeSnapshot = shapeSnapshotEntry.getValue();
        }
        if (shape != null) {
            ShapeSnapshot targetShapeSnapshot = shape.makeSnapshot();
            if (targetShapeSnapshot != null && !targetShapeSnapshot.equals(shapeSnapshot)) {
                shapeSnapshotStack.push(new AbstractMap.SimpleEntry<AbstractShape, ShapeSnapshot>(shape,
                        targetShapeSnapshot));

            }
        }
    }

    /* Moving occurs from here */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < shapes.size(); i++) {
                    AbstractShape shape = shapes.get(i);
                    if (targetShape == null) {
                        if (shape.onShape(event.getX(), event.getY(), 20.0F)) {
                            targetShape = shape;
                            shapes.add(0, targetShape);
                            shapes.remove(i + 1);
                            targetShape.canMove = true;
                            targetShape.enableSelect(true);
                            moveShapePoint.set(event.getX(), event.getY());
                        } else if (targetShape != shape) {
                            shape.enableSelect(false);
                        }
                    } else {
                        if (targetShape.onShape(event.getX(), event.getY(), 20.0F)) {
                            targetShape.canMove = true;
                            targetShape.enableSelect(true);
                            moveShapePoint.set(event.getX(), event.getY());
                        } else {
                            targetShape.enableSelect(false);
                            targetShape = null;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (targetShape != null && targetShape.canMove) {
                    PointF delta = targetShape.calculateDelta(
                            new PointF(event.getX() - moveShapePoint.x, event.getY() - moveShapePoint.y),
                            new PointF((float) CanvasLayout.this.getWidth(), (float) CanvasLayout.this.getHeight())
                    );
                    targetShape.moveListener = new AbstractShape.MoveListener() {
                        @Override
                        public void onMove(final PointF delta) {
                            for (View view : targetShape.handles) {
                                if (view instanceof ToolHandleView) {
                                    ToolHandleView toolHandleView = (ToolHandleView) view;
                                    toolHandleView.move(delta);
                                }
                            }
                        }
                    };
                    targetShape.move(delta);
                    moveShapePoint.set(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (targetShape != null) {
                    targetShape.canMove = false;
                    saveSnapshot(targetShape);
                }
                break;
        }
        return true;
    }
}
