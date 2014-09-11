package edu.utah.cs4962.paint;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


public class PaintActivity extends Activity {
    PaintView _paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _paintView = new PaintView(this);
        _paintView.setPadding(10, 20, 30, 40);
        _paintView.setColor(Color.RED);
        _paintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _paintView.setColor(Color.GREEN);
            }
        });

        _paintView.setOnSplotchTouchListener(new PaintView.OnSplotchTouchListener() {
            @Override
            public void onSplotchTouched(PaintView v) {
                _paintView.setColor(Color.BLUE);
            }
    });

        setContentView(_paintView);

    }

    @Override
    protected void onResume(){
        super.onResume();
        _paintView.setColor(Color.BLUE);

    }

}
