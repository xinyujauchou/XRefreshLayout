package com.gsywc.xrefreshlayout.util;

import android.graphics.drawable.Drawable;

/**
 * XRefreshlayout 全局配置
 * Created by chao.wang on 2016/8/4.
 */
public class XRefreshLayoutConfig {

    private static String headerLogoUrl; //logoUrl
    private static Drawable mLogoDrawable; //logoDrawable

    public XRefreshLayoutConfig logoUrl(String logoUrl){
        XRefreshLayoutConfig.headerLogoUrl = logoUrl;
        return this;
    }

    public XRefreshLayoutConfig logoDrawable(Drawable logoDrawable){
        XRefreshLayoutConfig.mLogoDrawable = logoDrawable;
        return this;
    }

    public XRefreshLayoutConfig build(){
        return this;
    }

    public static void setHeaderLogoUrl(String headerLogoUrl) {
        XRefreshLayoutConfig.headerLogoUrl = headerLogoUrl;
    }

    public static void setmLogoDrawable(Drawable mLogoDrawable) {
        XRefreshLayoutConfig.mLogoDrawable = mLogoDrawable;
    }

    public static String getHeaderLogoUrl() {
        return headerLogoUrl;
    }

    public static Drawable getmLogoDrawable() {
        return mLogoDrawable;
    }
}
