package com.xiaomishengtaiquan.Ui.classification.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.xiaomishengtaiquan.Model.City;
import com.xiaomishengtaiquan.R;
import com.xiaomishengtaiquan.Ui.classification.Activity.CityActivity;
import com.xiaomishengtaiquan.Ui.classification.Activity.LoginActivity;
import com.xiaomishengtaiquan.Ui.classification.Activity.ZxingSweepActivity;
import com.xiaomishengtaiquan.Utils.AMapUtil;
import com.xiaomishengtaiquan.Utils.DialogUtils;
import com.xiaomishengtaiquan.Utils.LoadDialog;
import com.xiaomishengtaiquan.Utils.LogAll;
import com.xiaomishengtaiquan.Utils.PrefManager;
import com.xiaomishengtaiquan.Utils.ToastUtil;
import com.xiaomishengtaiquan.Utils.TranslateUtils;
import com.xiaomishengtaiquan.Utils.UtilsAll;
import com.xiaomishengtaiquan.interfaceCallback.OnLocationGetAddressGetListener;
import com.xiaomishengtaiquan.interfaceCallback.OnLocationGetListener;
import com.xiaomishengtaiquan.interfaceCallback.alertCallBack;
import com.xiaomishengtaiquan.ScottMap.LocationTask;
import com.xiaomishengtaiquan.ScottMap.PoiSearchTask;
import com.xiaomishengtaiquan.ScottMap.PositionEntity;
import com.xiaomishengtaiquan.ScottMap.RegeocodeGetAddress;
import com.xiaomishengtaiquan.ScottMap.RegeocodeTask;
import com.xiaomishengtaiquan.ScottMap.RouteTask;
import com.xiaomishengtaiquan.ScottMap.Sha1;
import com.xiaomishengtaiquan.ScottMap.Utils;
import com.xiaomishengtaiquan.overlay.WalkRouteOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenda on 2018/3/20.
 */

