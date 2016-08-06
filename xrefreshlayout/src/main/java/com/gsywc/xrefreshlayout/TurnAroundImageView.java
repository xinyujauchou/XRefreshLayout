package com.gsywc.xrefreshlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 刷新TurnAround动画
 * Created by chao.wang on 2016/8/4.
 */
public class TurnAroundImageView extends ImageView implements ValueAnimator.AnimatorUpdateListener{

    private final static int DEFAULT_CIRCLE_COLOR = Color.parseColor("#9a9c9a"); //加载圈默认颜色
    private final static float FULL_ARC_ANGLE = 351.0f;
    private final static float INIT_ARC_START_ANGLE = 270.0f + (360.0f - FULL_ARC_ANGLE) / 2;
    private static int DEFAULT_STROKE_WIDTH = 2; //stoke默认宽度

    private Paint mPaint;                //画笔
    private float mStartAnglePercent = INIT_ARC_START_ANGLE / 360.0f; //开始绘制扇形的位置
    private float mProgressPercent;      //当前进度
    private RectF mRectF = new RectF();  //绘制扇形的区域
    private ValueAnimator valueAnimator; //动画句柄


    public TurnAroundImageView(Context context) {
        super(context);
        init();
    }

    public TurnAroundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        DEFAULT_STROKE_WIDTH = getResources().getDimensionPixelOffset(R.dimen.circle_stroke_width);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_CIRCLE_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initDrawScope();    //初始化扇形绘制范围
        drawCircle(canvas); //画圈
    }

    /**
     * 初始化绘制范围
     */
    private void initDrawScope(){
        if(mRectF.isEmpty()){
            mRectF.set(DEFAULT_STROKE_WIDTH, DEFAULT_STROKE_WIDTH, getMeasuredWidth() - DEFAULT_STROKE_WIDTH, getMeasuredHeight() - DEFAULT_STROKE_WIDTH);
        }
    }

    private float getStartArcAngle(){
        return 360.0f * mStartAnglePercent;
    }

    private float getSweepArcAngle(){
        return FULL_ARC_ANGLE * mProgressPercent;
    }

    private void drawCircle(Canvas canvas){
        canvas.drawArc(mRectF, getStartArcAngle(), getSweepArcAngle(), false, mPaint);
    }

    public void setProgress(float progress){
        this.mProgressPercent = progress;
        invalidate();
    }

    public void startTurnAround(){
        mProgressPercent = mProgressPercent != 1.0 ? 1.0f : mProgressPercent; //检查当前角度的合法性
        valueAnimator = ValueAnimator.ofFloat(mStartAnglePercent, mStartAnglePercent + 1.0f).setDuration(600); //从原始位置开始转动
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(this);
        valueAnimator.start();
    }

    /**
     * 停止转动
     */
    public void stopTurnAround(){
        if(valueAnimator != null){
            valueAnimator.cancel();
        }
        reset(); //重置状态
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if(animation == null){
            return;
        }
        float currentValue = (float)animation.getAnimatedValue();
        mStartAnglePercent = currentValue % 1.0f;
        invalidate();
    }

    /**
     * 重置状态
     */
    public void reset(){
        mStartAnglePercent = INIT_ARC_START_ANGLE / 360.0f;
        mProgressPercent = 0.0f;
    }
}
