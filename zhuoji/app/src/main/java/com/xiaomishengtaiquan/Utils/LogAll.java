package com.xiaomishengtaiquan.Utils;

import android.util.Log;

import com.xiaomishengtaiquan.Application.ZApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenda on 2017/12/28.
 */
public class LogAll {
    public static void print(String printContent, int i) {
        if (ZApplication.ISSHOWLOG) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String tag = "ShowTime:"+formatter.format(curDate);
            switch (i) {
                case 0:
                    Log.v(tag, printContent);
                    break;
                case 1:
                    Log.d(tag, printContent);
                    break;
                case 2:
                    Log.i(tag, printContent);
                    break;
                case 3:
                    Log.w(tag, printContent);
                    break;
                case 4:
                    Log.e(tag, printContent);
                    break;
            }
        }
    }
    public static void printError(String printContent) {
        int i=4;
        if (ZApplication.ISSHOWLOG) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String tag = "ShowTime:"+formatter.format(curDate);
            switch (i) {
                case 0:
                    Log.v(tag, printContent);
                    break;
                case 1:
                    Log.d(tag, printContent);
                    break;
                case 2:
                    Log.i(tag, printContent);
                    break;
                case 3:
                    Log.w(tag, printContent);
                    break;
                case 4:
                    Log.e(tag, printContent);
                    break;
            }
        }
    }
}
