/**
 * Wire
 * Copyright (C) 2018 Wire Swiss GmbH
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.xiaomishengtaiquan.Utils;

import com.xiaomishengtaiquan.Application.ZApplication;
import com.xiaomishengtaiquan.Model.Register;
import com.xiaomishengtaiquan.interfaceCallback.OkhttpCallBack;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chenda on 2018/1/23.
 */

public class UtilsOkHttpAll {

    ////////////////////////////////////////////////接口区域///////////////////////////////////////////
    //登录接口172.16.1.250   172.16.0.78
    public static final String HTTP = "http://172.16.1.250:8080";
    //注册接口TEST
    public static  final String REGISTERTEST="/register";
    //注册接口
    public static  final String REGISTER="/register";
    //设置密码接口
    public static  final  String SETPASSWORD="/setPassword";
    //登出
    public static  final  String LOGOUT="/logout";
    //获取短信验证码接口
    public static  final  String GETSMS="/sendRegisterSmsCode";

    /////////////////////////////////////////////////////////////////////////////////////////////////

    public static String judgeCode(String code)
    {
        switch (code) {
            case "C001T001":
                return "参数异常";
            case "C001T002":
                return "未知操作类型";
            case "C001B001":
                return "用户已注册";
            case "C001B002":
                return "注册失败";
            case "C001B003":
                return "用户未注册";
            case "C001B004":
                return "短信验证码错误";
            case "C001B005":
                return "密码错误";
            case "C001B006":
                return "登录失败";
            case "C001B007":
                return "修改密码失败";
            case "C001B008":
                return "密码不一致";
            case "FFFFFFFF":
                return "系统异常，请稍后重试";
            default:
        }
        return "null";

    }
    /**
     * POST提交表单数据
     *
     * @param url
     */
    public static void sendPostRegister(final OkhttpCallBack okhttpCallBack, String url,String mobilePhone,String smsCode,String inviteCode,String channel,String product) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("mobilePhone",mobilePhone);
        formBody.add("smsCode",smsCode);
        formBody.add("inviteCode",inviteCode);
        formBody.add("channel",channel);
        formBody.add("product",product);
        Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .post(formBody.build())//传递请求体
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okhttpCallBack.OnFaild(e.getMessage()+"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();
                if (response.isSuccessful()) {
                    LogAll.print(">>>>>>>>>>>" + r, 0);
                    okhttpCallBack.OnSuccess(r);
                } else {
                    LogAll.print(">>>>>>>>>>>失败" + r, 0);
                    okhttpCallBack.OnFaild("not have net");
                }
            }
        });
    }
    /**
     * POST提交Json数据
     *
     * @param url
     */
    public static void postJsonRegister(final OkhttpCallBack okhttpCallBack, String url, Register register) {
        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        String json = ZApplication.gson.toJson(register).toString();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okhttpCallBack.OnFaild(e.getMessage()+"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();
                if (response.isSuccessful()) {
                    LogAll.print(">>>>>>>>>>>" + r, 0);
                    okhttpCallBack.OnSuccess(r);
                } else {
                    LogAll.print(">>>>>>>>>>>失败" + r, 0);
                    okhttpCallBack.OnFaild("not have net");
                }
            }
        });
    }
}
