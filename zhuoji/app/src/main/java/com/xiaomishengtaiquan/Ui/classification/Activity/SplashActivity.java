package com.xiaomishengtaiquan.Ui.classification.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.xiaomishengtaiquan.MainActivity;
import com.xiaomishengtaiquan.R;
import com.xiaomishengtaiquan.Utils.UtilsAll;

//闪屏页
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置没有标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcomeone);
        RelativeLayout layoutSplash=(RelativeLayout) findViewById(R.id.activity_splash);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.5f,1f);
        alphaAnimation.setDuration(1000);//设置动画播放时长1000毫秒（1秒）
        layoutSplash.startAnimation(alphaAnimation);
        //设置动画监听
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //页面的跳转
                UtilsAll.JumpPager(SplashActivity.this,MainActivity.class);
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }
}
