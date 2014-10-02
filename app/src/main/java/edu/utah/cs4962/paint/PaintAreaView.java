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
    protected ArrayList<PlotColorTuple> _doodles = new ArrayList<PlotColorTuple>();
    protected Bitmap _mBitmap;

    protected Canvas _canvas;

    /* Constructor */
    public PaintAreaView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(_mBitmap == null){
            _mBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.RGB_565);
            _canvas = new Canvas(_mBitmap);

        }
        _canvas.setBitmap(_mBitmap);


        if(_points.size() != 0){
            PlotColorTuple pt = new PlotColorTuple(PaintApplication.get_selectedPaint(), _points);
            pt.drawDoodles(_canvas);
        }

        if(PaintApplication.get_drawings() != null){
            _doodles = PaintApplication.get_drawings();
            if(_doodles.size() != 0){
                //iterate through doodles
                for(int i = 0; i < _doodles.size(); i++){
                    _doodles.get(i).drawDoodles(_canvas);
                }
            }

            PaintApplication.set_canvas(_canvas);
        }
        canvas.drawBitmap(_mBitmap, 0, 0, null);
    }

    @Override
    public void draw(Canvas canvas){
        if(_mBitmap != null ){
            canvas.drawBitmap(_mBitmap, 0, 0, null);
        }

    }

    protected void clear(){
        _points.clear();
        _mBitmap = null;
        invalidate();
    }




    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        _points.add(new PointF(x,y));

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            //Store points
            _points.clear();
        }

        if(PaintApplication.get_drawings() != null){
            ArrayList<PlotColorTuple> saved = PaintApplication.get_drawings();
            saved.add(new PlotColorTuple(PaintApplication.get_selectedPaint(), _points));
            PaintApplication.set_drawings(saved);
        }
        else{
            ArrayList<PlotColorTuple> doodles = new ArrayList<PlotColorTuple>();
            doodles.add(new PlotColorTuple(PaintApplication.get_selectedPaint(), _points));
            PaintApplication.set_drawings(doodles);
        }
        invalidate();

        return true;
    }
}