package com.example.igor.canvastest;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Igor on 17.02.2016.
 */
public class CanvasLayout extends FrameLayout {

    public CanvasLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public CanvasLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public CanvasLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CanvasLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0, 255, 0));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);

        canvas.drawRoundRect(new RectF(200, 200, 500, 500), 6, 6, paint);

        Path path = new Path();
        float x0 = 550;
        float y0 = 550;

        float x1 = 1000;
        float y1 = 200;

        float size = (float) Math.sqrt(Math.pow(x1-x0, 2) + Math.pow(y1-y0, 2));
        float h = size * 0.2f;

        float arrowMiddleX = (9*x1 + x0)/10;
        float arrowMiddleY = (9*y1 + y0)/10;

        double angle = Math.toRadians(180) - Math.atan2(x0-x1, y1-y0);
        double angleOffset = Math.toRadians(65);

        float arrowLeftX = (float) (Math.sin(angle - angleOffset) * h) + arrowMiddleX;
        float arrowLeftY = (float) (Math.cos(angle - angleOffset) * h) + arrowMiddleY;

        float arrowRightX = (float) (Math.sin(angle + angleOffset) * h) + arrowMiddleX;
        float arrowRightY = (float) (Math.cos(angle + angleOffset) * h) + arrowMiddleY;

        path.moveTo(x0, x0);
        path.lineTo(x1, y1);
        Path path1 = new Path();
        path1.moveTo(arrowLeftX, arrowLeftY);
        path1.lineTo(x1, y1);
        path1.lineTo(arrowRightX, arrowRightY);
        path.addPath(path1);
        canvas.drawPath(path, paint);

        postInvalidate();
    }
}
