package edu.utah.cs4962.paint.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import edu.utah.cs4962.paint.models.PaintApplication;

/**
 * A subclass of ViewGroup that overrides onDraw to draw a painter’s palette
 board, and maintains a collection of paints. The class should offer addColor and removeColor
 methods that cause paint views to be added to and removed from the palette, updating the
 layout. Override onLayout to layout the paint views around the palette. The palette view must
 be either circular or shaped like a traditional painter’s palette, and the paints should be
 laid out along the edge of the palette in a circle or following a painter’s palette shape. Do not
 lay the paints out in a grid. When the user touches a paint view, select it (see below). When
 the user then drags their finger, continually set the x and y properties to move the paint under
 the user’s finger, creating a drag action. When the user releases the paint, if it is over another
 paint view, mix the two paints and add the result to the palette (see below). If it is not over
 another paint, simply return the paint to its original position by setting the x and y properties
 equal to the left and top properties, respectively (see View documentation on x, y, left, top).
 */
public class PaletteView extends ViewGroup {
    RectF _contentRect;
    PaintView _transformedView = null;
    float _radius;

    public PaletteView(Context context){
        super(context);
        setWillNotDraw(false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){

        Float x = event.getX();
        Float y = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _transformedView = detectPaintTouched(x,y);

            if(_transformedView != null){
            }
            return false;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            //TODO take account for swipes not on a paintView
            //take control in onTouchEvent
            if(_transformedView != null){
                _transformedView.setX(x);
                _transformedView.setY(y);
            }

            return onTouchEvent(event);
        }
        else if(event.getAction() == MotionEvent.ACTION_UP)
        {
            return onTouchEvent(event);
        }
        return false;
    }


    /**
     * Returns the selected paint or null if x,y is not over a paint view
     * @param x
     * @param y
     * @return
     */
    private PaintView detectPaintTouched(Float x, Float y){
        int xTest = Math.round(x);
        int yTest = Math.round(y);
        //detect which child was touched
        for(int i = 0; i < getChildCount(); i++) {
            PaintView pv = (PaintView) getChildAt(i);
            if (xTest > pv.getLeft() && x < pv.getRight() && yTest > pv.getTop() && yTest < pv.getBottom()) {
                return (PaintView) getChildAt(i);
            }
        }

        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
//        Log.i("PaletteView", "onTouchEvent");
        Float x = event.getX();
        Float y = event.getY();

        if(_transformedView != null && (event.getAction() == MotionEvent.ACTION_MOVE)){
            // check if dragged on top of another paint
            PaintView pv = detectPaintTouched(x,y);
            if(pv != null)
            {
                int color1 = pv.getColor();
                int color2 = _transformedView.getColor();
                if(pv.getColor() != _transformedView.getColor())
                {
                    //add color
                    addColor(color1, color2);
                    removeColor(_transformedView.getColor());
                    addColor(_transformedView.getColor());
                    _transformedView = null;
                    Log.i("PaletteView", "onTouchEvent - Added color "+color1);
                    return true;
                }
                else{
                    return false;
                }
            }

            else if (!inCircle(x, y))
            {
                removeColor( _transformedView.getColor());
                Log.i("PaletteView", "onTouchEvent - Removed color");
                return true;
            }

            else if (event.getAction() == MotionEvent.ACTION_UP){
                //place paint back
                if(_transformedView != null){
                    addColor(_transformedView.getColor());
                    _transformedView = null;
                }

                return true;
            }

        }
        return false;


    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        _contentRect = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                (float) getWidth() - getPaddingRight(),
                (float) getHeight() - getPaddingBottom());

