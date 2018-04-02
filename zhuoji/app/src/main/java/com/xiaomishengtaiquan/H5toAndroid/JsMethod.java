package com.xiaomishengtaiquan.H5toAndroid;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.xiaomishengtaiquan.Application.ZApplication;
import com.xiaomishengtaiquan.Model.Response;
import com.xiaomishengtaiquan.Utils.PrefManager;
import com.xiaomishengtaiquan.Utils.ToastUtilsAll;
import com.xiaomishengtaiquan.Utils.XutilsHttp;
import com.xiaomishengtaiquan.interfaceCallback.OkhttpCallBack;

/**
 * Created by chenda on 2018/4/2.
 */

public class JsMethod {

        private Context context;
        public JsMethod(Context context)
        {
                this.context=context;
        }
        // 1、 添加系统常规配置
        @JavascriptInterface
        public void logout() {
                XutilsHttp.postEncodedLogout(new OkhttpCallBack() {
                        @Override
                        public void OnSuccess(String json) {
                                Response response =new Response();
                                response = ZApplication.gson.fromJson(json, Response.class);
                                if (response.getCode().equals(XutilsHttp.SUCESS)) {
                                        ToastUtilsAll.alertDefine(context,"登出成功！");
                                        //把token设置为空
                                        PrefManager.saveData(context, "");
                                }
                        }
                        @Override
                        public void OnFaild(String faildM) {

                        }
                }, PrefManager.getData_token(context));
        }
}
