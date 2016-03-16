package com.example.igor.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.EditText;

/**
 * Created by Sergey Prokofev
 * on 29.02.16
 * sergey.prokofev@altarix.ru
 * skype masterw0rks
 */

public class TextShape extends AbstractShape {

    private RectF rect;
    private String text = "kajfasdfa";
    private Paint bgRectPaint;
    private EditText editText;
    private boolean editing;

    public TextShape(Context context, PointF base) {
        super(context);
        bgRectPaint = new Paint();
        this.shapePoints.add(base);
        this.shapePoints.add(new PointF(base.x + 100, base.y + 100));
        rect = new RectF(shapePoints.get(START).x, shapePoints.get(START).y, shapePoints.get(END).x,
                shapePoints.get(END).y);
    }

    @Override
    public void initPaint() {
        paint.setColor(Color.rgb(200, 100, 100));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        //todo add screen density deps
        paint.setTextSize(40);

        //        bgRectPaint.setColor(Color.WHITE);
        //        bgRectPaint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
    }

    @Override
    protected void onTransform(int index) {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, shapePoints.get(START).x, shapePoints.get(START).y, paint);
        float textWidth = paint.measureText(text);
        canvas.drawRect(rect, paint);
    }

    @Override
    public boolean onShape(float xC, float yC, float r) {
        /** Coords are on the shape if they are inside drawn rectangle within finger raduis
         *
         * Copied from AbstractRectangleShape */

        boolean inBoundsX = (rect.left - r) <= xC && xC <= (rect.right + r);
        boolean inBoundsY = (rect.top - r) <= yC && yC <= (rect.bottom + r);

        return inBoundsX && inBoundsY;
    }

    @Override
    public ShapeSnapshot makeSnapshot() {
        // TODO: 01.03.16 IMPLEMENT
        return null;
    }

    @Override
    public void restoreFromSnapshot(final ShapeSnapshot shapeSnapshot) {
        // TODO: 01.03.16 IMPLEMENT
    }

    @Override
    public void reset() {
    }

    @Override
    public PointF getHandlePoint(int index) {
        return null;
    }

    public void setHandlePoint(int index, PointF value) {

    }

    @Override
    public int getHandlesCount() {
        return 0;
    }

    /** Enabling edit text */
    public void onLongPress(CanvasLayout canvasLayout) {
        editing = true;
        canvasLayout.addView(editText, 100, 100);
    }

    @Override
    public void move(PointF move) {
        shapePoints.get(START).offset(move.x,move.y);
        super.move(move);
    }
}

