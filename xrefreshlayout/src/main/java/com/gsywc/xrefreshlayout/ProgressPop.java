package com.gsywc.xrefreshlayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.gsywc.xrefreshlayout.util.ProgressPopConfig;

/**
 * 进度Pop
 * Created by chao.wang on 2016/8/5.
 */
public class ProgressPop extends PopupWindow{
    private ImageView ivRing; //圆环
    private ObjectAnimator objectAnimator;
    private Runnable mCancelRunnable = new CancelRunnable();

    public ProgressPop(Context context){
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_progress_front, null);
        ivRing = (ImageView) contentView.findViewById(R.id.iv_ring);
        setContentView(contentView);
        initSetting();
        setAnimationStyle(R.style.AnimationPreview);
    }

    private void initSetting(){
        setTouchable(true);
        setOutsideTouchable(true);
        setPoPSize();
        update();
        initAnimator();
        objectAnimator.start();
    }

    private void setPoPSize(){
        int measureSpec = View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.AT_MOST); //预先测量
        ivRing.measure(measureSpec, measureSpec);
        setWidth(ivRing.getMeasuredWidth() + getContentView().getPaddingLeft() * 2);
        setHeight(ivRing.getMeasuredHeight() + getContentView().getPaddingRight() * 2);
    }

    public void show(View root){
        if(!isShowing() && root != null){
            showAtLocation(root, Gravity.CENTER, 0, 0);
            if(objectAnimator == null){
                initAnimator();
            }
            ivRing.removeCallbacks(mCancelRunnable);
            objectAnimator.start();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ivRing.postDelayed(mCancelRunnable, 600);
    }

    public void initAnimator(){
        objectAnimator = ObjectAnimator.ofFloat(ivRing, "rotation", 0f, 360f)
                                       .setDuration(getRingDuration());
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    private int getRingDuration(){
        return ProgressPopConfig.getRingDuration();
    }

    class CancelRunnable implements Runnable{
        @Override
        public void run() {
            if(objectAnimator != null){
                objectAnimator.cancel();
            }
        }
    }
}
