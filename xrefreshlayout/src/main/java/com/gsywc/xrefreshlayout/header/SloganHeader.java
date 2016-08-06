package com.gsywc.xrefreshlayout.header;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gsywc.xrefreshlayout.R;
import com.gsywc.xrefreshlayout.TurnAroundImageView;
import com.gsywc.xrefreshlayout.util.XRefreshLayoutConfig;
import com.gsywc.xrefreshlayout.model.HeaderState;
import com.gsywc.xrefreshlayout.model.IHeaderCallBack;

/**
 * 快钱Slogan header
 * Created by chao.wang on 2016/8/4.
 */
public class SloganHeader extends RelativeLayout implements IHeaderCallBack {
    private TurnAroundImageView mTurnAroundImageView; //轮转动画控件
    private ImageView iv_logo; //logo图
    private TextView tvRefreshTip;
    private ObjectAnimator mObjectAnimator;

    public SloganHeader(Context context) {
        super(context);
        initView(context);
    }

    public SloganHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.slogan_header, this);
        mTurnAroundImageView = (TurnAroundImageView)findViewById(R.id.iv_loading_circle);
        tvRefreshTip = (TextView)findViewById(R.id.tv_refresh_tip);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        int measureSpec = View.MeasureSpec.makeMeasureSpec(600, View.MeasureSpec.AT_MOST); //预先测量
        tvRefreshTip.measure(measureSpec, measureSpec);
        tvRefreshTip.getLayoutParams().width = tvRefreshTip.getMeasuredWidth();
        initAnimator();
        initLogo();
    }

    private void initLogo(){
        if(XRefreshLayoutConfig.getmLogoDrawable() != null){
            iv_logo.setImageDrawable(XRefreshLayoutConfig.getmLogoDrawable());
        }else
        if(!TextUtils.isEmpty(XRefreshLayoutConfig.getHeaderLogoUrl())){
            //TODO 后续可能会集成轻量级网络加载图片的代码
        }
    }

    @Override
    public void onStateChange(HeaderState headerState) {
        switch (headerState){
            case NOMAL:
                onStateNormal();
                break;
            case READY:
                onStateReady();
                break;
            case FRESHING:
                onStateRefreshing();
                break;
            case COMPLETE:
                onStateFinish();
                break;
        }
    }

    @Override
    public int getHeaderHeight() {
        return getMeasuredHeight();
    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {

    }

    @Override
    public void onHeaderMove(double offset, float percent) {
        mTurnAroundImageView.setProgress(percent);
    }

    @Override
    public void onStateFinish() {
        mTurnAroundImageView.stopTurnAround(); //结束刷新
        if(mObjectAnimator == null){
            initAnimator();
        }
        mTurnAroundImageView.clearAnimation();
        mObjectAnimator.cancel();
        mObjectAnimator.start();
        tvRefreshTip.setText(R.string.header_refresh_complete);
    }

    @Override
    public void onStateRefreshing() {
        mTurnAroundImageView.clearAnimation();
        tvRefreshTip.setText(R.string.header_refresh_loading);
        mTurnAroundImageView.setVisibility(View.VISIBLE);
        mTurnAroundImageView.setAlpha(1.0f);
        mTurnAroundImageView.startTurnAround(); //开始刷新动画
        mTurnAroundImageView.setImageDrawable(null);
    }

    @Override
    public void onStateReady() {
        tvRefreshTip.setText(R.string.header_refresh_release);
        mTurnAroundImageView.setAlpha(1.0f);
        mTurnAroundImageView.setImageDrawable(getResources().getDrawable(R.drawable.loading_refresh));
    }

    @Override
    public void onStateNormal() {
        tvRefreshTip.setText(R.string.header_refresh_pull);
        mTurnAroundImageView.setAlpha(1.0f);
        mTurnAroundImageView.setVisibility(View.VISIBLE);
        mTurnAroundImageView.setImageDrawable(getResources().getDrawable(R.drawable.loading_refresh));
    }

    public SloganHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    private void initAnimator(){
        mObjectAnimator = ObjectAnimator.ofFloat(mTurnAroundImageView, "alpha", 1, 0).setDuration(500);
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //mTurnAroundImageView.setVisibility(View.INVISIBLE);
                mTurnAroundImageView.setAlpha(1.0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //mTurnAroundImageView.setVisibility(View.INVISIBLE);
                mTurnAroundImageView.setAlpha(1.0f);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
