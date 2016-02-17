package com.example.igor.canvastest;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Igor on 17.02.2016.
 */
public class ToolHandleView extends ImageView {

    public ToolHandleView(Context context) {
        super(context);
        initVIew();
    }

    public ToolHandleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVIew();
    }

    public ToolHandleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVIew();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToolHandleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVIew();
    }

    void initVIew() {
        
    }
}
