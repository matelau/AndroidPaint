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

// keep count of points and set slider to x amount of points when animating redraw that many points
public class PaintActivity extends Activity {

    PaintAreaView _paintAreaView;
    PaletteView _paletteView;
    boolean _paintSelected = false;
    boolean _displayWelcome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);

        Display display = ((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        _paletteView = new PaletteView(this);
        _paletteView.setBackgroundColor(Color.BLACK);

        ArrayList<Integer> paints = PaintApplication.get_paintColors();


        for ( int splotchIndex = 0; splotchIndex < paints.size(); splotchIndex++)
        {
            PaintView paintView = new PaintView(this);
//            paintView.setPadding(10,10,10,10);
            paintView.setColor(PaintApplication.get_paintColors().get(splotchIndex));
            //set full transparency for background
            paintView.setBackgroundColor(Color.green(0x80000000));
            paintView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _paintSelected = true;
                    v.setBackgroundResource(android.R.drawable.list_selector_background);
                    int selected =((PaintView) v).getColor();
                    PaintApplication.set_selectedPaint(selected);
                }
            });
            _paletteView.addView(paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        _paintAreaView = new PaintAreaView(this);


        if(rotation  == 0 ){
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));
            rootLayout.addView(_paletteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));
        }
        else{
            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));
            rootLayout.addView(_paletteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));

        }
        if(!_displayWelcome){
            Toast.makeText(PaintApplication.getAppContext(), "Welcome to Paint", Toast.LENGTH_SHORT).show();
            Toast.makeText(PaintApplication.getAppContext(), "Drag paints on-top of another to mix", Toast.LENGTH_SHORT).show();
            Toast.makeText(PaintApplication.getAppContext(), "Drag paints on-top of paint area to remove", Toast.LENGTH_SHORT).show();
            Toast.makeText(PaintApplication.getAppContext(), "Otherwise tap a paint and begin", Toast.LENGTH_SHORT).show();
            _displayWelcome = true;
        }

        setContentView(rootLayout);

    }

    @Override
    protected void onResume(){
        _displayWelcome = false;
        super.onResume();
//        if(PaintApplication.get_canvas() != null)
//         _paintAreaView.onDraw(PaintApplication.get_canvas());
////        ArrayList<PlotColorTuple> doodles = PaintApplication.get_drawings();
////        for(int i = 0; i < doodles.size(); i++){
////            doodles.get(i).drawDoodles(PaintApplication.get_canvas());
////        }
//        PaintApplication.set_drawings(new ArrayList<PlotColorTuple>());
    }

}
