package com.xiaomishengtaiquan.Ui.classification.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaomishengtaiquan.Application.ZApplication;
import com.xiaomishengtaiquan.MainActivity;
import com.xiaomishengtaiquan.Model.Response;
import com.xiaomishengtaiquan.R;
import com.xiaomishengtaiquan.ScottMap.Utils;
import com.xiaomishengtaiquan.Ui.classification.View.CommomDialog;
import com.xiaomishengtaiquan.Ui.classification.View.EditText_Phone;
import com.xiaomishengtaiquan.Utils.DialogUtils;
import com.xiaomishengtaiquan.Utils.LogAll;
import com.xiaomishengtaiquan.Utils.PrefManager;
import com.xiaomishengtaiquan.Utils.ToastUtilsAll;
import com.xiaomishengtaiquan.Utils.UtilsAll;
import com.xiaomishengtaiquan.Utils.XutilsHttp;
import com.xiaomishengtaiquan.interfaceCallback.OkhttpCallBack;
import com.xiaomishengtaiquan.interfaceCallback.alertCallBack;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by chenda on 2018/3/26.
 * 注册页面
 */

@ContentView(R.layout.signup)
public class SingupActivity extends Activity {
    //手机号码
    @ViewInject(R.id.signup_phone_input)
    EditText_Phone signup_phone_input;
    //验证码
    @ViewInject(R.id.signup_validation_code)
    EditText signup_validation_code;
    //邀请码
    @ViewInject(R.id.invite_code)
    EditText_Phone invite_code;
    //获取验证码
    @ViewInject(R.id.signup_get_code)
    TextView signup_get_code;

    private Dialog showCode;

    private int EumMany_Number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); //绑定注解
    }

    @Event(value = {R.id.back_signup}, type = View.OnClickListener.class)
    private void back_signup(View view) {
        finish();
    }

    //注册按钮
    @Event(value = {R.id.singup_login}, type = View.OnClickListener.class)
    private void singup_login(View view) {
        if (!UtilsAll.isMobileNO(signup_phone_input.getText().toString())) {
            ToastUtilsAll.alertDefine(SingupActivity.this, "请输入正确的手机号码");
        } else if (UtilsAll.isEmpty(signup_phone_input.getText())) {
            ToastUtilsAll.alertDefine(SingupActivity.this, "请输入手机号码");
        } else if (UtilsAll.isEmpty(signup_validation_code.getText())) {
            ToastUtilsAll.alertDefine(SingupActivity.this, "请输入验证码");
        } else {
            showCode = DialogUtils.createLoadingDialog(SingupActivity.this, "注册中请稍后...");
            register_btn();
        }
    }

    //获取验证码
    @Event(value = {R.id.signup_get_code}, type = View.OnClickListener.class)
    private void signupcode(View view) {
        if (!UtilsAll.isMobileNO(signup_phone_input.getText().toString())) {
            ToastUtilsAll.alertDefine(SingupActivity.this, "请输入正确的手机号码");
        } else {

            XutilsHttp.postEncodedGetsms(new OkhttpCallBack() {
                @Override
                public void OnSuccess(String json) {
                    Response response = new Response();
                    response = ZApplication.gson.fromJson(json, Response.class);
                    if (response.getCode().equals(XutilsHttp.SUCESS)) {
                        ToastUtilsAll.alertDefine(SingupActivity.this, "验证码已经发送到你手机请注意查收");
                        startTime();
                    }else{
                        ToastUtilsAll.alertDefine(SingupActivity.this,response.getDescribe());
                    }
                }

                @Override
                public void OnFaild(String faildM) {
                    XutilsHttp.NotHaveNetwork(SingupActivity.this);
                }
            }, signup_phone_input.getText().toString(), "shzj", "xiaomi");

        }
        LogAll.print(">>>>>>>>>>>" + "注册", 4);
    }

    public void register_btn() {
        if (EumMany_Number > 3) {

        } else {
            XutilsHttp.postEncodedRegister(new OkhttpCallBack() {
                @Override
                public void OnSuccess(String json) {
                    LogAll.print(json, 4);
                    Response registerResponse = new Response();
                    registerResponse = ZApplication.gson.fromJson(json, Response.class);
                    //把token存起来
                    PrefManager.saveData(SingupActivity.this, registerResponse.getBody().getContent().getToken());
                    if (registerResponse.getCode().equals(XutilsHttp.SUCESS)) {
                        DialogUtils.closeDialog(showCode);
                      DialogUtils.AlertDilog(SingupActivity.this, "温馨提示", "注册成功是否设置密码", "去设置", "跳过", new alertCallBack() {
                            @Override
                            public void OnOk() {
                                UtilsAll.JumpPager(SingupActivity.this, PasswordActivity.class);
                            }
                            @Override
                            public void OnNo() {
                                UtilsAll.JumpPager(SingupActivity.this, MainActivity.class);
                            }
                        });
                    } else {
                        DialogUtils.closeDialog(showCode);
                        ToastUtilsAll.alertDefine(SingupActivity.this, registerResponse.getDescribe());
                    }
                }
                @Override
                public void OnFaild(String faildM) {
                    LogAll.print(">>>>>>>>>>>" + faildM, 4);
                    DialogUtils.closeDialog(showCode);
                    XutilsHttp.NotHaveNetwork(SingupActivity.this);
                    EumMany_Number++;
                }
            }, signup_phone_input.getText().toString(), signup_validation_code.getText().toString(), invite_code.getText().toString(), "ZHUOJIWANGLUO", "XIAOMI");
        }
    }


    //开启倒计时
    public void startTime()
    {
        /** 倒计时60秒，一次1秒 */
        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                signup_get_code.setText(millisUntilFinished / 1000 + "秒" + "后重试");
            }

            @Override
            public void onFinish() {
                signup_get_code.setText("获取验证码");
            }
        }.start();
    }
}

