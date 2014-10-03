package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 *  A subclass of View that overrides onDraw, and allows the user to paint
 using their finger. This is accomplished by overriding the onTouch method of View and using
 the data given to form poly-lines. The view must be resizable, even after paint has been
 applied to the view. To accomplish this, store the poly-lines that the user draws, including the
 color used to draw them. Then, whenever the view needs to be redrawn, use the stored
 locations and colors to draw all of the poly-lines. Storing the points in a unit coordinate space
 (0.0 -> 1.0 in both x and y) allows a similar picture to be drawn whatever the view’s size.
 */
public class PaintAreaView extends View {

    protected List<PlotColorTuple> _drawings = new ArrayList<PlotColorTuple>();
    protected ArrayList<PointF> _points = new ArrayList<PointF>();
    protected ArrayList<PointColorTuple> _pct = new ArrayList<PointColorTuple>();
    protected ArrayList<PlotColorTuple> _doodles = new ArrayList<PlotColorTuple>();
    protected Bitmap _mBitmap;

    protected Canvas _canvas;

    /* Constructor */
    public PaintAreaView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(_pct.size() > 0){
            for(int i = 0; i < _pct.size();i++){
                _pct.get(i).draw(canvas, i);
            }
            PaintApplication.set_canvas(canvas);
            invalidate();
        }
    }


    @Override
    public void draw(Canvas canvas){
        _pct = PaintApplication.get_points();
        if(_pct.size() > 0){
            for(int i = 0; i < _pct.size();i++){
                _pct.get(i).draw(canvas, i);
            }
            PaintApplication.set_canvas(canvas);
            invalidate();
        }
    }

    protected void clear(){
        _pct.clear();
        PaintApplication.set_points(_pct);
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        PointColorTuple pct = null;
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            pct = new PointColorTuple(PaintApplication.get_selectedPaint(), false, new PointF(x, y));

        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            pct = new PointColorTuple(PaintApplication.get_selectedPaint(), true, new PointF(x, y));
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            pct = new PointColorTuple(PaintApplication.get_selectedPaint(), false, new PointF(x, y));

        }
        if(pct != null)
        {
            _pct.add(pct);
            PaintApplication.set_points(_pct);
        }
        invalidate();

        return true;
    }
}