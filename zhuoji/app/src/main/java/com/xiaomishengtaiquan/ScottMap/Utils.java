/**
 * Project Name:Android_Car_Example
 * File Name:Utils.java
 * Package Name:com.amap.api.car.example
 * Date:2015年4月7日下午3:43:05
 */

package com.xiaomishengtaiquan.ScottMap;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.xiaomishengtaiquan.Application.ZApplication;
import com.xiaomishengtaiquan.Model.LatitudeANDlongitude;
import com.xiaomishengtaiquan.R;

import java.util.ArrayList;
import java.util.List;


/**
 * ClassName:Utils <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2015年4月7日 下午3:43:05 <br/>  
 * @author dada.chen
 * @version
 * @since JDK 1.8
 * @see
 */
public class Utils {

    private static ArrayList<Marker> markers = new ArrayList<Marker>();

    /**
     * 添加模拟测试的车的点
     */
    public static void addEmulateData(AMap amap, LatLng center) {
        if (markers.size() == 0) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                    .fromResource(R.mipmap.stable_cluster_marker_one_normal);

            for (int i = 0; i < 20; i++) {
                double latitudeDelt;
                double longtitudeDelt;
                if(i%2==0) {
                    latitudeDelt = (Math.random() - 0.5) * 0.1;
                    longtitudeDelt = (Math.random() - 0.5) * 0.1;
                }else
                {
                    latitudeDelt = (Math.random() - 0.5) * 0.01;
                    longtitudeDelt = (Math.random() - 0.5) * 0.01;
                }
                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.setFlat(true);
//                markerOptions.anchor(0.5f, 0.5f);
                markerOptions.icon(bitmapDescriptor);

                markerOptions.position(new LatLng(center.latitude + latitudeDelt, center.longitude + longtitudeDelt));
                Marker marker = amap.addMarker(markerOptions);
                markers.add(marker);
            }
        } else {
            for (Marker marker : markers) {
                double latitudeDelt = (Math.random() - 0.5) * 0.1;
                double longtitudeDelt = (Math.random() - 0.5) * 0.1;
                marker.setPosition(new LatLng(center.latitude + latitudeDelt, center.longitude + longtitudeDelt));

            }
        }
    }

    /**
     * 移除marker
     */
    public static void removeMarkers() {
        for (Marker marker : markers) {
            marker.remove();
            marker.destroy();
        }
        markers.clear();
    }

    //通过中心点拉去后台附近的marker的方法
    public static  void  addListMarker(String json, AMap aMap, Context context)
    {
//        String json="[{" +
//                "\"Latitude\": 31.33605," +
//                "\"longitude\": 121.433372" +
//                "}, {" +
//                "\"Latitude\": 31.335046," +
//                "\"longitude\": 121.433601" +
//                "}, {" +
//                "\"Latitude\": 31.690563," +
//                "\"longitude\": 121.52823" +
//                "}, {" +
//                "\"Latitude\": 31.340839," +
//                "\"longitude\": 121.44519" +
//                "}, {" +
//                "\"Latitude\": 31.333118," +
//                "\"longitude\": 121.441726" +
//                "}, {" +
//                "\"Latitude\": 31.330795," +
//                "\"longitude\": 121.440696" +
//                "}, {" +
//                "\"Latitude\": 31.32752," +
//                "\"longitude\": 121.43592" +
//                "}]";
       List<LatitudeANDlongitude> llts=new ArrayList<LatitudeANDlongitude>();
        // Json的解析类对象
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        // 加强for循环遍历JsonArray
        for (JsonElement longtu : jsonArray) {
            // 使用GSON
            LatitudeANDlongitude anDlongitude = ZApplication.gson.fromJson(longtu,
                    LatitudeANDlongitude.class);
            llts.add(anDlongitude);
        }

        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();

        //循环向地图添加marker
        for(int i=0;i<llts.size();i++)
        {
            LatLng latLng = new LatLng(llts.get(i).getLatitude(),llts.get(i).getLongitude());
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.draggable(false);//设置Marker不可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(context.getResources(),R.mipmap.fixed_point_one_normal)));
            markerOption.position(latLng);
            markerOptionlst.add(markerOption);
            aMap.addMarker(markerOption);
        }
    }

}
  
