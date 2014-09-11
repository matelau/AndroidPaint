package edu.utah.cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by matelau on 9/3/14.
 */
public class PaintView extends View {

    int _color;
    RectF _contentRect;
    float _radius;
    OnSplotchTouchListener _onSplotchTouchListener = null;

    public interface OnSplotchTouchListener
    {
        public void onSplotchTouched(PaintView v);
    }

    public PaintView(Context context) {
        super(context);
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
            Log.i("Splotch", "Touch Inside Splotch!");
            if (_onSplotchTouchListener != null)
                _onSplotchTouchListener.onSplotchTouched(this);
        }
        else
            Log.i("Paint_view", "Touch not in the circle");

        return super.onTouchEvent(event);
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
        int pointCount = 100;
        for (int pointIndex = 0; pointIndex < pointCount; pointIndex++)
        {
            PointF point = new PointF();
//            float randomRadius = ((0.8f * (float) Math.random()* 0.2f) * radius);
            _radius += (Math.random() - 0.5f) * 20.0f;
            point.x = center.x + _radius* (float) Math.cos((float)pointIndex / ((float) pointCount) * 2* Math.PI);
            point.y = center.y + _radius * (float) Math.sin((float) pointIndex / ((float) pointCount) *2*  Math.PI);

            if (pointIndex == 0)
                blotPath.moveTo(point.x, point.y);
            else
                blotPath.lineTo(point.x, point.y);
        }

        Paint blotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blotPaint.setColor(_color);
        canvas.drawPath(blotPath, blotPaint);

    }
}