        Paint palPaint = new Paint();
        palPaint.setColor(0xff8b4513); // brown
        palPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        _radius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);
        PointF center = new PointF(_contentRect.centerX(), _contentRect.centerY());
        canvas.drawOval(_contentRect, palPaint);

        Paint cornerPaint = new Paint();
        cornerPaint.setColor(Color.BLACK);
        cornerPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawCircle(center.x+(_radius/1.3f), center.y-(_radius/(1.3f)),_radius/4, cornerPaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);


        for(int childIndex = 0; childIndex < getChildCount(); childIndex++){
           View child = getChildAt(childIndex);
            // pass in at most to give the pieces different spacing
           int childWidthMeasureSpec = 50 | MeasureSpec.EXACTLY;
           int childHeightMeasureSpec = 50 | MeasureSpec.EXACTLY;
           child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }

        int width = Math.max(widthSpec, getSuggestedMinimumWidth());
        int height = Math.max(heightSpec,getSuggestedMinimumHeight() );

        // TODO: Respect padding
        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec,  0));
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // see what measure the children came up with
        int childWidthMax = 0;
        int childHeightMax = 0;
        for(int childIndex = 0; childIndex < getChildCount(); childIndex++){
            View child = getChildAt(childIndex);
            childWidthMax= Math.max(childWidthMax, child.getMeasuredWidth());
            childHeightMax= Math.max(childHeightMax, child.getMeasuredWidth());
            //check for removed children
        }

        childWidthMax = childWidthMax/2;
        childHeightMax = childHeightMax/2;

        //encompassing rect
        Rect layoutRect = new Rect();
        layoutRect.left = getPaddingLeft() + childWidthMax;// lefthand side + move half the width of the child
        layoutRect.top = getPaddingTop() + childWidthMax;
        layoutRect.right = getWidth() - getPaddingRight() - childHeightMax;
        layoutRect.bottom = getHeight() - getPaddingBottom() - childHeightMax;

        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            double angle = (double) childIndex / (double)getChildCount() * 2.0 * Math.PI;
            int childCenterX = (int) (layoutRect.centerX() + layoutRect.width() *  Math.cos(angle) * 0.4);
            int childCenterY = (int) (layoutRect.centerY() + layoutRect.height() *  Math.sin(angle) * 0.4);


            View child = getChildAt(childIndex);
            if(child.getVisibility() == GONE)
                child.layout(0,0,0,0);
            else{
                Rect childLayout = new Rect();
                //coords in parents system
                childLayout.left = childCenterX - childWidthMax ;
                childLayout.right = childCenterX + childWidthMax ;
                childLayout.top = childCenterY - childHeightMax ;
                childLayout.bottom = childCenterY + childHeightMax ;
                child.layout(childLayout.left, childLayout.top, childLayout.right, childLayout.bottom);
            }

        }

    }

    /**
     * mixes color and color2
     * @param color
     * @param color2
     */
    protected void addColor(int color, int color2){
        int r1, g1, b1, r2, g2, b2, r3, g3, b3;

        r1 = Color.red(color);
        g1 = Color.green(color);
        b1 = Color.blue(color);
        r2 = Color.red(color2);
        g2 = Color.green(color2);
        b2 = Color.blue(color2);

        r3 = (r1+r2)/2;
        g3 = (g1+g2)/2;
        b3 = (b1+b2)/2;

        PaintView pv = new PaintView(PaintApplication.getAppContext());
        int aColor = Color.rgb(r3,g3,b3);
        //store for rotation
        PaintApplication.add_paintColor(aColor);
        pv.setColor(aColor);
        pv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(android.R.drawable.list_selector_background);
                int selected =((PaintView) v).getColor();
                PaintApplication.set_selectedPaint(selected);
            }
        });

        addView(pv);
        invalidate();
    }

    protected void addColor(int color){
        removeView(_transformedView);
        PaintView pv = new PaintView(PaintApplication.getAppContext());
        //store for rotation
        PaintApplication.add_paintColor(color);
        pv.setColor(color);
        pv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(android.R.drawable.list_selector_background);
                int selected =((PaintView) v).getColor();
                PaintApplication.set_selectedPaint(selected);
            }
        });

        addView(pv);
        invalidate();
    }


    /**
     * determines where points x, y are in the radius of the palette
     * @param x
     * @param y
     * @return
     */
    protected boolean inCircle(Float x, Float y){
        Float cx = _contentRect.centerX();
        Float cy = _contentRect.centerY();
        double rad = _radius+(getWidth()/4);

//        distance formula
        double xDist = Math.pow((x - cx), 2);
        double yDist = Math.pow((y - cy), 2);
        double dist = Math.sqrt(xDist + yDist);
        if(dist > rad){
            return false;
        }
        else
            return true;

    }

    /**
     * Removes color
     * @param color
     */
    protected void removeColor(int color){
        //Remove from PaintApplication
        ArrayList<Integer> colors = PaintApplication.get_paintColors();
        ArrayList<Integer> updateColors = new ArrayList<Integer>();
        if(colors.size() > 3){
            for(int i = 0; i < colors.size(); i++){
                if(!colors.get(i).equals(color)){
                    updateColors.add(colors.get(i));
                }
            }
            PaintApplication.set_paintColors(updateColors);
            PaintView pv = null;
            for(int i = 0; i < getChildCount(); i++){
                PaintView current = (PaintView) getChildAt(i);
                if(current.getColor() == color){
                    pv = current;
                }
            }
            removeView(pv);
            invalidate();
        }
        else{
            Toast.makeText(getContext(), "You must keep at least three paints on the palette", Toast.LENGTH_SHORT).show();
        }

    }


}
