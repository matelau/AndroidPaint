package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;


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

    RectF _contentRect;

    /* Constructor */
    public PaintAreaView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint border = new Paint();
        border.setStrokeWidth(5.5f);
        border.setColor(Color.CYAN);
        border.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), border);
//        canvas.drawRect(_contentRect, border);

    }
}