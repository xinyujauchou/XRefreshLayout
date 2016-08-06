package com.gsywc.xrefreshlayout.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gsywc.xrefreshlayout.R;
import com.gsywc.xrefreshlayout.model.HeaderState;
import com.gsywc.xrefreshlayout.model.IFooterCallBack;

public class XRefreshViewFooter extends LinearLayout implements IFooterCallBack {
    private Context mContext;

    private View mContentView;
    private View mProgressBar;
    private TextView mHintView;
    private TextView mClickView;

    public XRefreshViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XRefreshViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    public void onStateChange(HeaderState headerState) {
        switch (headerState){
            case NOMAL:
                onStateNomal();
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
            case OVER:
                onStateComplete();
                break;
        }
    }

    private void onStateNomal(){
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setVisibility(View.VISIBLE);
        mHintView.setText("上拉加载更多");
        setVisibility(VISIBLE);
    }

    @Override
    public void onStateReady() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setVisibility(View.VISIBLE);
        mHintView.setText("松手加载更多");
        setVisibility(VISIBLE);
    }

    @Override
    public void onStateRefreshing() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mClickView.setVisibility(View.GONE);
        mHintView.setText("正在加载...");
        setVisibility(VISIBLE);
    }

    @Override
    public void onStateFinish() {
        mHintView.setText("加载完成");
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mClickView.setVisibility(View.GONE);
        setVisibility(VISIBLE);

    }

    @Override
    public void onStateComplete() {
        mHintView.setText("已全部加载");
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        setVisibility(VISIBLE);
    }

    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    public void show() {
        LayoutParams lp = (LayoutParams) mContentView
                .getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);
        mContext = context;
        RelativeLayout moreView = (RelativeLayout) LayoutInflater
                .from(mContext).inflate(R.layout.xrefreshview_footer, null);
        addView(moreView);

        mContentView = moreView.findViewById(R.id.xrefreshview_footer_content);
        mProgressBar = moreView
                .findViewById(R.id.xrefreshview_footer_progressbar);
        mHintView = (TextView) moreView
                .findViewById(R.id.xrefreshview_footer_hint_textview);
        mClickView = (TextView) moreView
                .findViewById(R.id.xrefreshview_footer_click_textview);
//        setVisibility(GONE);
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}
