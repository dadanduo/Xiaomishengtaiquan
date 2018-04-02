package com.xiaomishengtaiquan.Utils;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by chenda on 2018/4/2.
 */

public class TranslateUtils {

    //上下动画时控件显示的动画
    public static TranslateAnimation moveToViewShow() {
        //控件显示的动画
        TranslateAnimation mShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF
                , -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAnim.setDuration(500);
        return mShowAnim;
    }

    //上下动画时控件隐藏的动画
    public static TranslateAnimation moveToViewHide() {
        //控件隐藏的动画
        TranslateAnimation HiddenAmin = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF
                , 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        HiddenAmin.setDuration(500);
        return HiddenAmin;
    }
}
