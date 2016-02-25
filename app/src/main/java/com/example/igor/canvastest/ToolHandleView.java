package com.example.igor.canvastest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Igor on 17.02.2016.
 */
public class ToolHandleView extends ImageView implements View.OnTouchListener {

    private int[] coords = null;
    private PointF offset = new PointF();
    //private RectF bounds = new RectF();
    private PositionListener positionListener;
    private PointF startPoint = new PointF();

    public ToolHandleView(Context context) {
        super(context);
        initView();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
    }

    public ToolHandleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ToolHandleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToolHandleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    void initView() {
        setImageResource(R.drawable.tool_handle);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX();
                float y = event.getRawY();
                setTranslationX(x - offset.x);
                setTranslationY(y - offset.y);
                ensureBounds();
                onPositionChanged();
                break;
            case MotionEvent.ACTION_DOWN:
                coords = new int[2];
                ((View) view.getParent()).getLocationOnScreen(coords);
                offset.x = event.getX() + (float) getLeft() + coords[0];
                offset.y = event.getY() + (float) getTop() + coords[1];
                /*bounds.left = event.getX() + coords[0];
                bounds.top = event.getY() + coords[1];
                bounds.right = (float) (((View) getParent()).getWidth() - view.getWidth()) + event.getX() + coords[0];
                bounds.bottom =
                        (float) (((View) getParent()).getHeight() - view.getHeight()) + event.getY() + coords[1];*/
                break;
        }
        return true;
    }

    public void ensureBounds() {
        View parent = (View) getParent();
        if (getX() < 0.0F) {
            setX(0.0F);
        } else if (getX() + (float) getWidth() > (float) parent.getWidth()) {
            setX((float) (parent.getWidth() - getWidth()));
        }
        if (getY() < 0.0F) {
            setY(0.0F);
        } else if (getY() + (float) getHeight() > (float) parent.getHeight()) {
            setY((float) (parent.getHeight() - getHeight()));
        }
    }

    public void setPositionListener(final PositionListener positionListener) {
        this.positionListener = positionListener;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        onPositionChanged();
    }

    public void onPositionChanged() {
        if (positionListener != null) {
            startPoint.set(getTranslationX() + (float) (getWidth() / 2), getTranslationY()
                    + (float) (getHeight() / 2));
            positionListener.onPositionChanged(startPoint);
        }
    }

    public void move(PointF delta) {
        float x = getTranslationX() + delta.x;
        float y = getTranslationY() + delta.y;
        setX(x);
        setY(y);
        ensureBounds();
        onPositionChanged();
    }
}
