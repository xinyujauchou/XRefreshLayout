package com.gsywc.xrefreshlayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gsywc.xrefreshlayout.model.HeaderState;
import com.gsywc.xrefreshlayout.model.IHeaderCallBack;

import java.util.Calendar;

public class XRefreshViewHeader extends LinearLayout implements IHeaderCallBack {
	private final int ROTATE_ANIM_DURATION = 180;
	private ImageView mArrowImageView;
	private ImageView mOkImageView;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;
	private TextView mHeaderTimeTextView;
	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	private long lastRefrshTime = System.currentTimeMillis();

	private AnimatorSet as;

	public XRefreshViewHeader(Context context) {
		super(context);
		initView(context);
		initRefreshFinishAnimator();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XRefreshViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.xrefreshview_header, this);
		mArrowImageView = (ImageView) findViewById(R.id.xrefreshview_header_arrow);
		mOkImageView = (ImageView) findViewById(R.id.xrefreshview_header_ok);
		mHintTextView = (TextView) findViewById(R.id.xrefreshview_header_hint_textview);
		mHeaderTimeTextView = (TextView) findViewById(R.id.xrefreshview_header_time);
		mProgressBar = (ProgressBar) findViewById(R.id.xrefreshview_header_progressbar);

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	public void setRefreshTime(long lastRefreshTime) {
		lastRefrshTime = lastRefreshTime;
		freshTime();
	}

	private void freshTime(){
		mHeaderTimeTextView.setText(getResources().getString(R.string.header_last_fresh_time,
				Utils.formatFreshTime(lastRefrshTime)));
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		setVisibility(View.GONE);
	}

	public void show() {
		setVisibility(View.VISIBLE);
	}

	@Override
	public void onStateChange(HeaderState headerState) {
		switch (headerState){
			case NOMAL:
				onStateNormal();
				freshTime();
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
	public void onStateNormal() {
		mProgressBar.setVisibility(View.GONE);
		mArrowImageView.setVisibility(View.VISIBLE);
		mOkImageView.setVisibility(View.GONE);
		mArrowImageView.startAnimation(mRotateDownAnim);
		mHintTextView.setText("下拉可以刷新");
		mHeaderTimeTextView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onStateReady() {
		mProgressBar.setVisibility(View.GONE);
		mOkImageView.setVisibility(View.GONE);
		mArrowImageView.setVisibility(View.VISIBLE);
		mArrowImageView.clearAnimation();
		mArrowImageView.startAnimation(mRotateUpAnim);
		mHintTextView.setText("松开立即刷新");
		mHeaderTimeTextView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onStateRefreshing() {
		mArrowImageView.clearAnimation();
		mArrowImageView.setVisibility(View.GONE);
		mOkImageView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
		mHintTextView.setText("正在帮您刷新");

	}

	@Override
	public void onStateFinish() {
		mArrowImageView.setVisibility(View.GONE);
		mOkImageView.setVisibility(View.VISIBLE);
		mHintTextView.setText("刷新完成");
		mHeaderTimeTextView.setVisibility(View.GONE);

		if(as == null){
			initRefreshFinishAnimator();
		}
		as.start();
	}

	private void initRefreshFinishAnimator(){
		ObjectAnimator objectAnimator01 = ObjectAnimator.ofFloat(mProgressBar, "scaleX", 1, 0);
		ObjectAnimator objectAnimator02 = ObjectAnimator.ofFloat(mProgressBar, "scaleY", 1, 0);
		ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mOkImageView, "alpha", 0, 1);
		ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mOkImageView, "scaleX", 0, 1);
		ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(mOkImageView, "scaleY", 0, 1);

		as = new AnimatorSet();
		as.setDuration(400);
		as.playTogether(objectAnimator01,objectAnimator02, objectAnimator2, objectAnimator3, objectAnimator4);
		as.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mProgressBar.setAlpha(1);
				mProgressBar.setScaleX(1);
				mProgressBar.setScaleY(1);
				mProgressBar.setVisibility(GONE);
				mOkImageView.setVisibility(VISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mProgressBar.setAlpha(1);
				mProgressBar.setScaleX(1);
				mProgressBar.setScaleY(1);
				mProgressBar.setVisibility(GONE);
				mOkImageView.setVisibility(VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
	}

	@Override
	public void onHeaderMove(double offset, float percent) {
	}

	@Override
	public int getHeaderHeight() {
		return getMeasuredHeight();
	}
}
