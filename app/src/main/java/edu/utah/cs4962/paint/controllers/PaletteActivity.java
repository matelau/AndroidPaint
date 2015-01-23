package edu.utah.cs4962.paint.controllers;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import edu.utah.cs4962.paint.models.PaintApplication;
import edu.utah.cs4962.paint.views.PaintView;
import edu.utah.cs4962.paint.views.PaletteView;


public class PaletteActivity extends Activity {
     protected PaletteView _paletteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            //setup default palette
         _paletteView = new PaletteView(this);
        _paletteView.setBackgroundColor(Color.BLACK);
        ArrayList<Integer> paints = PaintApplication.get_paintColors();


        for ( int splotchIndex = 0; splotchIndex < paints.size(); splotchIndex++)
        {
            PaintView paintView = new PaintView(this);
            paintView.setColor(PaintApplication.get_paintColors().get(splotchIndex));
            //set full transparency for background
            paintView.setBackgroundColor(Color.green(0x80000000));
            paintView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundResource(android.R.drawable.list_selector_background);
                    int selected =((PaintView) v).getColor();
                    PaintApplication.set_selectedPaint(selected);
                }
            });
            _paletteView.addView(paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

            setContentView(_paletteView);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
