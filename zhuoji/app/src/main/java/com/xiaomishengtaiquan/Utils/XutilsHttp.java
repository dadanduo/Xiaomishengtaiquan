package com.xiaomishengtaiquan.Utils;

import android.app.Dialog;
import android.content.Context;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xiaomishengtaiquan.Application.ZApplication;
import com.xiaomishengtaiquan.R;
import com.xiaomishengtaiquan.Ui.classification.Activity.SingupActivity;
import com.xiaomishengtaiquan.Ui.classification.View.CommomDialog;
import com.xiaomishengtaiquan.interfaceCallback.OkhttpCallBack;


/*
 * 
 * 网络请求类
 * 
 */
public class XutilsHttp {

	public static  String SUCESS="00000000";
	public static  void NotHaveNetwork(Context context)
	{
		//弹出提示框 对对对
		new CommomDialog(context, R.style.dialog, "网络出现了问题了哦！", new CommomDialog.OnCloseListener() {
			@Override
			public void onClick(Dialog dialog, boolean confirm) {
				if(confirm){
					dialog.dismiss();
				}
			}
		}).setTitle("温馨提示").show();
	}
	//注册
	public static void postEncodedRegister(final OkhttpCallBack okhttpCallBack,String mobilePhone, String smsCode, String inviteCode, String channel, String product)
	{
        String url= UtilsOkHttpAll.HTTP+UtilsOkHttpAll.REGISTERTEST+"?mobilePhone="+mobilePhone+"&smsCode="+smsCode+"&inviteCode="+inviteCode+"&channel="+channel+"&product="+product;
		LogAll.print(">>>>>>>"+url,0);
        RequestParams pm = new RequestParams();
		pm.addHeader("Content-Type", "application/x-www-form-urlencoded");
		ZApplication.http.send(HttpRequest.HttpMethod.POST,url,pm,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				LogAll.print("成功："+responseInfo.result.toString(),4);
				okhttpCallBack.OnSuccess(responseInfo.result.toString());
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				okhttpCallBack.OnFaild(msg);
			}
		});
	}
	//设置密码
	public static void postEncodedSetPassword(final OkhttpCallBack okhttpCallBack,String token, String newPassword, String comfirmPassword, String channel, String product)
	{
		LogAll.print(">>>>token:"+token,4);
		String url= UtilsOkHttpAll.HTTP+UtilsOkHttpAll.SETPASSWORD+"?token="+token+"&newPassword="+newPassword+"&comfirmPassword="+comfirmPassword+"&channel="+channel+"&product="+product;
		LogAll.print(">>>>>>>"+url,0);
		RequestParams pm = new RequestParams();
		pm.addHeader("Content-Type", "application/x-www-form-urlencoded");
		ZApplication.http.send(HttpRequest.HttpMethod.POST,url,pm,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				LogAll.print("成功："+responseInfo.result.toString(),4);
				okhttpCallBack.OnSuccess(responseInfo.result.toString());
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				okhttpCallBack.OnFaild(msg);
			}
		});
	}
	//登出
	public static void postEncodedLogout(final OkhttpCallBack okhttpCallBack,String token)
	{
		LogAll.print(">>>>token:"+token,4);
		String url= UtilsOkHttpAll.HTTP+UtilsOkHttpAll.LOGOUT+"?token="+token;
		LogAll.print(">>>>>>>"+url,0);
		RequestParams pm = new RequestParams();
		pm.addHeader("Content-Type", "application/x-www-form-urlencoded");
		ZApplication.http.send(HttpRequest.HttpMethod.POST,url,pm,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				LogAll.print("成功："+responseInfo.result.toString(),4);
				okhttpCallBack.OnSuccess(responseInfo.result.toString());
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				okhttpCallBack.OnFaild(msg);
			}
		});
	}
	//获取短信验证码
	public static void postEncodedGetsms(final OkhttpCallBack okhttpCallBack, String mobilePhone, String channel, String product)
	{
		String url= UtilsOkHttpAll.HTTP+UtilsOkHttpAll.GETSMS+"?&mobilePhone="+mobilePhone+"&channel="+channel+"&product="+product;
		LogAll.print(">>>>>>>"+url,0);
		RequestParams pm = new RequestParams();
		pm.addHeader("Content-Type", "application/x-www-form-urlencoded");
		ZApplication.http.send(HttpRequest.HttpMethod.POST,url,pm,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				LogAll.print("成功："+responseInfo.result.toString(),4);
				okhttpCallBack.OnSuccess(responseInfo.result.toString());
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				okhttpCallBack.OnFaild(msg);
			}
		});
	}
}
