package com.xiaomishengtaiquan.Ui.classification.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomishengtaiquan.R;
import com.xiaomishengtaiquan.Ui.classification.View.EditText_Phone;
import com.xiaomishengtaiquan.Utils.DialogUtils;
import com.xiaomishengtaiquan.Utils.TranslateUtils;
import com.xiaomishengtaiquan.Utils.UtilsAll;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by chenda on 2018/3/26.
 */

@ContentView(R.layout.login)
public class LoginActivity extends Activity {

    @ViewInject(R.id.phone_input)
    EditText_Phone editText_phone;
    @ViewInject(R.id.validation_code)
    EditText validation_code;
    @ViewInject(R.id.get_code)
    TextView get_code;
    @ViewInject(R.id.password_logoin)
    LinearLayout password_logoin;
    @ViewInject(R.id.phone_login_in)
    LinearLayout phone_login_in;
    @ViewInject(R.id.updatepassword)
    TextView updatePassword;
    @ViewInject(R.id.password_intput)
    EditText_Phone password_intput;


    private boolean phone_and_password=true;

    Dialog showCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); //绑定注解

    }
    //Event事件注解 获取验证码
    @Event(value = {R.id.get_code},type = View.OnClickListener.class)
    //方法需要用private
    private void getCode(View view){
        if(UtilsAll.isEmpty(editText_phone.getText()))
        {
            Toast.makeText(this,"请先输入手机号码"+editText_phone.getText(),Toast.LENGTH_LONG).show();
        } else{
            showCode= DialogUtils.createLoadingDialog(LoginActivity.this,"发送验证码中");
            /** 倒计时60秒，一次1秒 */
            CountDownTimer timer = new CountDownTimer(60*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub
                    get_code.setText(millisUntilFinished/1000+"秒"+"后重试");
                }

                @Override
                public void onFinish() {
                    get_code.setText("获取验证码");
                    DialogUtils.closeDialog(showCode);
                }
            }.start();

        }

    }
    //Event事件注解 注册账号
    @Event(value = {R.id.signup},type = View.OnClickListener.class)
    //方法需要用private
    private void SingUp(View view){
        UtilsAll.JumpPager(LoginActivity.this,SingupActivity.class);
    }
    @Event(value = {R.id.login_in},type = View.OnClickListener.class)
    private void loginIn(View view)
    {
        Toast.makeText(this,"注册账号",Toast.LENGTH_LONG).show();
    }
    @Event(value = {R.id.forget_password},type = View.OnClickListener.class)
    private void forgetPassword(View view)
    {
        Toast.makeText(this,"忘记密码",Toast.LENGTH_LONG).show();
    }
    @Event(value = {R.id.updatepassword},type = View.OnClickListener.class)
    private void UpdatePassword(View view)
    {
        if(phone_and_password)
        {
            password_logoin.setVisibility(View.VISIBLE);
            phone_login_in.setVisibility(View.GONE);
            password_logoin.setAnimation(TranslateUtils.moveToViewShow());
            //密码登录从右边滑入
            password_logoin.startAnimation(AnimationUtils.loadAnimation(
                    LoginActivity.this, R.anim.slide_from_right
            ));
            updatePassword.setText("切换成验证码登录");
            phone_and_password=false;
        }else{
            password_logoin.setVisibility(View.GONE);
            phone_login_in.setVisibility(View.VISIBLE);
            //手机登录布局从左边滑入
            phone_login_in.startAnimation(AnimationUtils.loadAnimation(
                    LoginActivity.this, R.anim.slide_from_left
            ));
            updatePassword.setText("切换成密码登录");
            phone_and_password=true;
        }

    }
    @Event(value = {R.id.wechat_btn},type = View.OnClickListener.class)
    private void wechat(View view)
    {
        Toast.makeText(this,"" +
                "微信",Toast.LENGTH_LONG).show();
    }
    @Event(value = {R.id.qq_btn},type = View.OnClickListener.class)
    private void qq(View view)
    {
        Toast.makeText(this,"qq",Toast.LENGTH_LONG).show();
    }
    @Event(value = {R.id.weibo_btn},type = View.OnClickListener.class)
    private void weibo(View view)
    {
        Toast.makeText(this,"微博",Toast.LENGTH_LONG).show();
    }
    @Event(value = {R.id.closeLogin},type = View.OnClickListener.class)
    private void close(View view)
    {
        finish();
    }
}
