package com.xiaomishengtaiquan.H5toAndroid;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiaomishengtaiquan.Application.ZApplication;

/**
 * Created by chenda on 2018/4/2.
 */

public class Webset {
    public static  void setweb(WebView web, Context context)
    {
        // 设置支持js
        web.getSettings().setJavaScriptEnabled(true);
        // 设置 缓存模式
//        web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.loadUrl(ZApplication.URL);
        // 支持自定义alert
        web.setWebChromeClient(new MyWebChromeClient());
        // ///////////////////////////////////////////让js页面用手机格式打开///////////////////////////////////////
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        // 添加js调用接口类，通过Android这个字段 调用这个类的方法
        web.addJavascriptInterface(new JsMethod(context), "android");
    }
}
