package edu.utah.cs4962.paint.controllers;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.ArrayList;

import edu.utah.cs4962.paint.models.PaintApplication;
import edu.utah.cs4962.paint.models.PointColorTuple;
import edu.utah.cs4962.paint.views.PaintAreaView;

/**
 *  In watch mode, the menu contains a button to get back to create mode,
 *  a scrub control to change the position in
 *  playback of the painting (see Paint Area View), and a play/pause button that
 *  causes the position of the scrub control to move forward by itself at some suitable interval.
 */
public class WatchActivity extends Activity {
    protected PaintAreaView _paintAreaView;
    protected SeekBar _sb;
    protected ObjectAnimator _seekAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Action Bar
        ActionBar ab = getActionBar();
        Button paintReturn = new Button(this);

        //Return
        paintReturn.setText("Return");
        paintReturn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaintApplication.set_inPaintMode(true);
                _paintAreaView.restore();
                startActivity(new Intent(PaintApplication.getAppContext(), PaintActivity.class));
            }
        });


        //Play Mode
        Button play = new Button(this);
        play.setText("Play");
        Button pause = new Button(this);
        pause.setText("Pause");

        play.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate();
            }
        });
        pause.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                _seekAnimator.cancel();
            }
        });
        //Add to Action Bar
        LinearLayout abll = new LinearLayout(this);
        abll.addView(paintReturn);
        abll.addView(play);
        abll.addView(pause);
        ab.setCustomView(abll);
        ab.setDisplayShowCustomEnabled(true);

        //Paint Area
        LinearLayout rootLayout = new LinearLayout(this);
        _paintAreaView = new PaintAreaView(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.addView(_paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, .8f));

        //Seek Bar
        _sb = new SeekBar(this);
        _sb.setMax(PaintApplication.get_points().size());
        _sb.incrementProgressBy(1);
        _sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

               @Override
               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        _sb.setProgress(progress);
                        _paintAreaView.set_pctAnim(portionOfPts(0, progress));
                    }
               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {

               }

               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {

               }
       });


        rootLayout.addView(_sb);
        setContentView(rootLayout);

    }

    /**
     * Animates the drawing and the seekbar
     */
    public void animate(){
        int ptsNum = PaintApplication.get_points().size();
        _seekAnimator = new ObjectAnimator();
        _seekAnimator.setTarget(_sb);
        _seekAnimator.setIntValues(_sb.getProgress(), ptsNum);
        _seekAnimator.setDuration(ptsNum * 20);
        _seekAnimator.setInterpolator(new LinearInterpolator());
        _seekAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (Integer) animation.getAnimatedValue();
                ArrayList<PointColorTuple> pctAnim =  portionOfPts(0, progress);
                _sb.setProgress(progress);
                _paintAreaView.set_pctAnim(pctAnim);
            }
        });
        _seekAnimator.start();
    }

    /**
     * Returns a portion of points to be animated
     * @param beginning
     * @param end
     * @return
     */
    protected ArrayList<PointColorTuple> portionOfPts(int beginning, int end){
        ArrayList<PointColorTuple> toAnim = new ArrayList<PointColorTuple>();
        if(end >= PaintApplication.get_points().size()){
            end = PaintApplication.get_points().size()-1;
        }
        for(int i = beginning; i < end; i++){
            toAnim.add(PaintApplication.get_points().get(i));
        }
        return toAnim;
    }


}
