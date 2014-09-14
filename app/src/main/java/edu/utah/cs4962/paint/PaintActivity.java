package edu.utah.cs4962.paint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;


public class PaintActivity extends Activity {
    PaintView _paintView;
    PaintAreaView _paintAreaView;
    PaletteView _paleteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setWeightSum(1f);

        Display display = ((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        _paintAreaView = new PaintAreaView(this);
        _paleteView = new PaletteView(this);


        _paintView = new PaintView(this);
        _paintView.setPadding(30, 30, 30, 30);
        _paintView.setColor(Color.RED);
        _paintView.setBackgroundColor(Color.BLACK);
        _paintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _paintView.setColor(Color.GREEN);
            }
        });

        if(rotation  == 0 ){
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
            rootLayout.addView(_paleteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
        }
        else{
            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
            rootLayout.addView(_paleteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));

        }


        _paintView.setOnSplotchTouchListener(new PaintView.OnSplotchTouchListener() {
            @Override
            public void onSplotchTouched(PaintView v) {
                _paintView.setColor(Color.BLUE);
            }
    });



        setContentView(rootLayout);

    }

    @Override
    protected void onResume(){
        super.onResume();
        _paintView.setColor(Color.BLUE);

    }

}
