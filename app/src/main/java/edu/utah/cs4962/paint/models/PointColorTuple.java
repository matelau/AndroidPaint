package edu.utah.cs4962.paint.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Each Point is a color and in or out of a continuum
 */
public class PointColorTuple implements Serializable{
    protected int _color;
    protected boolean _inContinuum;
    protected PointF _point;

    /*
    * Constructor
     */
    public PointColorTuple(int color, boolean inContinuum, PointF point ){
        _color = color;
        _inContinuum = inContinuum;
        _point = point;
    }
    public int get_color() {
        return _color;
    }

    public boolean is_inContinuum() {
        return _inContinuum;
    }


    public void draw(Canvas canvas, int index){
        Paint pt = new Paint(Paint.ANTI_ALIAS_FLAG);
        pt.setStyle(Paint.Style.FILL_AND_STROKE);
        pt.setColor(_color);

        if(!_inContinuum){
            canvas.drawCircle(_point.x, _point.y, 5f, pt);
        }
        else{
            pt.setStrokeWidth(10f);
            ArrayList<PointColorTuple> pct;
            if(PaintApplication.is_inPaintMode())
                 pct = PaintApplication.get_points();
            else
                pct = PaintApplication.get_pointsToAnim();
            if(index > 0){
                PointF prev = pct.get(index-1)._point;
                canvas.drawLine(prev.x, prev.y, _point.x, _point.y, pt);

            }

        }

    }

}
