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

// keep count of points and set slider to x amount of points when animating redraw that many points
public class PaintActivity extends Activity {

    PaintAreaView _paintAreaView;
    PaletteView _paletteView;
    boolean _paintMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getActionBar();
        Button selectPaint = new Button(this);
        selectPaint.setText("Choose Paint");
        Button watchMode= new Button(this);
        watchMode.setText("Review");
        if(PaintApplication._selectedPaint != Color.BLACK){
            selectPaint.setBackgroundColor(PaintApplication._selectedPaint);
        }
        else {
            selectPaint.setTextColor(Color.WHITE);
            selectPaint.setBackgroundColor(Color.BLACK);
        }
        selectPaint.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaintApplication.getAppContext(), PaletteActivity.class));
            }
        });
        LinearLayout abll = new LinearLayout(this);
        abll.setOrientation(LinearLayout.HORIZONTAL);
        abll.addView(selectPaint);
        abll.addView(watchMode);
        ab.setCustomView(abll);
        ab.setDisplayShowCustomEnabled(true);
        ab.show();
        LinearLayout rootLayout = new LinearLayout(this);

        Display display = ((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();


        _paintAreaView = new PaintAreaView(this);

        if(rotation  == 0 ){
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));
        }
        else{
            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .4f));

        }

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
