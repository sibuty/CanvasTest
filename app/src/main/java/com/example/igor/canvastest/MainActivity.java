package com.example.igor.canvastest;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CanvasLayout canvasLayout = (CanvasLayout) findViewById(R.id.main_layout);
        AbstractShape arrowShape = new ArrowShape(new PointF(200,200), new PointF(100, 100));
        AbstractShape rectangleShape =
                new RectangleShape(new PointF(200, 200), new PointF(400, 400));

        canvasLayout.addShape(arrowShape);
        canvasLayout.addShape(rectangleShape);
    }
}
