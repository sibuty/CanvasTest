package com.example.igor.canvastest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.EditText;

/**
 * Created by Sergey Prokofev
 * on 29.02.16
 * sergey.prokofev@altarix.ru
 * skype masterw0rks
 */

public class TextShape extends AbstractShape {

    private String text = "Quick brown fox jumps over a lazy dog";
    private Paint bgRectPaint;
    private EditText editText;
    private boolean editing;
    private final int textSize = 40;
    private RectF rect;
    private final int margin = 5;

    public TextShape(Context context, PointF base) {
        super(context);
        this.shapePoints.add(base);
    }

    @Override
    public void initPaint() {
        paint.setColor(Color.MAGENTA);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        //todo add screen density deps
        paint.setTextSize(textSize);
        paint.setStrokeWidth(1);

        bgRectPaint = new Paint();
        bgRectPaint.setColor(Color.MAGENTA);
        bgRectPaint.setStyle(Paint.Style.STROKE);
        bgRectPaint.setStrokeWidth(5);
    }

    @Override
    protected void onTransform(int index) {

    }

    @Override
    public void draw(Canvas canvas) {
                float textWidth = paint.measureText(text);
        PointF start = shapePoints.get(START);
        //        /* Triple margin at bottom for border lay lower than j,q letter tails */
                rect = new RectF(start.x - margin, start.y - textSize - margin, start.x + textWidth + margin,
                        start.y + 3*margin);
        //        canvas.drawRect(rect, bgRectPaint);
        //        canvas.drawText(text, start.x, start.y, paint);

        TextPaint mTextPaint = new TextPaint();
        mTextPaint.setDither(true);
        mTextPaint.setColor(Color.MAGENTA);
        mTextPaint.setTextSize(textSize);
        StaticLayout mTextLayout =
                new StaticLayout(text, mTextPaint, 300, Layout.Alignment.ALIGN_NORMAL,
                        1.0f, 0.0f, false);

        canvas.save();
        // calculate x and y position where your text will be placed

        canvas.translate(start.x, start.y);
        mTextLayout.draw(canvas);
        canvas.restore();
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

