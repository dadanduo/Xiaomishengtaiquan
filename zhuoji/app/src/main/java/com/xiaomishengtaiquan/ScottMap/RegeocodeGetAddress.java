/**
 * Project Name:Android_Car_Example  
 * File Name:RegeocodeTask.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月2日下午6:24:53  
 *
 */

package com.xiaomishengtaiquan.ScottMap;

import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xiaomishengtaiquan.Utils.LogAll;
import com.xiaomishengtaiquan.interfaceCallback.OnLocationGetAddressGetListener;

/**
 * ClassName:RegeocodeTask <br/>
 * Function: 简单的封装的逆地理编码功能 <br/>
 * Date: 2018年4月2日 下午6:24:53 <br/>
 *
 * @author dada.chenda
 * @version
 * @since JDK 1.8
 * @see
 */
public class RegeocodeGetAddress implements OnGeocodeSearchListener {
	private static final float SEARCH_RADIUS = 50;
	private OnLocationGetAddressGetListener onLocationGetAddressGetListenercontent;

	private GeocodeSearch mGeocodeSearch;

	private String address_txt;


	private Context context;

	public RegeocodeGetAddress(Context context) {
		context=context;
		mGeocodeSearch = new GeocodeSearch(context);
		mGeocodeSearch.setOnGeocodeSearchListener(this);
	}

	public void search(double latitude, double longitude) {
		RegeocodeQuery regecodeQuery = new RegeocodeQuery(new LatLonPoint(
				latitude, longitude), SEARCH_RADIUS, GeocodeSearch.AMAP);
		mGeocodeSearch.getFromLocationAsyn(regecodeQuery);
	}
	public void search_Use_address(String address)
	{
		address_txt=address;
		LogAll.printError("选择的地址："+address);
		//构造 GeocodeSearch 对象，并设置监听。
		//通过GeocodeQuery设置查询参数,调用getFromLocationNameAsyn(GeocodeQuery geocodeQuery) 方法发起请求。
		//address表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode都ok
		GeocodeQuery query = new GeocodeQuery(address, address);
		mGeocodeSearch.getFromLocationNameAsyn(query);
	}

	public void setonLocationGetAddressGetListener(OnLocationGetAddressGetListener onLocationGetAddressGetListener)
	{
		onLocationGetAddressGetListenercontent= onLocationGetAddressGetListener;
	}

	@Override
	public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

		if (i == AMapException.CODE_AMAP_SUCCESS) {
			if (geocodeResult != null
					&& geocodeResult.getGeocodeAddressList().size()>=0) {
				LatLonPoint latLonPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
				LogAll.printError("经度："+ latLonPoint.getLatitude());
				PositionEntity entity = new PositionEntity();
				entity.address = address_txt;
				entity.city = address_txt;
				entity.latitue=latLonPoint.getLatitude();
				entity.longitude=latLonPoint.getLongitude();
				onLocationGetAddressGetListenercontent.onLocationAddressGet(entity);
			}
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult regeocodeReult,
									int resultCode) {
		if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
			if (regeocodeReult != null
					&& regeocodeReult.getRegeocodeAddress() != null
					&&  onLocationGetAddressGetListenercontent != null) {
				String address = regeocodeReult.getRegeocodeAddress()
						.getFormatAddress();
				String city = regeocodeReult.getRegeocodeAddress().getCityCode();

				PositionEntity entity = new PositionEntity();
				entity.address = address;
				entity.city = city;
				entity.latitue=regeocodeReult.getRegeocodeQuery().getPoint().getLatitude();
				entity.longitude=regeocodeReult.getRegeocodeQuery().getPoint().getLongitude();
				LogAll.printError(regeocodeReult.getRegeocodeQuery().getPoint().getLatitude()+"?????");
				onLocationGetAddressGetListenercontent.onRegeAddresscodeGet(entity);

			}
		}
		//TODO 可以根据app自身需求对查询错误情况进行相应的提示或者逻辑处理


	}

}
