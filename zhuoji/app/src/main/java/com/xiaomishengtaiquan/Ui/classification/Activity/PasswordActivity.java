package com.xiaomishengtaiquan.Ui.classification.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.xiaomishengtaiquan.Application.ZApplication;
import com.xiaomishengtaiquan.MainActivity;
import com.xiaomishengtaiquan.Model.Response;
import com.xiaomishengtaiquan.R;
import com.xiaomishengtaiquan.Utils.LogAll;
import com.xiaomishengtaiquan.Utils.PrefManager;
import com.xiaomishengtaiquan.Utils.ToastUtilsAll;
import com.xiaomishengtaiquan.Utils.UtilsAll;
import com.xiaomishengtaiquan.Utils.XutilsHttp;
import com.xiaomishengtaiquan.interfaceCallback.OkhttpCallBack;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by chenda on 2018/3/28.
 */

@ContentView(R.layout.password_set)
public class PasswordActivity extends Activity {

    @ViewInject(R.id.password_set)
    private EditText password_set;
    @ViewInject(R.id.reset_password)
    private EditText reset_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); //绑定注解
    }
    @Event(value = {R.id.back_setpassword},type = View.OnClickListener.class)
    private void back_setpassword(View view)
    {
        finish();
    }
    @Event(value = {R.id.password_ok},type = View.OnClickListener.class)
    private void password_ok(View view)
    {
        if(UtilsAll.isEmpty(password_set.getText().toString()))
        {
            ToastUtilsAll.alertDefine(PasswordActivity.this,"请输入密码");
        }else if(UtilsAll.isEmpty(reset_password.getText().toString()))
        {
            ToastUtilsAll.alertDefine(PasswordActivity.this,"请确认密码");
        }else if(!password_set.getText().toString().equals(reset_password.getText().toString()))
        {
            ToastUtilsAll.alertDefine(PasswordActivity.this,"两次输入的密码不一致");
        }else
        {
            XutilsHttp.postEncodedSetPassword(new OkhttpCallBack() {
                @Override
                public void OnSuccess(String json) {
                    Response response = new Response();
                    response = ZApplication.gson.fromJson(json, Response.class);
                    if (response.getCode().equals(XutilsHttp.SUCESS)) {
                        UtilsAll.JumpPager(PasswordActivity.this, MainActivity.class);
                        ToastUtilsAll.alertDefine(PasswordActivity.this, "密码修改成功！");
                    }
                    LogAll.print("成功："+json,0);
                }
                @Override
                public void OnFaild(String faildM) {
                    LogAll.print("失败："+faildM,0);
                    XutilsHttp.NotHaveNetwork(PasswordActivity.this);
                }
            }, PrefManager.getData_token(PasswordActivity.this),UtilsAll.md5(password_set.getText().toString()),UtilsAll.md5(reset_password.getText().toString()),"shanghaizhuoji","xiaomi");
        }

    }
}
