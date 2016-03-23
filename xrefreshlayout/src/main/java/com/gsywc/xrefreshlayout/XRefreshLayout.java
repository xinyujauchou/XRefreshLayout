package com.gsywc.xrefreshlayout;

import android.content.Context;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.gsywc.xrefreshlayout.model.HeaderState;
import com.gsywc.xrefreshlayout.model.IFooterCallBack;
import com.gsywc.xrefreshlayout.model.IHeaderCallBack;

/**
 * 具备 刷新/加载更多 功能的布局， 直接嵌套在 根布局 使用<br>
 *     刷新 和 分页加载功能  默认关闭, 可通过{@link #setPullRefreshEnable(boolean)}/{@link #setPullLoadMoreEnable(boolean)}设置<br>
 *     如果想接受 刷新/加载 事件, 必须 传递 {@link RefreshListener}实例 来接收回调<br>
 *     有默认样式的header 以及 footer, 也支持自定义 Header by{@link #setHeaderView(ViewGroup)}<br>
 *     设置自定义footer by {@link #setFooterView(ViewGroup)}<br>
 *
 * Created by chao.wang on 2016/2/29.
 */
public class XRefreshLayout extends LinearLayout{
    private final static String TAG = XRefreshLayout.class.getSimpleName();
    private final static String KEY_MAP_STATE_HEADER = "headerStAteKey"; //header 状态值 key
    private final static String KEY_MAP_STATE_FOOTER = "footerStateKey"; //footer 状态值 key
    private final static int FAST_ANIMATION_DURATION = 200;  //控件复位动画
    private final static int RESET_ANIMATION_DURATION = 300;  //控件复位动画
    private final static float DEFAULT_MOVE_FRICTION = 0.5f; //阻力系数
    private final static long DEFAULT_OVER_STAY_DELAY = 1000; //状态停留延时

    private Scroller mScroller;               //滚动辅助
    private RefreshListener mRefreshListener; //监听刷新控件的动作:刷新/加载更多
    private IHeaderCallBack mHeaderCallback;  //header 回调
    private IFooterCallBack mFooterCallback;  //footer 回调

    private ViewGroup mHeaderView;  //just headView
    private ViewGroup mFooterView;  //just footerView
    /** 程序运行信息 **/
    private float lastX;    //上次X坐标
    private float lastY;    //上次Y坐标
    private int deltaY;     //Y轴上的偏移量
    private int mTouchSlop; //最小滚动阙值
    private boolean isTouching = false;
    private boolean hasIntercepted = false; //是否拦截child事件
    private ArrayMap<String, HeaderState> stateArrayMap= new ArrayMap<>(); //存储细分状态值
    private HeaderState mHeadState = HeaderState.NOMAL; //Header 和 Footer 状态
    private int mMoveY = 0; //纵向移动的距离,  负数表示向上移动(加载更多), 反之 正数则表示向下移动(下拉刷新)
    /** 用户配置信息 **/
    private boolean isRefreshEnable = true;  //是否允许下拉刷新
    private boolean isLoadMoreEnable = false; //是否允许上拉加载更多

    private boolean hasLoadOver; //数据是否已经全部加载完毕
    private long mOverStayDelay = DEFAULT_OVER_STAY_DELAY; //加载完毕后停留时间


    public XRefreshLayout(Context context) {
        super(context);
        init();
    }