public class SharedFragment extends Fragment  implements AMap.OnCameraChangeListener,
        AMap.OnMapLoadedListener, OnLocationGetListener,RouteTask.OnRouteCalculateListener,
        AMap.OnMapTouchListener,RouteSearch.OnRouteSearchListener,AMap.OnMapClickListener,OnLocationGetAddressGetListener {
   //出行
    private LinearLayout goon;
    //服务
    private LinearLayout service;
    //居所
    private LinearLayout residence;

    //城市选择
    private LinearLayout city;
    //消息
    private ImageView message;
    //我要用车
    private Button useBike;

    private View goonView;
    private View sericeView;
    private View residenceView;

    //点击marker的动画
    private LinearLayout stationAddress;

    private TextView citynameText;
    private RelativeLayout btn_go_s_h;
    private ImageView Resetleft;
    private ImageView ResetRight;
    //只允许定位城市设置一次，选择城市回来便不能再次赋值（因为这里采用的是多次不停的定位）
    private boolean iscityFirst=true;
    //没有网络用户滑动地图会给提示
    private boolean isOne=true;
    // 地图控件
    MapView mapView=null;
    //初始化地图控制器对象
    AMap aMap;
    //定位
    private LocationTask mLocationTask;
    //逆地理编码功能
    private RegeocodeTask mRegeocodeTask;
    private RegeocodeGetAddress regeocodeGetAddress;
    //绘制点标记
    private Marker mPositionMark, mInitialMark,tempMark;//可移动、圆点、点击
    //初始坐标、移动记录坐标
    private LatLng mStartPosition,mRecordPositon;
    //默认添加一次车辆坐标
    private boolean mIsFirst = true;
    //切换城市的封装
    private RouteTask mRouteTask;
    //就第一次显示位置
    private boolean mIsFirstShow = true;
    private LatLng initLocation;
    private ValueAnimator animator = null;//坐标动画
    private BitmapDescriptor initBitmap,moveBitmap,smallIdentificationBitmap,bigIdentificationBitmap;//定位圆点、可移动、所有标识（车）
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;
    private LatLonPoint mStartPoint = null;//起点，116.335891,39.942295
    private LatLonPoint mEndPoint = null;//终点，116.481288,39.995576
    private final int ROUTE_TYPE_WALK = 3;
    //是否正在处于规划路线状态 false:不是true:是
    private boolean isClickIdentification = false;
    WalkRouteOverlay walkRouteOverlay;//路线
    private String [] time;
    private String distance_show;
    public static final String TAG = "sharedfragment";
    //地址
    private TextView address;
    //剩余车辆
    private TextView bikeNum;
    //距离
    private TextView distance;
    //时间
    private TextView times;
    //查看附近的车辆
    private Button goLook;

    Dialog showCode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.share,container,false);
        //注册事件
        registere(view);
        mapView=view.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        initBitmap();
        initAMap();
        initLocation();
        RouteTask.getInstance(getActivity().getApplicationContext()).addRouteCalculateListener(this);
        Log.e(TAG, "sha1" + Sha1.sHA1(getActivity()));
        return view;
    }
    private void initBitmap()
    {
        initBitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.location_marker);
        moveBitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.homepage_wholeanchor);
        smallIdentificationBitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.stable_cluster_marker_one_normal);
        bigIdentificationBitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.stable_cluster_marker_one_select);
    }
    /**
     * 初始化定位
     */
    private void initLocation() {
        mLocationTask = LocationTask.getInstance(getActivity().getApplicationContext());
        mLocationTask.setOnLocationGetListener(this);
        mRegeocodeTask = new RegeocodeTask(getActivity().getApplicationContext());
        regeocodeGetAddress=new RegeocodeGetAddress(getActivity().getApplicationContext());
    }
    /**
     * 初始化地图控制器对象
     */
    private void initAMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mRouteSearch = new RouteSearch(getActivity());
            mRouteSearch.setRouteSearchListener(this);
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.getUiSettings().setGestureScaleByMapCenter(true);
//            aMap.getUiSettings().setScrollGesturesEnabled(false);
            aMap.setOnMapTouchListener(this);
            aMap.setOnMapLoadedListener(this);
            aMap.setOnCameraChangeListener(this);
            aMap.setOnMapClickListener(this);
            // 绑定 Marker 被点击事件
            aMap.setOnMarkerClickListener(markerClickListener);
            //选择城市回来的回调
            mRouteTask=RouteTask.getInstance(getActivity().getApplicationContext());

        }
    }


    public void registere(View view)
    {
        stationAddress=view.findViewById(R.id.stationAddress);
        address=view.findViewById(R.id.address);
        bikeNum=view.findViewById(R.id.bikeNum);
        distance=view.findViewById(R.id.distance);
        citynameText=view.findViewById(R.id.cityname);
        times=view.findViewById(R.id.times);
        goLook=view.findViewById(R.id.goLook);
        btn_go_s_h=view.findViewById(R.id.btn_go_s_h);
        ResetRight=view.findViewById(R.id.ResetRight);
        Resetleft=view.findViewById(R.id.Resetleft);

        goon=view.findViewById(R.id.goon);
        service=view.findViewById(R.id.service);
        residence=view.findViewById(R.id.residence);
        city=view.findViewById(R.id.citychoose);
        message=view.findViewById(R.id.message);
        useBike=view.findViewById(R.id.useBike);
        goonView=view.findViewById(R.id.goonview);
        sericeView=view.findViewById(R.id.serviceview);
        residenceView=view.findViewById(R.id.residenceview);

        //出行
        goon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goonView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                sericeView.setBackgroundColor(getResources().getColor(R.color.colortransparent));
                residenceView.setBackgroundColor(getResources().getColor(R.color.colortransparent));
            }
        });
        //服务
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goonView.setBackgroundColor(getResources().getColor(R.color.colortransparent));
                sericeView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                residenceView.setBackgroundColor(getResources().getColor(R.color.colortransparent));
            }
        });
        //居所
        residence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goonView.setBackgroundColor(getResources().getColor(R.color.colortransparent));
                sericeView.setBackgroundColor(getResources().getColor(R.color.colortransparent));
                residenceView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        });

        //城市代码
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),CityActivity.class),Activity.RESULT_FIRST_USER);
            }
        });

        //消息
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //我要用车点击事件
        useBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UtilsAll.isEmpty(PrefManager.getData_token(getActivity()))) {
                   DialogUtils.AlertDilog(getActivity(), "温馨提示", "你当前为游客身份,需要登录账号才能是用该功能！", "再逛逛", "去登录", new alertCallBack() {
                        @Override
                        public void OnOk() {
                            IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                            intentIntegrator
                                    .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                                    .setPrompt("对准车上二维码")//写那句提示的话
                                    .setOrientationLocked(false)//扫描方向固定
                                    .setCaptureActivity(ZxingSweepActivity.class) // 设置自定义的activity是CustomActivity
                                    .initiateScan(); // 初始化
                        }
                        @Override
                        public void OnNo() {
                            UtilsAll.JumpPager(getActivity(),LoginActivity.class);
                        }

                    });
                }else{
                    IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                    intentIntegrator
                            .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                            .setPrompt("对准车上二维码")//写那句提示的话
                            .setOrientationLocked(false)//扫描方向固定
                            .setCaptureActivity(ZxingSweepActivity.class) // 设置自定义的activity是CustomActivity
                            .initiateScan(); // 初始化扫描
                }
            }
        });
        //复位左边
        Resetleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRefresh();
            }
        });
        //复位右边
        ResetRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRefresh();
            }
        });
        //查看附近车辆
        goLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //返回数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         //         * 处理二维码扫描结果
         //         */
