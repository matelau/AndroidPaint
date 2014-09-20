package edu.utah.cs4962.paint;

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
    PaintView _transformedView;

    public PaletteView(Context context){
        super(context);
        setWillNotDraw(false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        Log.i("PaletteView", "onInterceptTouchEvent - source: "+ event.getSource());
        Float x = event.getX();
        Float y = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _transformedView = detectPaintTouched(x,y);
            return false;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            //TODO take account for swipes not on a paintView
            //take control in onTouchEvent
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
        Log.i("PaletteView", "onTouchEvent");
        Float x = event.getX();
        Float y = event.getY();


        if(_transformedView != null && (event.getAction() == MotionEvent.ACTION_UP )){
            // check if dragged on top of another paint
            PaintView pv = detectPaintTouched(x,y);
            if(pv != null){
                int color1 = pv.getColor();
                int color2 = _transformedView.getColor();
                if(pv.getColor() != _transformedView.getColor()){
                    //add color
                    addColor(color1, color2);
                    //remove pv and transformed
                    Log.i("PaletteView", "onTouchEvent - Need to Add color");
                    return true;
                }
                else{

                    return false;
                }

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
        float _radius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);
        PointF center = new PointF(_contentRect.centerX(), _contentRect.centerY());
        canvas.drawOval(_contentRect, palPaint);


        // remove corner
        _contentRect = new RectF(50,50,50,50);
        Paint cornerPaint = new Paint();
        cornerPaint.setColor(Color.BLACK);
        cornerPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawCircle(center.x+(_radius/1.3f), center.y-(_radius/(1.3f)),_radius/4, cornerPaint);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        //TODO: implement iterate through all the children then report how big you need to be
        // getChildAt() call measure, getChildCount()

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
     *
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




        Toast t = Toast.makeText(PaintApplication.getAppContext(), "color1: "+color+ " color2: "+color2, Toast.LENGTH_SHORT);
        t.show();
//        onLayout(true);
        invalidate();
    }

    protected void removeColor(Color color){
        //TODO: implement
    }


}
