package com.example.igor.canvastest;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CanvasLayout canvasLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvasLayout =  (CanvasLayout) findViewById(R.id.main_layout);
        AbstractShape arrowShape = new ArrowShape(this, new PointF(200,200), new PointF(100, 100));
        AbstractShape rectangleShape =
                new RectangleShape(this, new PointF(200, 200), new PointF(400, 400));
        AbstractShape ovalShape =
                new OvalShape(this, new PointF(300, 300), new PointF(600, 400));
        //AbstractShape textShape = new TextShape(new PointF(700, 400));

        canvasLayout.addShape(arrowShape);
        canvasLayout.addShape(rectangleShape);
        canvasLayout.addShape(ovalShape);
        //canvasLayout.addShape(textShape);
    }

    @Override
    public void onBackPressed() {
        if(!canvasLayout.undo()) {
            finish();
        }
    }
}
