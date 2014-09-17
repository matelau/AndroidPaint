package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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

    //TODO: Look into Application.class to save state.

    public PaletteView(Context context){
        super(context);
    }

//    @Override
//    protected void onDraw(Canvas canvas){
//        //TODO: draw palette add paints
//        _contentRect = new RectF(
//                getPaddingLeft(),
//                getPaddingTop(),
//                (float) getWidth() - getPaddingRight(),
//                (float) getHeight() - getPaddingBottom());
//
//        Paint palPaint = new Paint();
//        palPaint.setColor(0xff8b4513); // brown
//        palPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        float _radius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);
//        PointF center = new PointF(_contentRect.centerX(), _contentRect.centerY());
//        canvas.drawCircle(center.x, center.y, _radius, palPaint);
//
//    }

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
        //getLeft() == l
        Log.i("layout", "l:" + l + " getLeft(): " + getLeft());

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
            int childCenterX = (int) (layoutRect.centerX() + layoutRect.width() *  Math.cos(angle) * 0.5);
            int childCenterY = (int) (layoutRect.centerY() + layoutRect.height() *  Math.sin(angle) * 0.5);


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

    protected void addColor(int color){
        //TODO: implement
//        onLayout(true);
        invalidate();
    }

    protected void removeColor(Color color){
        //TODO: implement
    }


}
