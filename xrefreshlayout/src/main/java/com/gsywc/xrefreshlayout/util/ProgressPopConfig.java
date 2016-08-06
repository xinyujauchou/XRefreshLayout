package com.gsywc.xrefreshlayout.util;

/**
 * 进度Pop
 * Created by chao.wang on 2016/8/5.
 */
public class ProgressPopConfig {
    private final static int DEFAULT_RING_DURATION = 600;

    private static int mRingDuration = DEFAULT_RING_DURATION;

    public static int getRingDuration() {
        return mRingDuration;
    }

    public static void setRingDuration(int mRingDuration) {
        if(mRingDuration < 300 || mRingDuration > 900){
            return;
        }
        ProgressPopConfig.mRingDuration = mRingDuration;
    }
}
