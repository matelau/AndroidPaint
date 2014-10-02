package edu.utah.cs4962.paint;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

// keep count of points and set slider to x amount of points when animating redraw that many points
public class PaintActivity extends Activity {

    PaintAreaView _paintAreaView;
    PaletteView _paletteView;
    boolean _paintSelected = false;
    boolean _displayedWelcome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getActionBar();
        Button selectPaint = new Button(this);
        selectPaint.setText("Choose Paint");
        if(PaintApplication._selectedPaint != Color.BLACK){
            selectPaint.setBackgroundColor(PaintApplication._selectedPaint);
        }
        else {
            selectPaint.setTextColor(Color.WHITE);
            selectPaint.setBackgroundColor(Color.BLACK);
        }
        selectPaint.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaintApplication.getAppContext(), PaletteActivity.class));
            }
        });
        ab.setCustomView(selectPaint);
        ab.setDisplayShowCustomEnabled(true);
        ab.show();
        LinearLayout rootLayout = new LinearLayout(this);

        Display display = ((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

//        _paletteView = new PaletteView(this);
//        _paletteView.setBackgroundColor(Color.BLACK);
//
//        ArrayList<Integer> paints = PaintApplication.get_paintColors();
//
//
//        for ( int splotchIndex = 0; splotchIndex < paints.size(); splotchIndex++)
//        {
//            PaintView paintView = new PaintView(this);
////            paintView.setPadding(10,10,10,10);
//            paintView.setColor(PaintApplication.get_paintColors().get(splotchIndex));
//            //set full transparency for background
//            paintView.setBackgroundColor(Color.green(0x80000000));
//            paintView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    _paintSelected = true;
//                    v.setBackgroundResource(android.R.drawable.list_selector_background);
//                    int selected =((PaintView) v).getColor();
//                    PaintApplication.set_selectedPaint(selected);
//                }
//            });
//            _paletteView.addView(paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//        }

        _paintAreaView = new PaintAreaView(this);


        if(rotation  == 0 ){
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));
//            rootLayout.addView(_paletteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));
        }
        else{
            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));
//            rootLayout.addView(_paletteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));

        }
//        if(!_displayedWelcome){
//            Toast.makeText(PaintApplication.getAppContext(), "Welcome to Paint", Toast.LENGTH_SHORT).show();
//            Toast.makeText(PaintApplication.getAppContext(), "Drag paints on-top of another to mix", Toast.LENGTH_SHORT).show();
//            Toast.makeText(PaintApplication.getAppContext(), "Drag paints on-top of paint area to remove", Toast.LENGTH_SHORT).show();
//            Toast.makeText(PaintApplication.getAppContext(), "Otherwise tap a paint and begin", Toast.LENGTH_SHORT).show();
//            _displayedWelcome = true;
//        }

        setContentView(rootLayout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Save");
        menu.add("Load");
        menu.add("Clear");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        //TODO: implement save and load
        if(item.getTitle() == "Clear"){
            _paintAreaView.clear();
            PaintApplication.set_drawings(new ArrayList<PlotColorTuple>());


        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(PaintApplication.get_points().size() > 0)
        {
            _paintAreaView.draw(PaintApplication.get_canvas());
        }
    }

}
