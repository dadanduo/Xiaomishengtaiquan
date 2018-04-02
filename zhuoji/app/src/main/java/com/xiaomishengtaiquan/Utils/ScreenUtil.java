package com.xiaomishengtaiquan.Utils;

import android.content.Context;
import android.view.WindowManager;

import com.xiaomishengtaiquan.Application.ZApplication;

/**
 * Created by Administrator on 2017/4/25.
 */

public class ScreenUtil {
    public static int sysWidth()
    {
        WindowManager wm = (WindowManager) ZApplication.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width/2;
    }
    public static int sysHeight()
    {
        WindowManager wm = (WindowManager) ZApplication.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height/2;
    }
}
