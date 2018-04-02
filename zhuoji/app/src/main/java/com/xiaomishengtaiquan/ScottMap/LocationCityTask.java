/**
 * Project Name:Android_Car_Example  
 * File Name:LocationTask.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月3日上午9:27:45  
 *
 */

package com.xiaomishengtaiquan.ScottMap;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xiaomishengtaiquan.interfaceCallback.OnLocationGetCityListener;


/**
 * ClassName:LocationTask <br/>
 * Function: 简单封装了定位请求，可以进行单次定位和多次定位，注意的是在app结束或定位结束时注意销毁定位 <br/>
 * Date: 2015年4月3日 上午9:27:45 <br/>
 *
 * @author dada.chen
 * @version
 * @since JDK 1.8
 * @see
 */
public class LocationCityTask implements AMapLocationListener,
		OnLocationGetCityListener {

	private AMapLocationClient mLocationClient;

	private static LocationCityTask mLocationTask;

	private Context mContext;

	private OnLocationGetCityListener mOnLocationGetCityGetListener;



	private LocationCityTask(Context context) {
		mLocationClient = new AMapLocationClient(context);
		mLocationClient.setLocationListener(this);
		mContext = context;
	}
	public void setOnLocationGetCityListener(
			OnLocationGetCityListener onLocationGetCityListener) {
		mOnLocationGetCityGetListener= onLocationGetCityListener;
	}
	public static LocationCityTask getInstance(Context context) {
		if (mLocationTask == null) {
			mLocationTask = new LocationCityTask(context);
		}
		return mLocationTask;
	}
	/**
	 * 开启单次定位
	 */
	public void startSingleLocate() {
		AMapLocationClientOption option=new AMapLocationClientOption();
		option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		option.setOnceLocation(true);
		mLocationClient.setLocationOption(option);
		mLocationClient.startLocation();

	}
	/**
	 * 开启多次定位
	 */
	public void startLocate() {

		AMapLocationClientOption option=new AMapLocationClientOption();
		option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		option.setOnceLocation(false);
		option.setInterval(1000);
		mLocationClient.setLocationOption(option);
		mLocationClient.startLocation();

	}
	/**
	 * 结束定位，可以跟多次定位配合使用
	 */
	public void stopLocate() {
		mLocationClient.stopLocation();

	}
	/**
	 * 销毁定位资源
	 */
	public void onDestroy() {
		mLocationClient.stopLocation();
		mLocationClient.onDestroy();
		mLocationTask = null;
	}
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null && amapLocation.getErrorCode() == 0) {
			PositionEntity entity = new PositionEntity();
			entity.latitue = amapLocation.getLatitude();
			entity.longitude = amapLocation.getLongitude();

			if (!TextUtils.isEmpty(amapLocation.getAddress())) {
				entity.address = amapLocation.getAddress();
			}
			if (!TextUtils.isEmpty(amapLocation.getCity())) {
				entity.city = amapLocation.getCity();
			}
			mOnLocationGetCityGetListener.onLocationGet(entity);


		}
	}
	@Override
	public void onLocationGet(PositionEntity entity) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onRegecodeGet(PositionEntity entity) {

		// TODO Auto-generated method stub

	}

}
