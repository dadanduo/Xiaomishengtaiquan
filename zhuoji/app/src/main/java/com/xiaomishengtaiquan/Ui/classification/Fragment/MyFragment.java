package com.xiaomishengtaiquan.Ui.classification.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.xiaomishengtaiquan.H5toAndroid.JsMethod;
import com.xiaomishengtaiquan.H5toAndroid.Webset;
import com.xiaomishengtaiquan.R;

/**
 * Created by chenda on 2018/3/20.
 */

public class MyFragment extends Fragment{


    private WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.my,container,false);
        webView=view.findViewById(R.id.web_my);
        Webset.setweb(webView,getActivity());
        return view;
    }
}
