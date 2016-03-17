package com.gsywc.xrefreshlayout.model;

/**
 * Header/Footer 状态
 * Created by chao.wang on 2016/3/8.
 */
public enum HeaderState {
    /**
     * 普通状态
     */
    NOMAL,
    /**
     * 已达刷新条件
     */
    READY,
    /**
     * 正在刷新
     */
    FRESHING,
    /**
     * 刷新完成
     */
    COMPLETE,
    /**
     * 全部加载完毕
     */
    OVER
}
