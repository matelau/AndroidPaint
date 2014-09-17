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
    PaletteView _paletteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        PaletteView rootLayout = new PaletteView(this);
//        rootLayout.setWeightSum(1f);

        Display display = ((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
//        _paintAreaView = new PaintAreaView(this);
//        _paletteView = new PaletteView(this);

        for ( int splotchIndex = 0; splotchIndex <= 10; splotchIndex++)
        {
            PaintView paintView = new PaintView(this);
            paintView.setColor(Color.RED);
            paintView.setBackgroundColor(Color.BLACK);
            paintView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    paintView.setColor(Color.GREEN);
                    v.invalidate();
                    v.setVisibility(View.GONE);
                }
            });
            rootLayout.addView(paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }


//        if(rotation  == 0 ){
//            rootLayout.setOrientation(LinearLayout.VERTICAL);
//            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
//            rootLayout.addView(_paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
//        }
//        else{
//            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
//            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
//            rootLayout.addView(_paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
//
//        }


//        _paintView.setOnSplotchTouchListener(new PaintView.OnSplotchTouchListener() {
//            @Override
//            public void onSplotchTouched(PaintView v) {
//                _paintView.setColor(Color.BLUE);
//            }
//    });



        setContentView(rootLayout);

    }

    @Override
    protected void onResume(){
        super.onResume();
//        _paintView.setColor(Color.BLUE);


    }

}
