package com.xiaomishengtaiquan.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chenda on 2018/3/22.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    //SharedPreferences 文件名
    private static final String PREF_NAME = "intro_slider";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //sharedPreferences 存储token值文件名

    private static  final  String SAVE_TOKEN_NAME="token_saves";

    public PrefManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    //存token数据
    public static  void  saveData(Context context,String token)
    {
        SharedPreferences sp = context.getSharedPreferences(SAVE_TOKEN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.commit();
    }
    //取得token
    public static  String getData_token(Context context)
    {
        SharedPreferences sp =context.getSharedPreferences(SAVE_TOKEN_NAME, Context.MODE_PRIVATE);
        String token = sp.getString("token", null);
        return token;
    }

    public boolean isFirstTimeLaunch(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
