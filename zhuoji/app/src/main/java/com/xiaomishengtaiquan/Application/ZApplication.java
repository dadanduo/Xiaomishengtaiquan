/**
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.xiaomishengtaiquan.Application;

import android.app.Application;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;


//主项目中的application
public class ZApplication extends Application  {
    //是否打印bug日志
    public static  boolean ISSHOWLOG=true;
    //配置高德地图key
    public static  String MAPKEY="bfcc5ada51c0b71957d03219e2c22e2e";

    public static Gson gson;
    private static ZApplication zApplication;
    public static ZApplication getInstance() {
        return zApplication;
    }

    public static HttpUtils http;

    public static  String URL="http://172.16.0.235:8080/untitled2/loginImage.html?_ijt=63omppubu5u70cedo2mftm522u";
    @Override
    public void onCreate() {
        super.onCreate();
        gson=new Gson();
        http=new HttpUtils();

    }
}
