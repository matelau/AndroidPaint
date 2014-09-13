package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
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

    public PaletteView(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        _contentRect = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                (float) getWidth() - getPaddingRight(),
                (float) getHeight() - getPaddingBottom());

        /* TODO: Color Mixing */

//       Paint blotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        blotPaint.setColor(_color);
//        canvas.drawPath(blotPath, blotPaint);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
