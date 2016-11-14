package com.example.android.materialdesigncodelab.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.example.android.materialdesigncodelab.R;
import com.example.android.materialdesigncodelab.ui.components.CustomLocationDialog;
import com.example.android.materialdesigncodelab.ui.components.MarqueeFloatWindow;
import com.example.android.materialdesigncodelab.utils.AmapUtils;
import com.example.android.materialdesigncodelab.utils.MockLocationProvider;

import java.util.List;

/**
 * Created by lixindong on 8/8/16.
 */


/**
 * AMapV1地图中简单介绍显示定位小蓝点
 */
public class InstantMapAcitivity extends AppCompatActivity implements AMapLocationListener {
    public static final String QUERY_LONGITUDE = "query_longitude";
    public static final String QUERY_LATITUDE = "query_latitude";
    public static final String NEED_LOCATE = "NEED_LOCATE";
    private AMap aMap;
    private MapView mapView;
    private Marker mMarker = null;
    private AMapLocationClient mlocationClient;
    private LatLng mLatlng;
    private boolean mNeedLocate = true;
    boolean mMocking = false;
    private Intent mMarqueeViewIntent;

    private FloatingActionButton fab;
    private ContentAdapter mContentAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_map);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.instant_map_title);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        double longitude = getIntent().getDoubleExtra(QUERY_LONGITUDE, 0);
        double latitude = getIntent().getDoubleExtra(QUERY_LATITUDE, 0);
        mNeedLocate = getIntent().getBooleanExtra(NEED_LOCATE, true);
        mLatlng = new LatLng(latitude, longitude);
        init();
        initListView();
        if (mNeedLocate) {
            AmapUtils.startLocationOnce(this, this);
        } else {
            try {
                if (!mNeedLocate) {
                    mapView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showMarkerAt(mLatlng);
                        }
                    }, 1000);
                }

                aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(mLatlng));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMarqueeViewIntent = new Intent(this, MarqueeFloatWindow.class);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MockLocationProvider.isRunning(v.getContext())) {
                    MockLocationProvider.stopMock(v.getContext());
                    v.getContext().stopService(mMarqueeViewIntent);
                }
                String text = "Mocking " + mLatlng.longitude + "," + mLatlng.latitude;
                Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show();
                mMarqueeViewIntent.putExtra(MarqueeFloatWindow.TEXT, text);
                v.getContext().startService(mMarqueeViewIntent);

                MockLocationProvider.startMock(v.getContext(), mLatlng.longitude, mLatlng.latitude);
            }
        });
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mLatlng = latLng;
                showMarkerAt(latLng);
            }
        });
    }

    public void showMarkerAt(LatLng latLng) {
        AMapLocation location = new AMapLocation("gps");
        location.setLongitude(latLng.longitude);
        location.setLatitude(latLng.latitude);
        mLatlng = latLng;
        Snackbar.make(mapView, "long click at: " + latLng.longitude + " " + latLng.latitude, Snackbar.LENGTH_SHORT).show();
        if (mMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("pos");
            markerOptions.snippet(latLng.longitude + " " + latLng.latitude);
            mMarker = mapView.getMap().addMarker(markerOptions);
        }
        mMarker.setPosition(latLng);
        mMarker.showInfoWindow();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_stopmock:
                MockLocationProvider.stopMock(this);
                stopService(mMarqueeViewIntent);
                return true;
            case R.id.action_custom_location:
                showCustomLocationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCustomLocationDialog() {
        CustomLocationDialog.showDialog(this, new CustomLocationDialog.OnDialogClickedListener() {
            @Override
            public void onOk(View v, String latitude, String longitude) {
                try {
                    mLatlng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                    showMarkerAt(mLatlng);
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(mLatlng));
                } catch (Exception e) {
                } finally {

                }

            }

            @Override
            public void onCancel(View v) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem setting = menu.findItem(R.id.action_stopmock);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    AmapUtils.inputTips(getApplicationContext(), newText, "", new Inputtips.InputtipsListener() {
                        @Override
                        public void onGetInputtips(List<Tip> list, int i) {
                            mContentAdapter.update(list);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                    });
                }
                return false;
            }
        });
        return true;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null
                && amapLocation.getErrorCode() == 0) {
            mLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
            showMarkerAt(mLatlng);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(mLatlng));
        } else {
            String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
            Log.e("AmapErr",errText);
        }
    }

    /**
     * 停止定位
     */

    RecyclerView mRecyclerView;

    private void initListView() {
        mContentAdapter = new ContentAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_result_list);
        mRecyclerView.setAdapter(mContentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.GONE);
    }


    static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        List<Tip> mList = null;
        InstantMapAcitivity mActivity;

        public ContentAdapter(InstantMapAcitivity acitivity) {
            mActivity = acitivity;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position < getItemCount()) {
                final Tip tip = mList.get(position);
                final String content = mList.get(position).getName();
                holder.mName.setText(content);
                holder.mName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.mLatlng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
                        mActivity.showMarkerAt(mActivity.mLatlng);
                        if (View.VISIBLE == mActivity.mRecyclerView.getVisibility()) {
                            mActivity.mRecyclerView.setVisibility(View.GONE);
                        }
                        mActivity.aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                        mActivity.aMap.moveCamera(CameraUpdateFactory.changeLatLng(mActivity.mLatlng));
                        mActivity.mSearchView.clearFocus();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        public void update(List<Tip> list) {
            mList = list;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_search_result_list, parent, false));
            mName = (TextView) itemView.findViewById(R.id.name);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (View.VISIBLE == mRecyclerView.getVisibility()) {
            mRecyclerView.setVisibility(View.GONE);
        }
    }
}