//        if (requestCode == REQUEST_CODE) {
//            //处理扫描结果（在界面上显示）
//            if (null != data) {
//                Bundle bundle = data.getExtras();
//                if (bundle == null) {
//                    return;
//                }
//                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
//                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
        //选择城市页面的回调
        if (requestCode == Activity.RESULT_FIRST_USER) {

            if (resultCode == Activity.RESULT_CANCELED) {
                String cityname=data.getStringExtra("cityname");
                if(!cityname.equals("close"))
                {
                    citynameText.setText(cityname);
                    iscityFirst=false;
                    LogAll.print(">>>>>>>>>"+cityname,4);
                    regeocodeGetAddress.search_Use_address(cityname);
                    showCode= DialogUtils.createLoadingDialog(getActivity(),"定位中请稍后...");

                }
            }
        }
    }

    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {

        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(final Marker marker) {
            showLYT();
            LogAll.printError(marker.getPosition() + "");
            isClickIdentification = true;
            if(tempMark!=null)
            {
                tempMark.setIcon(smallIdentificationBitmap);
                walkRouteOverlay.removeFromMap();
                tempMark = null;
            }
            startAnim(marker);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        tempMark = marker;
                        Log.e(TAG,mPositionMark.getPosition().latitude+"==="+mPositionMark.getPosition().longitude);
                        mStartPoint = new LatLonPoint(mRecordPositon.latitude, mRecordPositon.longitude);
                        mPositionMark.setPosition(mRecordPositon);
                        mEndPoint =new LatLonPoint(marker.getPosition().latitude,marker.getPosition().longitude);
                        regeocodeGetAddress.search(marker.getPosition().latitude,marker.getPosition().longitude);
                        marker.setIcon(bigIdentificationBitmap);
                        marker.setPosition(marker.getPosition());
                        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return true;
        }
    };

    private void startAnim(Marker marker) {
        ScaleAnimation anim = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f);
        anim.setDuration(300);
        marker.setAnimation(anim);
        marker.startAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        Utils.removeMarkers();
        mapView.onDestroy();
        mLocationTask.onDestroy();
        RouteTask.getInstance(getActivity().getApplicationContext()).removeRouteCalculateListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
        LogAll.printError("onresume");
        if (mInitialMark != null) {
            mInitialMark.setToTop();
        }
        if (mPositionMark != null) {
            mPositionMark.setToTop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if(isOne)
        {
            UtilsAll.abnormal(getActivity());
            isOne=false;
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Log.e(TAG, "onCameraChangeFinish" + cameraPosition.target);
        if(!isClickIdentification) {
            mRecordPositon = cameraPosition.target;
        }
        mStartPosition = cameraPosition.target;
        mRegeocodeTask.setOnLocationGetListener(this);
        regeocodeGetAddress.setonLocationGetAddressGetListener(this);
        mRegeocodeTask
                .search(mStartPosition.latitude, mStartPosition.longitude);
        if(mIsFirst) {
            //添加车辆数据
            Utils.addEmulateData(aMap, mStartPosition);
            createInitialPosition(cameraPosition.target.latitude, cameraPosition.target.longitude);
            //创建移动位置坐标
            createMovingPosition();
            mIsFirst = false;
        }
        if (mInitialMark != null) {
            mInitialMark.setToTop();
        }
        if (mPositionMark != null) {
            mPositionMark.setToTop();
            if(!isClickIdentification) {
                animMarker();
            }
        }
    }


    /**
     * 地图加载完成
     */
    @Override
    public void onMapLoaded() {
        //开启多次定位
        mLocationTask.startLocate();
    }

    /**
     * 创建初始位置图标
     */
    private void createInitialPosition(double lat, double lng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(lat, lng));
        markerOptions.icon(initBitmap);
        mInitialMark = aMap.addMarker(markerOptions);
        mInitialMark.setClickable(false);
    }

    /**
     * 创建移动位置图标
     */
    private void createMovingPosition() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(0, 0));
        markerOptions.icon(moveBitmap);
        mPositionMark = aMap.addMarker(markerOptions);
        mPositionMark.setPositionByPixels(mapView.getWidth() / 2,
                mapView.getHeight() / 2);
        mPositionMark.setClickable(false);
    }

    @Override
    public void onLocationGet(PositionEntity entity) {
        // todo 这里在网络定位时可以减少一个逆地理编码
        Log.e("onLocationGet", "onLocationGet" + entity.address);
        //选择城市页面回调不再赋值
        if(iscityFirst)
        {
            citynameText.setText(entity.city);
        }
        RouteTask.getInstance(getActivity().getApplicationContext()).setStartPoint(entity);
        mStartPosition = new LatLng(entity.latitue, entity.longitude);
        if(mIsFirstShow) {
            CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                    mStartPosition, 17);
            aMap.animateCamera(cameraUpate);
            mIsFirstShow = false;
        }
        mInitialMark.setPosition(mStartPosition);
        initLocation = mStartPosition;
        Log.e("onLocationGet", "onLocationGet" + mStartPosition);
    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {
        Log.e(TAG, "onRegecodeGet" + entity.address);
        entity.latitue = mStartPosition.latitude;
        entity.longitude = mStartPosition.longitude;
        RouteTask.getInstance(getActivity().getApplicationContext()).setStartPoint(entity);
        RouteTask.getInstance(getActivity().getApplicationContext()).search();
        Log.e(TAG, "onRegecodeGet" + mStartPosition);
    }

    @Override
    public void onRouteCalculate(float cost, float distance, int duration) {
        Log.e(TAG,"cost"+cost+"---"+"distance"+distance+"---"+"duration"+duration);
        movePoint();
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if(motionEvent.getPointerCount()>=2)
        {
            aMap.getUiSettings().setScrollGesturesEnabled(false);
        }else
        {
            aMap.getUiSettings().setScrollGesturesEnabled(true);
        }
    }

    //动一动
    private void animMarker() {
        if (animator != null) {
            animator.start();
            return;
        }
        animator = ValueAnimator.ofFloat(mapView.getHeight()/2, mapView.getHeight()/2 - 30);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(150);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                mPositionMark.setPositionByPixels(mapView.getWidth() / 2, Math.round(value));
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPositionMark.setIcon(moveBitmap);
            }
        });
        animator.start();
    }

    private void endAnim() {
        if (animator != null && animator.isRunning())
            animator.end();
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    //规划完路线的回调
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        LoadDialog.getInstance().dismiss();
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    walkRouteOverlay = new WalkRouteOverlay(
                            getActivity(), aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    time = AMapUtil.getFriendlyTimeArray(dur);
                    distance_show = AMapUtil.getFriendlyLength(dis);
                    distance.setText(distance_show);
                    times.setText(time[0]+time[1]);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(getActivity(), "对不起没有搜索到相关信息");
                }
            } else {
                ToastUtil.show(getActivity(), "对不起没有搜索到相关信息");
            }
        } else {
            ToastUtil.showerror(getActivity().getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(getActivity(), "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(getActivity(), "终点未设置");
        }
        showDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    private void showDialog()
    {
        LoadDialog loadDialog =  LoadDialog.getInstance();
        loadDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.load_dialog);
        LoadDialog.getInstance().show(getActivity().getSupportFragmentManager(),"");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        clickMap();
    }

    //刷新
    private void clickRefresh()
    {
        clickInitInfo();
        if(initLocation!=null) {
            CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                    initLocation, 17f);
            aMap.animateCamera(cameraUpate);
        }
    }
    private void clickMap()
    {
        clickInitInfo();
        if(mRecordPositon!=null) {
            CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                    mRecordPositon, 17f);
            aMap.animateCamera(cameraUpate);
        }
    }
    private void clickInitInfo()
    {
        hideRLYT();
        isClickIdentification = false;
        if(null!=tempMark) {
            tempMark.setIcon(smallIdentificationBitmap);
            tempMark.hideInfoWindow();
            tempMark = null;
        }
        //移除连线
        if(null!=walkRouteOverlay) {
            walkRouteOverlay.removeFromMap();
        }
    }
    //显示信息
    public void showLYT()
    {
        stationAddress.setVisibility(View.VISIBLE);
        btn_go_s_h.setVisibility(View.GONE);
        stationAddress.setAnimation(TranslateUtils.moveToViewShow());
    }
    //隐藏信息
    public void hideRLYT()
    {
        stationAddress.setVisibility(View.GONE);
        btn_go_s_h.setVisibility(View.VISIBLE);
        stationAddress.setAnimation(TranslateUtils.moveToViewHide());
    }

    //地址转经纬度的回调
    @Override
    public void onLocationAddressGet(PositionEntity entity) {
        DialogUtils.closeDialog(showCode);
        LogAll.printError("回调的地址："+entity.address);
        if (entity.latitue == 0 && entity.longitude == 0) {
            PoiSearchTask poiSearchTask=new PoiSearchTask(getActivity().getApplicationContext());
            poiSearchTask.search(entity.address,RouteTask.getInstance(getActivity().getApplicationContext()).getStartPoint().city);
        } else {
            mRouteTask.setEndPoint(entity);
            mRouteTask.search();
        }
     }
    //经纬度转地址的回调
    @Override
    public void onRegeAddresscodeGet(PositionEntity entity) {
        //设置终点地址
        address.setText(entity.address);
    }
    //移动点
    public void movePoint()
    {
        PositionEntity endPoint = RouteTask.getInstance(getActivity().getApplicationContext()).getEndPoint();
        mRecordPositon = new LatLng(endPoint.latitue,endPoint.longitude);
        clickMap();
        RouteTask.getInstance(getActivity().getApplicationContext()).setEndPoint(null);
    }
}
