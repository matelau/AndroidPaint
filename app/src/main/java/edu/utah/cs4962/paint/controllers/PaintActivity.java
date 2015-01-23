package edu.utah.cs4962.paint.controllers;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import edu.utah.cs4962.paint.models.PaintApplication;
import edu.utah.cs4962.paint.models.PointColorTuple;
import edu.utah.cs4962.paint.views.PaintAreaView;


/**
 * Central Activity to Application creates a blank palette and allows a user to choose paint
 */
public class PaintActivity extends Activity {

    protected PaintAreaView _paintAreaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup Action Bar with Options
        ActionBar ab = getActionBar();
        Button selectPaint = new Button(this);
        selectPaint.setText("Choose Paint");
        //Sets the buttons color to reflect color choice
        if(PaintApplication.get_selectedPaint() != Color.BLACK){
            selectPaint.setBackgroundColor(PaintApplication.get_selectedPaint());
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
        //Button to allow a user to watch
        Button watchMode= new Button(this);
        watchMode.setText("Review");
        watchMode.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaintApplication.set_inPaintMode(false);
                startActivity(new Intent(PaintApplication.getAppContext(), WatchActivity.class));
            }
        });

        LinearLayout abll = new LinearLayout(this);
        abll.setOrientation(LinearLayout.HORIZONTAL);
        abll.addView(selectPaint);
        abll.addView(watchMode);
        ab.setCustomView(abll);
        ab.setDisplayShowCustomEnabled(true);
        ab.show();

        //PaintArea
        LinearLayout rootLayout = new LinearLayout(this);
        _paintAreaView = new PaintAreaView(this);
        rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
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
        if(item.getTitle() == "Clear"){
            _paintAreaView.clear();
        }
        else if(item.getTitle() == "Save"){
            save();
        }
        else if(item.getTitle() == "Load"){
            load();
        }

        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * Saves the current drawing
     */
    protected void save(){
        Type tp = new TypeToken<Collection<PointColorTuple>>(){}.getType();
        Gson gson = new Gson();
        String s = gson.toJson(PaintApplication.get_points(), tp);
        Log.i("Save", s);
        FileOutputStream outputStream;
        try{
             outputStream = openFileOutput("Paint.dat", MODE_PRIVATE);
             outputStream.write(s.getBytes());
             outputStream.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Loads a stored drawing
     */
    protected void load(){
        try{
            Type tp = new TypeToken<Collection<PointColorTuple>>(){}.getType();
            FileInputStream fis = getApplicationContext().openFileInput("Paint.dat");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }

            String json = sb.toString();
            Log.i("Load", json);
            Gson gson = new Gson();
            PaintApplication.set_points((ArrayList<PointColorTuple>) gson.fromJson(json, tp ));
            _paintAreaView.restore();

        }
        catch(Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    @Override
    protected void onResume(){
        super.onResume();
        File file = getFileStreamPath("Paint.dat");
        if(PaintApplication.get_points().size() > 0)
        {
            _paintAreaView.draw(PaintApplication.get_canvas());
        }
        else if(file.exists()){
            load();
        }
    }

}
