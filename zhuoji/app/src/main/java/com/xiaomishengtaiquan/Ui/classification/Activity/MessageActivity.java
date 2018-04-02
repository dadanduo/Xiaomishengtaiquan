package com.xiaomishengtaiquan.Ui.classification.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.xiaomishengtaiquan.R;

/**
 * Created by chenda on 2018/3/22.
 */

//消息页面
public class MessageActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.citychoose);
    }
}
