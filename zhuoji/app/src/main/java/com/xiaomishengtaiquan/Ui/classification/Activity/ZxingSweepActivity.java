package com.xiaomishengtaiquan.Ui.classification.Activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.xiaomishengtaiquan.R;
import com.xiaomishengtaiquan.Utils.LogAll;

/**
 * Created by chenda on 2018/3/21.
 * 二维码扫描页面
 */

public class ZxingSweepActivity extends Activity implements DecoratedBarcodeView.TorchListener{

    // 添加一个按钮用来控制闪光灯，同时添加两个按钮表示其他功能，先用Toast表示
    LinearLayout btn_ontouch;
    LinearLayout inputCode;
    ImageView close;
    TextView closeandopen;

    DecoratedBarcodeView mDBV;
    private CaptureManager captureManager;
    private boolean isLightOn = false;



    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxingcode);
        btn_ontouch=  findViewById(R.id.btn_ontouch);
        inputCode=findViewById(R.id.inputCode);
        closeandopen=findViewById(R.id.closeandopen);
        mDBV= findViewById(R.id.dbv_custom);

        mDBV.setTorchListener(this);

        // 如果没有闪光灯功能，就去掉相关按钮
        if (!hasFlash()) {
            btn_ontouch.setVisibility(View.GONE);
        }
        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, mDBV);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();
        //选择闪关灯
        btn_ontouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightOn) {
                    mDBV.setTorchOff();
                } else {
                    mDBV.setTorchOn();
                }
            }
        });
        close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    // torch 手电筒
    @Override
    public void onTorchOn() {
        LogAll.print("手电筒开启",3);
        closeandopen.setText("关闭手电筒");
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        LogAll.print("手电筒关闭",3);
        closeandopen.setText("打开手电筒");
        isLightOn = false;
    }

    // 判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