    public XRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        Log.d(TAG, TAG);
        setOrientation(VERTICAL); //设置线性布局的排列方向
        mScroller = new Scroller(getContext(), new LinearInterpolator()); //初始化线性滚轮
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addHeaderAndFooter(); //添加header和Footer
                removeViewTreeObserver(this);
            }
        });
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    public void removeViewTreeObserver(ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    /** 添加header和Footer **/
    private void addHeaderAndFooter(){
        if(mHeaderView == null){
            mHeaderView = new XRefreshViewHeader(getContext());
        }
        if(mFooterView == null){
            mFooterView = new XRefreshViewFooter(getContext());
        }
        mHeaderCallback = (IHeaderCallBack)mHeaderView;
        mFooterCallback = (IFooterCallBack)mFooterView;
        addView(mHeaderView, 0);
        addView(mFooterView);
        mHeaderView.setVisibility(isRefreshEnable ? View.VISIBLE : View.GONE);
        mFooterView.setVisibility(isLoadMoreEnable ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int parentHieght = 0;
        for(int index = 0; index < childCount; index++){
            View child = getChildAt(index);
            if(child.getVisibility() == View.GONE){ //过滤不可见的child
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            parentHieght += child.getMeasuredHeight();
        }
        setMeasuredDimension(parentWidth, parentHieght);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int top = getPaddingTop() + mMoveY;
        int childCount = getChildCount();
        for(int index = 0; index < childCount; index++){
            View child = getChildAt(index);
            if(child.getVisibility() == View.GONE){ //过滤不可见的child
                continue;
            }
            int childHeight = child.getMeasuredHeight();
            if(index == 0){ //header
                child.layout(0, top - childHeight, r, top);
            }else if(index == 1){ //contentView
                child.layout(0, top, r, top + childHeight);
                top += childHeight;
            }else if(index == 2){ //footer
                child.layout(0, top, r, top + childHeight);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                updateCoordinate(ev.getRawX(), ev.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = ev.getRawX();
                float currentY = ev.getRawY();
                deltaY = (int)(currentY - lastY); //偏移量
                boolean need = needInterceptEvent(currentX, currentY);
                if(need){ //是否拦截child事件
                    if(mHeadState != HeaderState.FRESHING){
                        updateLayout((int) (deltaY * DEFAULT_MOVE_FRICTION));
                    }
//                    if(!hasIntercepted){
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.dispatchTouchEvent(ev);
//                    }
                    hasIntercepted = true;
                    return true;
                }
                if(hasIntercepted){
                    hasIntercepted = false;
                    ev.setAction(MotionEvent.ACTION_DOWN);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(mHeadState != HeaderState.FRESHING){
                    releaseFresh();
                }
                hasIntercepted = isTouching = false;
                deltaY = 0;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否需要拦截Child事件
     * @param currentX 当前X坐标
     * @param currentY 当前Y坐标
     * @return 是否需要拦截事件
     */
    private boolean needInterceptEvent(float currentX, float currentY){
        if(Math.abs(deltaY) < Math.abs(currentX - lastX) - 3 && mHeadState != HeaderState.READY){
            updateCoordinate(currentX, currentY);
            return false;
        }
        if(deltaY > 0 && (mMoveY + deltaY * DEFAULT_MOVE_FRICTION) > 0 && canChildScrollDown()){ //处理边界
            deltaY = (int)((0 - mMoveY)/DEFAULT_MOVE_FRICTION);
        }

        if(deltaY < 0 && (mMoveY + deltaY * DEFAULT_MOVE_FRICTION) < 0 && canChildScrollUp()){   //处理边界
            deltaY = (int)((0 - mMoveY)/DEFAULT_MOVE_FRICTION);
        }

        updateCoordinate(currentX, currentY);
        return mMoveY != 0 || (deltaY > 0 && !canChildScrollDown()) || (deltaY < 0 && !canChildScrollUp());
    }

    /**
     * 更新上次坐标
     * @param x 本次X坐标
     * @param y 本次Y坐标
     */
    private void updateCoordinate(float x, float y){
        lastX = x;
        lastY = y;
    }

    /**
     * 更新layout位置
     * @param dy 偏移量
     */
    private void updateLayout(int dy){
        mMoveY += dy;  //更新总偏移量
        notifyHeaderAndFooterPosition();
        requestLayout();
    }

    /**
     * 记录Header 和  Footer 位置变化
     */
    private void notifyHeaderAndFooterPosition(){
        float denominator = mMoveY >= 0 ? mHeaderView.getMeasuredHeight() : mFooterView.getMeasuredHeight();
        float percent = Math.abs(mMoveY) / denominator;
        if(mMoveY >= 0 && mHeaderCallback != null && percent <= 1){
            mHeaderCallback.onHeaderMove(Math.abs(mMoveY), percent);
        }
        if(mHeadState == HeaderState.FRESHING || (getFooterState() == HeaderState.OVER && mMoveY <= 0)){

            return;
        }
        if(percent >= 1.0){
            updateHeaderORFooter(HeaderState.READY);
        }else if(mHeadState == HeaderState.READY || isTouching){
            updateHeaderORFooter(HeaderState.NOMAL);
        }
    }

    /**
     * 松手触发 刷新/回弹 操作
     */
    public void releaseFresh(){
        int destHeight = mMoveY >= 0 ? mHeaderView.getMeasuredHeight() : -mFooterView.getMeasuredHeight();

        if((mHeadState != HeaderState.FRESHING && mHeadState != HeaderState.COMPLETE && mRefreshListener != null) &&
                ((mMoveY > 0 && isRefreshEnable && mMoveY >= destHeight)
              || (mMoveY < 0 && isLoadMoreEnable && !hasLoadOver && mMoveY <= destHeight))){
            updateHeaderORFooter(HeaderState.FRESHING); //开始刷新
            if(mMoveY > 0){
                mRefreshListener.onRefresh();
            }else{
                mRefreshListener.onLoadMore();
            }
        }

        if((mMoveY > 0 && (!isRefreshEnable || mMoveY <= destHeight || getHeaderState() == HeaderState.COMPLETE))
               || (mMoveY < 0 && (!isLoadMoreEnable || mMoveY >= destHeight || getFooterState() == HeaderState.COMPLETE
                            || getFooterState() == HeaderState.OVER))){
            startScroll(0 - mMoveY, RESET_ANIMATION_DURATION); //执行复位动画
        }else
        if((mMoveY > 0 && mMoveY >= destHeight)
                                || (mMoveY < 0 && mMoveY <= destHeight)){
            startScroll(0 - (mMoveY - destHeight), RESET_ANIMATION_DURATION); //执行复位动画
        }
    }

    /**
     * 更新Header 和 Footer 的状态
     * @param headerState 当前状态
     */
    private void updateHeaderORFooter(HeaderState headerState){
        mHeadState = headerState;
        if(mMoveY > 0 || (mMoveY == 0 && deltaY > 0)){
            updateHeaderState(headerState);
        }else {
            updateFooterState(headerState);
        }
    }

    private void updateHeaderState(HeaderState headerState){
        if(!isRefreshEnable || headerState == getHeaderState() || mHeaderCallback == null){
            return;
        }
        stateArrayMap.put(KEY_MAP_STATE_HEADER, headerState);
        mHeaderCallback.onStateChange(headerState);
    }

    private void updateFooterState(HeaderState headerState){
        if(!isLoadMoreEnable || headerState == getFooterState() || mFooterCallback == null){
            return;
        }
        stateArrayMap.put(KEY_MAP_STATE_FOOTER, headerState);
        mFooterCallback.onStateChange(headerState);
    }

    private HeaderState getHeaderState(){
        return getStateByKey(KEY_MAP_STATE_HEADER);
    }

    private HeaderState getFooterState(){
        return getStateByKey(KEY_MAP_STATE_FOOTER);
    }

    private HeaderState getStateByKey(String mapKey){
        HeaderState headerState = stateArrayMap.get(mapKey);
        if(headerState == null){
            headerState = HeaderState.NOMAL;
        }
        return headerState;
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            int mScrollDy = mScroller.getCurrY() - mMoveY;
            updateLayout(mScrollDy);
        }
//        else{
//            //TODO 后面添加
//        }
    }

    /**
     * @param offsetY  滑动偏移量，负数向上滑，正数反之
     * @param duration 滑动持续时间
     */
    private void startScroll(int offsetY, int duration) {
        if(Math.abs(offsetY) < 40){ //如果距离较短  则快读复位
            duration = FAST_ANIMATION_DURATION;
        }
        if (offsetY != 0) {
            mScroller.startScroll(0, mMoveY, 0, offsetY, duration);
            invalidate();
        }
    }

    private boolean canChildScrollDown(){
        if(getChildCount() <= 1){
            return true;
        }
        View child = getChildAt(1);
        if(child instanceof AbsListView){
            AbsListView absListView = (AbsListView) child;
            return (absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0
                    || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()))
                    || child.canScrollVertically(-1);
        }
        return child.getScrollY() > 0 || child.canScrollVertically(-1);
    }

    public boolean canChildScrollUp() {
        if(getChildCount() <= 1){
            return true;
        }
        View child = getChildAt(1);
        if (child instanceof AbsListView) {
            AbsListView absListView = (AbsListView) child;
            return child.canScrollVertically(1)
//                    || absListView.getLastVisiblePosition() != mTotalItemCount - 1
                    ;
        } else if (child instanceof WebView) {
            WebView webview = (WebView) child;
            return child.canScrollVertically(1)
                    || webview.getContentHeight() * webview.getScale() != webview.getHeight() + webview.getScrollY();
        } else if (child instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) child;
            View childView = scrollView.getChildAt(0);
            if (childView != null) {
                return child.canScrollVertically(1)
                        || scrollView.getScrollY() != childView.getHeight()
                        - scrollView.getHeight();
            }
        } else {
            return child.canScrollVertically(1);
        }
        return true;
    }

    /**
     * 设置监听事件{@link RefreshListener}
     * @param refreshListener 监听的事件
     */
    public XRefreshLayout seteRreshListener(RefreshListener refreshListener){
        this.mRefreshListener = refreshListener;
        return this;
    }

    /**
     * 实现该接口实时接收刷新,加载事件<br>
     * {@link #onRefresh()} 接收刷新事件<br>
     * {@link #onLoadMore()} 接收加载更多事件
     */
    public interface RefreshListener{
        /** 触发'刷新'机制的时候会回调此方法 **/
        void onRefresh();

        /** 触发'加载更多'机制的时候会回调此方法 **/
        void onLoadMore();
    }

    /**
     * 设置刷新状态
     * @param isRefreshing 刷新状态  true : 转换到刷新状态， false ： 转换到普通状态
     */
    public void setRefreshing(boolean isRefreshing){
        if(isRefreshing && mHeadState != HeaderState.FRESHING){
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    startScroll(mHeaderView.getMeasuredHeight() + 100, FAST_ANIMATION_DURATION * 2);
                }
            }, 300);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    releaseFresh();
                }
            },300 + FAST_ANIMATION_DURATION * 2);
        }else{
            updateHeaderORFooter(HeaderState.COMPLETE);
            releaseFresh();
        }
    }

    public boolean isRefreshing(){
        return getHeaderState() == HeaderState.FRESHING;
    }

    public void setLoadingMore(boolean isLoadingMore){
        if(isLoadingMore){

        }else{
            updateHeaderORFooter(HeaderState.COMPLETE);
            releaseFresh();
        }
    }

    public boolean isLoadingMore(){
        return getFooterState() == HeaderState.FRESHING;
    }

    /**
     * 数据是否已经加载完毕,数据全部加载完毕后需要设置为true,状态重置时,需要设置为false
     * @param hasLoadOver 数据是否全部加载完毕
     */
    public void setHasLoadOver(boolean hasLoadOver){
        this.hasLoadOver = hasLoadOver;
        if(!hasLoadOver && isLoadMoreEnable){
            mFooterView.setVisibility(View.VISIBLE);
            updateFooterState(HeaderState.NOMAL);
            return;
        }
        updateHeaderORFooter(HeaderState.OVER);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                releaseFresh();
            }
        }, mOverStayDelay);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mFooterView.setVisibility(View.INVISIBLE);
            }
        },mOverStayDelay + FAST_ANIMATION_DURATION + 200);
    }

    /**
     * 设置是否允许下拉刷新
     * @param enable true : 允许刷新操作  false : 不允许下拉操作
     * @return XRefreshLayout
     */
    public XRefreshLayout setPullRefreshEnable(boolean enable){
        this.isRefreshEnable = enable;
        return this;
    }

    /**
     * 设置是否允许上拉加载更多
     * @param enable true : 允许  false : 不允许
     * @return XRefreshLayout
     */
    public XRefreshLayout setPullLoadMoreEnable(boolean enable){
        this.isLoadMoreEnable = enable;
        return this;
    }

    /**
     * 设置全部加载完成以后  提示停留时间
     * @param overStayDelay 停留时间 默认{@link #DEFAULT_OVER_STAY_DELAY}
     * @return XRefreshLayout
     */
    public XRefreshLayout setOverStayDelay(long overStayDelay){
        this.mOverStayDelay = overStayDelay;
        return this;
    }

    /**
     * 设置刷新成功的时间爱你,用于提醒用户上次更新时间
     * @param refreshTime 上次刷新成功时间
     */
    public void setRefreshTime(long refreshTime){
        if(mHeaderCallback != null){
            mHeaderCallback.setRefreshTime(refreshTime);
        }
    }

    /**
     * 设置自定义Header  必须继承{@link IHeaderCallBack}
     * @param headerView 自定义header
     * @return XRefreshLayout
     */
    public XRefreshLayout setHeaderView(ViewGroup headerView){
        if(!(headerView instanceof IHeaderCallBack)){
            throw new IllegalArgumentException("header view must implements interface IHeaderCallBack");
        }
        mHeaderView = headerView;
        mHeaderCallback = (IHeaderCallBack)headerView;
        return this;
    }

    /**
     * footer  必须继承{@link IFooterCallBack}
     * @param footerView footer
     * @return XRefreshLayout
     */
    public XRefreshLayout setFooterView(ViewGroup footerView){
        if(!(footerView instanceof IFooterCallBack)){
            throw new IllegalArgumentException("header view must implements interface IHeaderCallBack");
        }
        mFooterView = footerView;
        mFooterCallback = (IFooterCallBack)mFooterView;
        return this;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

    }
}
