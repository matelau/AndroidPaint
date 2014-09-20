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
import android.widget.Toast;

import java.util.ArrayList;


public class PaintActivity extends Activity {
    PaintView _paintView;
    PaintAreaView _paintAreaView;
    PaletteView _paletteView;
    boolean _paintSelected = false;
//    PaintApplication _pa = new PaintApplication();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        PaletteView paletteView = new PaletteView(this);

        Display display = ((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        _paletteView = new PaletteView(this);
        _paletteView.setBackgroundColor(Color.BLACK);

        // Initialize starting paints
//        _pa = new PaintApplication();
        ArrayList<Integer> paints = new ArrayList<Integer>();
        paints.add(Color.RED);
        paints.add(Color.GREEN);
        paints.add(Color.BLUE);
        paints.add(Color.WHITE);
        paints.add(Color.BLACK);
        PaintApplication.set_paintColors(paints);

        for ( int splotchIndex = 0; splotchIndex < paints.size(); splotchIndex++)
        {
            PaintView paintView = new PaintView(this);
            paintView.setColor(PaintApplication.get_paintColors().get(splotchIndex));
            //set full transparency
            paintView.setBackgroundColor(Color.green(0x80000000));
            paintView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _paintSelected = true;
                    v.setBackgroundResource(android.R.drawable.list_selector_background);
                    int duration = Toast.LENGTH_SHORT;
                    int selected =((PaintView) v).getColor();
                    _paintAreaView.set_selectedPaint(selected);
                    _paintAreaView.set_paintSelected(_paintSelected);
                    PaintApplication.set_selectedPaint(selected);
                    String text= "Paint Color: "+ selected +" paint selected: "+  _paintSelected;
                    Toast toast = Toast.makeText(PaintApplication.getAppContext(), text, duration);
                    toast.show();

                }
            });
            _paletteView.addView(paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        _paintAreaView = new PaintAreaView(this);

        if(rotation  == 0 ){
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
            rootLayout.addView(_paletteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
        }
        else{
            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));
            rootLayout.addView(_paletteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .5f));

        }


        setContentView(rootLayout);

    }

    @Override
    protected void onResume(){
        super.onResume();
//        _paintView.setColor(Color.BLUE);


    }

}
