package edu.utah.cs4962.paint.models;

import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;

import edu.utah.cs4962.paint.misc.PlotColorTuple;

/**
 * Maintains the Global State of the Application
 */
public class PaintApplication  extends Application {

    private static Context context;
    protected static ArrayList<PlotColorTuple> _drawings;
    protected static ArrayList<Integer> _paintColors = new ArrayList<Integer>(){{
        add(Color.RED);
        add(Color.GREEN);
        add(Color.BLUE);
        add(Color.WHITE);
        add(Color.BLACK);
    }};

    protected static int _selectedPaint = Color.BLACK;
    protected static boolean _inPaintMode = true;
    protected static Canvas _canvas = null;

    static {
        _drawings = new ArrayList<PlotColorTuple>();
    }

    protected static ArrayList<PointColorTuple> _points = new ArrayList<PointColorTuple>();

    protected static ArrayList<PointColorTuple> _pointsToAnim = new ArrayList<PointColorTuple>();

    public static ArrayList<PointColorTuple> get_pointsToAnim() {
        return _pointsToAnim;
    }

    public static void set_pointsToAnim(ArrayList<PointColorTuple> _pointsToAnim) {
        PaintApplication._pointsToAnim = _pointsToAnim;
    }

    public static ArrayList<PointColorTuple> get_points() {
        return _points;
    }

    public static void set_points(ArrayList<PointColorTuple> _points) {
        PaintApplication._points = _points;
    }

    public static Canvas get_canvas() {
        return _canvas;
    }

    public static void set_canvas(Canvas canvas) {
        PaintApplication._canvas = canvas;
    }

    public static int get_selectedPaint() {
        return _selectedPaint;
    }

    public static void set_selectedPaint(int selectedPaint) {
        _selectedPaint = selectedPaint;
    }


    public static ArrayList<Integer> get_paintColors() {
        return _paintColors;
    }

    public static void set_paintColors(ArrayList<Integer> paintColors) {
        _paintColors = paintColors;
    }

    public static void add_paintColor(int color){
        _paintColors.add(color);
    }

    public static boolean is_inPaintMode() {
        return _inPaintMode;
    }

    public static void set_inPaintMode(boolean _inPaintMode) {
        PaintApplication._inPaintMode = _inPaintMode;
    }


    public void onCreate(){
        super.onCreate();
        PaintApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return PaintApplication.context;
    }
}
