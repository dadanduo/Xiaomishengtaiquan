package com.xiaomishengtaiquan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomishengtaiquan.Ui.classification.Fragment.FoundFragment;
import com.xiaomishengtaiquan.Ui.classification.Fragment.MyFragment;
import com.xiaomishengtaiquan.Ui.classification.Fragment.RecomMendedFragment;
import com.xiaomishengtaiquan.Ui.classification.Fragment.SharedFragment;
import com.xiaomishengtaiquan.Utils.DialogUtils;
import com.xiaomishengtaiquan.Utils.UtilsAll;
import com.xiaomishengtaiquan.interfaceCallback.alertCallBack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //共享
    private SharedFragment sharedFragment;
    //发现
    private FoundFragment foundFragment;
    //推荐
    private RecomMendedFragment recomMendedFragment;
    //我的
    private MyFragment myFragment;
    //共享
    private RelativeLayout SharedButton;
    //发现
    private RelativeLayout FoundButton;
    //推荐
    private RelativeLayout RecomMendedButton;
    //我的
    private RelativeLayout MyButton;

    private ImageView ShareImg;
    private ImageView FoundImg;
    private ImageView RecomMendedImg;
    private ImageView MyImg;

    private TextView ShareTxt;
    private TextView FoundTxt;
    private TextView RecomMendedTxt;
    private TextView MyTxt;

    private boolean granted;

    //退出时的时间
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_main);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        UtilsAll.abnormal(this);

        //判断网络是否可用
        registered();
        FoundButton.setOnClickListener(this);
        SharedButton.setOnClickListener(this);
        RecomMendedButton.setOnClickListener(this);
        MyButton.setOnClickListener(this);

        initPermission();
        initFragment1();

    }

    public void registered()
    {
        FoundButton =findViewById(R.id.found);
        SharedButton=findViewById(R.id.share);
        RecomMendedButton=findViewById(R.id.recommend);
        MyButton=findViewById(R.id.my);

        ShareImg=findViewById(R.id.share_img);
        FoundImg=findViewById(R.id.found_img);
        RecomMendedImg=findViewById(R.id.recommend_img);
        MyImg=findViewById(R.id.my_img);

        ShareTxt=findViewById(R.id.share_txt);
        FoundTxt=findViewById(R.id.found_txt);
        RecomMendedTxt=findViewById(R.id.recommend_txt);
        MyTxt=findViewById(R.id.my_txt);
    }

    //显示第一个fragment
    private void initFragment1(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if( foundFragment == null){
            foundFragment = new FoundFragment();
            transaction.add(R.id.main_frame_layout, foundFragment);

        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(foundFragment);
        //提交事务
        transaction.commit();

        ShareImg.setBackgroundResource(R.mipmap.tab_home_press);
        FoundImg.setBackgroundResource(R.mipmap.tabbar_discover);
        RecomMendedImg.setBackgroundResource(R.mipmap.tabbar_cicle);
        MyImg.setBackgroundResource(R.mipmap.tabbar_me);

        ShareTxt.setTextColor(getResources().getColor(R.color.colorselect));
        FoundTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
        RecomMendedTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
        MyTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
    }


    //显示第二个fragment
    private void initFragment2(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(recomMendedFragment == null){
            recomMendedFragment = new RecomMendedFragment();
            transaction.add(R.id.main_frame_layout,recomMendedFragment );

        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(recomMendedFragment);
        //提交事务
        transaction.commit();

        ShareImg.setBackgroundResource(R.mipmap.tab_home);
        FoundImg.setBackgroundResource(R.mipmap.tabbar_discover_press);
        RecomMendedImg.setBackgroundResource(R.mipmap.tabbar_cicle);
        MyImg.setBackgroundResource(R.mipmap.tabbar_me);

        ShareTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
        FoundTxt.setTextColor(getResources().getColor(R.color.colorselect));
        RecomMendedTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
        MyTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
    }

    //显示第三个fragment
    private void initFragment3(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(sharedFragment== null){
           sharedFragment = new SharedFragment();
            transaction.add(R.id.main_frame_layout, sharedFragment);

        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(sharedFragment);
        //提交事务
        transaction.commit();

        ShareImg.setBackgroundResource(R.mipmap.tab_home);
        FoundImg.setBackgroundResource(R.mipmap.tabbar_discover);
        RecomMendedImg.setBackgroundResource(R.mipmap.tabbar_cicle_press);
        MyImg.setBackgroundResource(R.mipmap.tabbar_me);

        ShareTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
        FoundTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
        RecomMendedTxt.setTextColor(getResources().getColor(R.color.colorselect));
        MyTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
    }

    //显示第四个fragment
    private void initFragment4(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(myFragment == null){
            myFragment = new MyFragment();
            transaction.add(R.id.main_frame_layout, myFragment);

        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(myFragment);
        //提交事务
        transaction.commit();

        ShareImg.setBackgroundResource(R.mipmap.tab_home);
        FoundImg.setBackgroundResource(R.mipmap.tabbar_discover);
        RecomMendedImg.setBackgroundResource(R.mipmap.tabbar_cicle);
        MyImg.setBackgroundResource(R.mipmap.tabbar_me_press);

        ShareTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
        FoundTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
        RecomMendedTxt.setTextColor(getResources().getColor(R.color.colortabletxt));
        MyTxt.setTextColor(getResources().getColor(R.color.colorselect));
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction){
        if(sharedFragment != null){
            transaction.hide(sharedFragment);
        }
        if(foundFragment != null){
            transaction.hide(foundFragment);
        }
        if(recomMendedFragment != null){
            transaction.hide(recomMendedFragment);
        }
        if(myFragment!=null)
        {
            transaction.hide(myFragment);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == SharedButton){
            initFragment1();
        }else if(v == FoundButton){
            initFragment2();
        }else if(v == RecomMendedButton){
            initFragment3();
        }else if(v==MyButton) {
            initFragment4();
        }
    }

    //权限判断
    private void initPermission() {
        int permission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            //需不需要解释的dialog
            if (shouldRequest()) return;
            //请求权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    private boolean shouldRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //显示一个对话框，给用户解释
            explainDialog();
            return true;
        }
        return false;
    }
    private void explainDialog() {

        DialogUtils.AlertDilog(MainActivity.this, "权限", "该操作需要定位权限，请允许该权限，否则该功能无法正常使用", "去设置", "禁止", new alertCallBack() {
            @Override
            public void OnOk() {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            @Override
            public void OnNo() {
                finish();
            }

        });
    }
    /**
     * 请求权限的回调
     *
     * 参数1：requestCode-->是requestPermissions()方法传递过来的请求码。
     * 参数2：permissions-->是requestPermissions()方法传递过来的需要申请权限
     * 参数3：grantResults-->是申请权限后，系统返回的结果，PackageManager.PERMISSION_GRANTED表示授权成功，PackageManager.PERMISSION_DENIED表示授权失败。
     * grantResults和permissions是一一对应的
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;//是否授权，可以根据permission作为标记
        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //再按一次退出系统
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出小蜜生态圈", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


}
