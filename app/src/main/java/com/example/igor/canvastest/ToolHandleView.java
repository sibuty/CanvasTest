package com.example.igor.canvastest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
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
    private RectF bounds = new RectF();
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
                if (x > bounds.left && x < bounds.right) {
                    view.setTranslationX(x - offset.x) ;
                }
                if (y > bounds.top && y < bounds.bottom) {
                    view.setTranslationY(y - offset.y);
                }
                if (positionListener != null) {
                    positionListener.onPositionChanged(new PointF(view.getTranslationX() + view.getWidth()/2, view.getTranslationY() + view.getHeight()/2));
                }
                break;

            case MotionEvent.ACTION_DOWN:
                coords = new int[2];
                ((View) view.getParent()).getLocationOnScreen(coords);
                offset.x = event.getX() + (float) getLeft() + coords[0];
                offset.y = event.getY() + (float) getTop() + coords[1];
                bounds.left = event.getX() + coords[0];
                bounds.top = event.getY() + coords[1];
                bounds.right = (float) (((View) getParent()).getWidth() - view.getWidth()) + event.getX() + coords[0];
                bounds.bottom = (float) (((View) getParent()).getHeight() - view.getHeight()) + event.getY() + coords[1];
                break;
        }
        return true;
    }

    public void setPositionListener(final PositionListener positionListener) {
        this.positionListener = positionListener;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        startPoint.set(getTranslationX() + (float) (getWidth() / 2), getTranslationY() + (float) (getHeight() / 2));
        positionListener.onPositionChanged(startPoint);
    }
}
