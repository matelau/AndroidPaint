package edu.utah.cs4962.paint.misc;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;

import edu.utah.cs4962.paint.models.PaintApplication;

/**
 * Each Drawing is a color and a set of points
 */
public class PlotColorTuple {
    protected int _color;
    protected ArrayList<PointF> _storedPoints;
    protected boolean _inContinuum;

    /*
    * Constructor
     */
    public PlotColorTuple(int color, ArrayList<PointF> points){
        _color = color;
        _storedPoints = points;
//        _inContinuum = continued;
    }
    public int get_color() {
        return _color;
    }

    public boolean is_inContinuum() {
        return _inContinuum;
    }

    public ArrayList<PointF> get_points() {
        return _storedPoints;
    }

    protected void drawDoodles(Canvas canvas){
        if(_storedPoints.size() != 0){
            Paint polyLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            polyLinePaint.setStyle(Paint.Style.STROKE);
            polyLinePaint.setStrokeWidth(8.0f);
            polyLinePaint.setColor(PaintApplication.get_selectedPaint());

            Path path = new Path();
            path.moveTo(_storedPoints.get(0).x, _storedPoints.get(0).y);

            for(int i = 0; i < _storedPoints.size(); i++) {
                path.lineTo(_storedPoints.get(i).x, _storedPoints.get(i).y);
            }

            canvas.drawPath(path, polyLinePaint);
        }

    }

}
