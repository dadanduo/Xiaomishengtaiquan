package com.xiaomishengtaiquan.Ui.classification.Activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.xiaomishengtaiquan.Model.City;
import com.xiaomishengtaiquan.R;
import com.xiaomishengtaiquan.ScottMap.LocationCityTask;
import com.xiaomishengtaiquan.ScottMap.PositionEntity;
import com.xiaomishengtaiquan.Ui.classification.View.MyLetterListView;
import com.xiaomishengtaiquan.Utils.DBHelper;
import com.xiaomishengtaiquan.Utils.DatabaseHelper;
import com.xiaomishengtaiquan.Utils.PingYinUtil;
import com.xiaomishengtaiquan.interfaceCallback.OnLocationGetCityListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

//城市选择页面
public class CityActivity extends Activity implements OnScrollListener,AMapLocationListener,
        OnLocationGetCityListener {
    private BaseAdapter adapter;
    private ResultListAdapter resultListAdapter;
    private ListView personList;
    private ListView resultList;
    private TextView overlay; // 对话框首字母textview
    private MyLetterListView letterListView; // A-Z listview
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private Handler handler;
    private OverlayThread overlayThread; // 显示首字母对话框
    private ArrayList<City> allCity_lists; // 所有城市列表
    private ArrayList<City> city_lists;// 城市列表
    private ArrayList<City> city_hot;
    private ArrayList<City> city_result;
    private ArrayList<String> city_history;
    private EditText sh;
    private TextView tv_noresult;
    //定位
    private LocationCityTask mLocationCityTask;
    private String currentCity; // 用于保存定位到的城市
    private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
    private boolean isNeedFresh;

    private DatabaseHelper helper;

    ImageView closecity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citychoose);
        initLocation();
        personList = findViewById(R.id.list_view);
        allCity_lists = new ArrayList<City>();
        city_hot = new ArrayList<City>();
        city_result = new ArrayList<City>();
        city_history = new ArrayList<String>();
        resultList = findViewById(R.id.search_result);
        sh = findViewById(R.id.sh);
        tv_noresult = findViewById(R.id.tv_noresult);
        closecity = findViewById(R.id.closecity);
        closecity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, CityActivity.this.getIntent().putExtra("cityname", "close"));
                CityActivity.this.finish();
            }
        });
        helper = new DatabaseHelper(this);
        sh.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString() == null || "".equals(s.toString())) {
                    letterListView.setVisibility(View.VISIBLE);
                    personList.setVisibility(View.VISIBLE);
                    resultList.setVisibility(View.GONE);
                    tv_noresult.setVisibility(View.GONE);
                } else {
                    city_result.clear();
                    letterListView.setVisibility(View.GONE);
                    personList.setVisibility(View.GONE);
                    getResultCityList(s.toString());
                    if (city_result.size() <= 0) {
                        tv_noresult.setVisibility(View.VISIBLE);
                        resultList.setVisibility(View.GONE);
                    } else {
                        tv_noresult.setVisibility(View.GONE);
                        resultList.setVisibility(View.VISIBLE);
                        resultListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        letterListView = findViewById(R.id.MyLetterListView01);
        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        isNeedFresh = true;
        personList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //所有的城市
                if (position >= 4) {
                    setResult(RESULT_CANCELED, CityActivity.this.getIntent().putExtra("cityname", allCity_lists.get(position).getName()));
                    CityActivity.this.finish();
                }
            }
        });
        locateProcess = 1;
        personList.setAdapter(adapter);
        personList.setOnScrollListener(this);
        resultListAdapter = new ResultListAdapter(this, city_result);
        resultList.setAdapter(resultListAdapter);
        resultList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //搜索出来的城市
                setResult(RESULT_CANCELED, CityActivity.this.getIntent().putExtra("cityname", city_result.get(position).getName()));
                CityActivity.this.finish();
            }
        });
        initOverlay();
        cityInit();
        hotCityInit();
        hisCityInit();
        setAdapter(allCity_lists, city_hot, city_history);


    }

    public void InsertCity(String name) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from recentcity where name = '"
                + name + "'", null);
        if (cursor.getCount() > 0) { //
            db.delete("recentcity", "name = ?", new String[]{name});
        }
        db.execSQL("insert into recentcity(name, date) values('" + name + "', "
                + System.currentTimeMillis() + ")");
        db.close();
    }


    private void cityInit() {
        City city = new City("定位", "0"); // 当前定位城市
        allCity_lists.add(city);
        city = new City("最近", "1"); // 最近访问的城市
        allCity_lists.add(city);
        city = new City("热门", "2"); // 热门城市
        allCity_lists.add(city);
        city = new City("全部", "3"); // 全部城市
        allCity_lists.add(city);
        city_lists = getCityList();
        allCity_lists.addAll(city_lists);
    }

    /**
     * 热门城市
     */
    public void hotCityInit() {
        City city = new City("上海", "2");
        city_hot.add(city);
        city = new City("北京", "2");
        city_hot.add(city);
        city = new City("广州", "2");
        city_hot.add(city);
        city = new City("深圳", "2");
        city_hot.add(city);
        city = new City("武汉", "2");
        city_hot.add(city);
        city = new City("天津", "2");
        city_hot.add(city);
        city = new City("西安", "2");
        city_hot.add(city);
        city = new City("南京", "2");
        city_hot.add(city);
        city = new City("杭州", "2");
        city_hot.add(city);
        city = new City("成都", "2");
        city_hot.add(city);
        city = new City("重庆", "2");
        city_hot.add(city);
    }

    private void hisCityInit() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from recentcity order by date desc limit 0, 3", null);
        while (cursor.moveToNext()) {
            city_history.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
    }

    @SuppressWarnings("unchecked")
    private ArrayList<City> getCityList() {
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<City> list = new ArrayList<City>();
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list, comparator);
        return list;
    }

    @SuppressWarnings("unchecked")
    private void getResultCityList(String keyword) {
        DBHelper dbHelper = new DBHelper(this);
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(
                    "select * from city where name like \"%" + keyword
                            + "%\" or pinyin like \"%" + keyword + "%\"", null);
            City city;
            Log.e("info", "length = " + cursor.getCount());
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                city_result.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(city_result, comparator);
    }

    /**
     * a-z排序
     */
    @SuppressWarnings("rawtypes")
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyi().substring(0, 1);
            String b = rhs.getPinyi().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };

    private void setAdapter(List<City> list, List<City> hotList,
                            List<String> hisCity) {
        adapter = new ListAdapter(this, list, hotList, hisCity);
        personList.setAdapter(adapter);
    }



    private class ResultListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<City> results = new ArrayList<City>();

        public ResultListAdapter(Context context, ArrayList<City> results) {
            inflater = LayoutInflater.from(context);
            this.results = results;
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(results.get(position).getName());
            return convertView;
        }

        class ViewHolder {
            TextView name;
        }
    }

    public class ListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> list;
        private List<City> hotList;
        private List<String> hisCity;
        final int VIEW_TYPE = 5;

        public ListAdapter(Context context, List<City> list,
                           List<City> hotList, List<String> hisCity) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
            this.context = context;
            this.hotList = hotList;
            this.hisCity = hisCity;
            alphaIndexer = new HashMap<String, Integer>();
            sections = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = getAlpha(list.get(i).getPinyi());
                // 上一个汉语拼音首字母，如果不存在为" "
                String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                        .getPinyi()) : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(list.get(i).getPinyi());
                    alphaIndexer.put(name, i);
                    sections[i] = name;
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getItemViewType(int position) {
            return position < 4 ? position : 4;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView city;
            int viewType = getItemViewType(position);
            if (viewType == 0) { // 定位
                convertView = inflater.inflate(R.layout.frist_list_item, null);
                TextView locateHint = convertView
                        .findViewById(R.id.locateHint);
                city = convertView.findViewById(R.id.lng_city);
                city.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locateProcess == 2) {
                            //定位的城市
                            setResult(RESULT_CANCELED, CityActivity.this.getIntent().putExtra("cityname", city.getText().toString()));
                            CityActivity.this.finish();
                        } else if (locateProcess == 3) {
                            locateProcess = 1;
                            personList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            mLocationClient.stop();
                            isNeedFresh = true;
//                            InitLocation();
                            currentCity = "";
//                            mLocationClient.start();
                        }
                    }
                });
                ProgressBar pbLocate = (ProgressBar) convertView
                        .findViewById(R.id.pbLocate);
                if (locateProcess == 1) { // 正在定位
                    locateHint.setText("正在定位");
                    city.setVisibility(View.GONE);
                    pbLocate.setVisibility(View.VISIBLE);
                } else if (locateProcess == 2) { // 定位成功
                    locateHint.setText("当前定位城市");
                    city.setVisibility(View.VISIBLE);
                    city.setText(currentCity);
//                    mLocationClient.stop();
                    pbLocate.setVisibility(View.GONE);
                } else if (locateProcess == 3) {
                    locateHint.setText("未定位到城市,请选择");
                    city.setVisibility(View.VISIBLE);
                    city.setText("重新选择");
                    pbLocate.setVisibility(View.GONE);
                }
            } else if (viewType == 1) { // 最近访问城市
                convertView = inflater.inflate(R.layout.recent_city, null);
                GridView rencentCity = (GridView) convertView
                        .findViewById(R.id.recent_city);
                rencentCity
                        .setAdapter(new HitCityAdapter(context, this.hisCity));
                rencentCity.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //最近访问的城市
                        setResult(RESULT_CANCELED, CityActivity.this.getIntent().putExtra("cityname", city_history.get(position)));
                        CityActivity.this.finish();

                    }
                });
                TextView recentHint = (TextView) convertView
                        .findViewById(R.id.recentHint);
                recentHint.setText("最近访问的城市");
            } else if (viewType == 2) {
                convertView = inflater.inflate(R.layout.recent_city, null);
                GridView hotCity = (GridView) convertView
                        .findViewById(R.id.recent_city);
                hotCity.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //热门的城市
                        setResult(RESULT_CANCELED, CityActivity.this.getIntent().putExtra("cityname", city_hot.get(position).getName()));
                        CityActivity.this.finish();
                    }
                });
                hotCity.setAdapter(new HotCityAdapter(context, this.hotList));
                TextView hotHint = convertView
                        .findViewById(R.id.recentHint);
                hotHint.setText("热门城市");
            } else if (viewType == 3) {
                convertView = inflater.inflate(R.layout.total_item, null);
            } else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item, null);
                    holder = new ViewHolder();
                    holder.alpha = (TextView) convertView
                            .findViewById(R.id.alpha);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position >= 1) {
                    holder.name.setText(list.get(position).getName());
                    String currentStr = getAlpha(list.get(position).getPinyi());
                    String previewStr = (position - 1) >= 0 ? getAlpha(list
                            .get(position - 1).getPinyi()) : " ";
                    if (!previewStr.equals(currentStr)) {
                        holder.alpha.setVisibility(View.VISIBLE);
                        holder.alpha.setText(currentStr);
                    } else {
                        holder.alpha.setVisibility(View.GONE);
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView alpha; // 首字母标题
            TextView name; // 城市名字
        }
    }

    @Override
    protected void onStop() {
//        mLocationClient.stop();
        super.onStop();
    }

    class HotCityAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> hotCitys;

        public HotCityAdapter(Context context, List<City> hotCitys) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.hotCitys = hotCitys;
        }

        @Override
        public int getCount() {
            return hotCitys.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_city, null);
            TextView city = (TextView) convertView.findViewById(R.id.city);
            city.setText(hotCitys.get(position).getName());
            return convertView;
        }
    }

    class HitCityAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<String> hotCitys;

        public HitCityAdapter(Context context, List<String> hotCitys) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.hotCitys = hotCitys;
        }

        @Override
        public int getCount() {
            return hotCitys.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_city, null);
            TextView city = (TextView) convertView.findViewById(R.id.city);
            city.setText(hotCitys.get(position));
            return convertView;
        }
    }

    private boolean mReady;

    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        mReady = true;
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private boolean isScroll = false;

    private class LetterListViewListener implements
            MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            isScroll = false;
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                personList.setSelection(position);
                overlay.setText(s);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1000);
            }
        }
    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定位";
        } else if (str.equals("1")) {
            return "最近";
        } else if (str.equals("2")) {
            return "热门";
        } else if (str.equals("3")) {
            return "全部";
        } else {
            return "#";
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL
                || scrollState == SCROLL_STATE_FLING) {
            isScroll = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!isScroll) {
            return;
        }

        if (mReady) {
            String text;
            String name = allCity_lists.get(firstVisibleItem).getName();
            String pinyin = allCity_lists.get(firstVisibleItem).getPinyi();
            if (firstVisibleItem < 4) {
                text = name;
            } else {
                text = PingYinUtil.converterToFirstSpell(pinyin)
                        .substring(0, 1).toUpperCase();
            }
            overlay.setText(text);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }



    public void location_Sucess(String cityname)
    {
        currentCity = cityname;
        locateProcess = 2; // 定位成功
        personList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //定位失败
    public void location_Faild()
    {
        locateProcess = 3; // 定位失败
        personList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        mLocationCityTask = LocationCityTask.getInstance(this.getApplicationContext());
        mLocationCityTask.setOnLocationGetCityListener(this);
        mLocationCityTask .startSingleLocate();
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    @Override
    public void onLocationGet(PositionEntity entity) {
        Log.e("onLocationGet", "定位详细地址：" + entity.address);
        location_Sucess(entity.city);
    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {
        location_Faild();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mLocationCityTask.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
    }
    //重写系统返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //TODO something
            setResult(RESULT_CANCELED, CityActivity.this.getIntent().putExtra("cityname", "close"));
            CityActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
