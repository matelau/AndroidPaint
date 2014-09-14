package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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
    ArrayList<Integer> paints;


    public PaletteView(Context context){
        super(context);
        paints = new ArrayList<Integer>();
        paints.add(Color.RED);
        paints.add(Color.BLUE);
        paints.add(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas){
        //TODO: draw palette add paints
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
        canvas.drawCircle(center.x, center.y, _radius, palPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        //TODO: implement
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // what I think width/height should be
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        //start small and try to grow
        int width = getSuggestedMinimumWidth();
        int height = getSuggestedMinimumHeight();

        if(widthMode == MeasureSpec.AT_MOST)
            width = widthSpec;
        if(heightMode == MeasureSpec.AT_MOST)
            height = heightSpec;

        if( widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSpec;
            height = width;
        }

        if( heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
            width = height;
        }

        //enforce squareness
        if(width > height && widthMode != MeasureSpec.EXACTLY)
            width = height;
        if(height > width && heightMode != MeasureSpec.EXACTLY)
            height = heightSpec;

        // TODO: Respect padding
        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, width < getSuggestedMinimumWidth() ? MEASURED_STATE_TOO_SMALL : 0),
                resolveSizeAndState(height, heightMeasureSpec,  height < getSuggestedMinimumHeight() ? MEASURED_STATE_TOO_SMALL : 0));

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int children = getChildCount(), height = getMeasuredHeight();
        for (int i = 0; i < children; i++) {
            final View view = getChildAt(i);

            if (view.getVisibility() != View.VISIBLE)
                continue;

            final int width = view.getMeasuredWidth();
            view.layout(l, 0, l + width, height);
            l += width;
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
