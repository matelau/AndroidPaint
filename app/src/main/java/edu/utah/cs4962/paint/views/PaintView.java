package edu.utah.cs4962.paint.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * A subclass of View that depicts a blob of paint of a particular color. Override
 onDraw to draw the paint blob to fill the view, respecting the padding of the view. Allow the
 paint to be highlighted using an outline or a glow effect by offering a boolean property “active”
 that, when changed, causes the view to redraw itself, either adding or removing the highlight
 effect.
 */
public class PaintView extends View {

    int _color;
    RectF _contentRect;
    float _radius;
    OnSplotchTouchListener _onSplotchTouchListener = null;


    /* Constructor */
    public PaintView(Context context) {
        super(context);
        setMinimumWidth(75);
        setMinimumHeight(75);
    }

    public interface OnSplotchTouchListener
    {
        public void onSplotchTouched(PaintView v);
    }

    public int getColor(){
        return _color;
    }

    public void setColor(int color){
        _color = color;
        invalidate();
    }


    public OnSplotchTouchListener getOnSplotchTouchListener(){
        return _onSplotchTouchListener;
    }
    public void setOnSplotchTouchListener(OnSplotchTouchListener listener){
        _onSplotchTouchListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        float circleCenterX = _contentRect.centerX();
        float circleCenterY = _contentRect.centerY();

        float distance = (float) Math.sqrt((circleCenterX - x) * (circleCenterX- x) + (circleCenterY - y) * (circleCenterY- y));
        if(distance < _radius){

            if (_onSplotchTouchListener != null)
                _onSplotchTouchListener.onSplotchTouched(this);
        }


        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        _contentRect = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                (float) getWidth() - getPaddingRight(),
                (float) getHeight() - getPaddingBottom());

        Path blotPath = new Path();
        PointF center = new PointF(_contentRect.centerX(), _contentRect.centerY());
        _radius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);
        int pointCount = 350;
        for (int pointIndex = 0; pointIndex < pointCount; pointIndex+=4)
        {
            PointF point = new PointF();
            point.x = center.x + _radius * (float) Math.cos((float)pointIndex / ((float) pointCount) * 2* Math.PI);
            point.y = center.y + _radius * (float) Math.sin((float) pointIndex / ((float) pointCount) *2*  Math.PI);

            PointF control1 = new PointF();
            float c1Radius = _radius + (float) ((Math.random() - .5) * 2.0f *20f);
            control1.x = center.x + c1Radius * (float) Math.cos((float)pointIndex / ((float) pointCount) * 2* Math.PI);

            PointF control2 = new PointF();
            float c2Radius = _radius + (float) ((Math.random() - .5) * 2.0f * 20f) ;
            control2.y = center.y + c2Radius * (float) Math.sin((float) pointIndex / ((float) pointCount) *2*  Math.PI);

            if (pointIndex == 0)
                blotPath.moveTo(point.x, point.y);
            else
                blotPath.quadTo(control1.x, control2.y,  point.x, point.y);
        }
        Paint blotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blotPaint.setColor(_color);
        canvas.drawPath(blotPath, blotPaint);

    }
}