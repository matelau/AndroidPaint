package edu.utah.cs4962.paint;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;
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

    /* Constructor */
    public PaintAreaView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint border = new Paint();
        border.setStrokeWidth(5.5f);
        border.setColor(Color.BLACK);
        border.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), border);

//        if (_drawings.size() > 0 && _paintSelected){
//            PaintApplication.set_drawings((ArrayList<PlotColorTuple>) _drawings);
        for(int i = 0; i < _drawings.size(); i++){
            PlotColorTuple path = _drawings.get(i);
            Paint polyLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            polyLinePaint.setStyle(Paint.Style.STROKE);
            polyLinePaint.setStrokeWidth(8.0f);
            polyLinePaint.setColor(path.get_color());
            Path currentPath = path.get_points();
            canvas.drawPath(currentPath, polyLinePaint);
//                if(i > 0 && path != null){
//            path.draw(canvas, polyLinePaint);
                //if the last point is connected inContinuum is true
//                    PlotColorTuple smoothingPoint = _drawings.get(i-1);
//                    if(point._color == smoothingPoint._color && point._inContinuum ){
//                        point.draw(canvas,polyLinePaint, smoothingPoint.get_points());
//                    }
//                    else{
//                        point.draw(canvas,polyLinePaint, null);
//                    }
//
//                }
//                else{
//                    point.draw(canvas,polyLinePaint, null);
//                }
//        }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        _drawings = PaintApplication.get_drawings();
        draw(new Canvas());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();


        _points.add(new PointF(x,y));

        if (event.getAction() == MotionEvent.ACTION_UP){
            //Store points
            Path polyLinePath = new Path();
            polyLinePath.moveTo(_points.get(0).x, _points.get(0).y);
            for(int i = 0; i < _points.size(); i++)
            {
                PointF point = _points.get(i);
                polyLinePath.moveTo(point.x, point.y);
            }
//            polyLinePath.close();
            PlotColorTuple currentPath = new PlotColorTuple(PaintApplication.get_selectedPaint(), polyLinePath );
            Log.i("onTouchEvent - PaintAreaView", "points size:" + _points.size() + " " + polyLinePath.toString());
            _drawings.add(currentPath);

            _points.clear();
//              currentPoint = new PlotColorTuple(PaintApplication.get_selectedPaint(), new PointF(x,y), false);

        }
//        else if(event.getAction() == MotionEvent.ACTION_UP){
////            currentPoint = new PlotColorTuple(PaintApplication.get_selectedPaint(), new PointF(x,y), false);
//
//        }
        invalidate();
        return true;
    }
}