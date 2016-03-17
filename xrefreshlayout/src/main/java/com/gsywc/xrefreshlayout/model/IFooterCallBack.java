package com.gsywc.xrefreshlayout.model;


public interface IFooterCallBack {

	public void onStateChange(HeaderState headerState);
	/**
	 * 正常状态，例如需要点击footerview才能加载更多，主要是到达底部不自动加载更多时会被调用
	 */
	public void onStateReady();
	/**
	 * 正在刷新
	 */
	public void onStateRefreshing();
	/**
	 * 刷新结束
	 */
	public void onStateFinish();
	/**
	 * 已无更多数据
	 */
	public void onStateComplete();
	/**
	 * 隐藏footerview
	 */
	public void hide();
	/**
	 * 显示footerview
	 */
	public void show();
	/**
	 * 获得footerview的高度
	 * @return
	 */
	public int getFooterHeight();
}
