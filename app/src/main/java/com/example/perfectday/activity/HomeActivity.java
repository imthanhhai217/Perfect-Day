package com.example.perfectday.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.perfectday.R;
import com.example.perfectday.adapter.CurrentWeatherAdapter;
import com.example.perfectday.asynctask.RequestWeatherAsync;
import com.example.perfectday.database.HistoryDatabase;
import com.example.perfectday.effect.DepthPageTransformer;
import com.example.perfectday.model.CurrentWeather;
import com.example.perfectday.ulti.Global;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;

public class HomeActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int MAX_PAGE = 3;
    private static final String TAG = "HomeActivity";
    private RequestWeatherAsync requestWeatherAsync;
    @Nullable
    @BindView(R.id.vpMain)
    ViewPager mVPMain;
    @Nullable
    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;
    @Nullable
    @BindView(R.id.btnManageCity)
    ImageView btnAddCity;
    @Nullable
    @BindView(R.id.btnLocation)
    ImageView btnLocation;


    private static int currentPagePosition = -1;
    CurrentWeatherAdapter mPagerAdapter;
    private ArrayList<String> mListData;

    public final int REQUEST_CITY_SELECT = 97;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        ButterKnife.bind(this);

        mListData = new ArrayList<>();
        loadData();

        btnAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ManageListCityActivity.class);
                intent.putStringArrayListExtra("listName", mListData);
                startActivityForResult(intent, REQUEST_CITY_SELECT);
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getLocationDone == true) {
                    Global.latitude = mLastLocation.getLatitude();
                    Global.longitude = mLastLocation.getLongitude();
                    getWeatherLocation(Global.latitude, Global.longitude);
                } else {
                    Toasty.warning(getBaseContext(), "Đang lấy vị trí ...").show();
                    getMyLocation();
                }
            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    public void initDatabase() {
        HistoryDatabase historyDatabase = new HistoryDatabase(getApplicationContext());
        mListData = historyDatabase.getListLocation();
        if (mListData.size() > 0) {
            Log.d(TAG, "initDatabase: " + mListData.size());
        } else {
            //TODO update database
            mListData.add("Hà Nội");
            historyDatabase.updateData(mListData);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    public void loadData() {
        initDatabase();
        mPagerAdapter = null;
        mPagerAdapter = new CurrentWeatherAdapter(getSupportFragmentManager(), mListData, HomeActivity.this);
        mVPMain.setAdapter(mPagerAdapter);
        mVPMain.setOffscreenPageLimit(5);
        mVPMain.setPageTransformer(true, new DepthPageTransformer());
        mPagerAdapter.notifyDataSetChanged();

        circleIndicator.createIndicators(mListData.size(), 0);
        circleIndicator.setViewPager(mVPMain);
        mPagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    public void loadData(int position) {
        initDatabase();
        mPagerAdapter = null;
        mPagerAdapter = new CurrentWeatherAdapter(getSupportFragmentManager(), mListData, HomeActivity.this);
        mVPMain.setAdapter(mPagerAdapter);
        mVPMain.setOffscreenPageLimit(5);
        mVPMain.setPageTransformer(true, new DepthPageTransformer());
        mPagerAdapter.notifyDataSetChanged();

        circleIndicator.createIndicators(mListData.size(), position);
        circleIndicator.setViewPager(mVPMain);
        mPagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        mVPMain.setCurrentItem(position, true);
    }

    @Override
    public void onBackPressed() {
        if (mVPMain.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mVPMain.setCurrentItem(mVPMain.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CITY_SELECT) {
            if (resultCode == RESULT_OK) {
                String cityName;
                String newCity;
                if (data.getStringExtra("cityName") != null) {
                    cityName = data.getStringExtra("cityName");
                    int position = getPosition(cityName);
                    loadData(position);
                }
                if (data.getStringExtra("newCity") != null) {
                    newCity = data.getStringExtra("newCity");
                    addCity(newCity);
                }
            } else {
                Log.d(TAG, "onActivityResult: RESULT_CANCELED");
                loadData();
            }
        }
    }

    public int getPosition(String name) {
        int re = -1;
        for (int i = 0; i < mListData.size(); i++) {
            Log.d(TAG, "getPosition: " + name);
            if (name.trim().equals(mListData.get(i))) {
                Log.d(TAG, "getPosition: cityName : " + name + " in " + i);
                re = i;
                break;
            }
        }
        return re;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled: " + position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    //TODO LOCATION
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 21;

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            //------------------------------------------------------------------------------
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            getLocationDone = true;
//            getWeatherLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    }

    boolean getLocationDone = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnLocation.setVisibility(View.VISIBLE);
                    Toast.makeText(HomeActivity.this,
                            "Đã cấp quyền truy cập vị trí địa lí",
                            Toast.LENGTH_LONG).show();
                    getMyLocation();
                } else {
                    Toast.makeText(HomeActivity.this,
                            "Vui lòng cấp quyền để sử dụng hành động này",
                            Toast.LENGTH_LONG).show();
                    btnLocation.setVisibility(View.GONE);
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        getMyLocation();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(HomeActivity.this,
                "onConnectionSuspended: " + String.valueOf(i),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(HomeActivity.this,
                "onConnectionFailed: " + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
    }

    public void getWeatherLocation(double latitude, double longitude) {
        String url = Global.LINK_API + Global.CURRENT_WEATHER_LOCATION_LAT + latitude + Global.CURRENT_WEATHER_LOCATION_LONG + longitude + Global.API_KEY;
        Log.d(TAG, "getWeatherLocation: url : " + url);
        requestWeatherAsync = (RequestWeatherAsync) new RequestWeatherAsync() {
            @Override
            protected void onPostExecute(CurrentWeather currentWeather) {
                super.onPostExecute(currentWeather);
                loadDataLocation(currentWeather);
            }
        }.execute(url);
    }

    public boolean checkLocationIsExit(String cityName) {
        boolean re = false;
        for (int i = 0; i < mListData.size(); i++) {
            Log.d(TAG, "checkLocationIsExit: " + cityName + " " + (mListData.get(i)));
            if (cityName.trim().equals(mListData.get(i))) {
                re = true;
                break;
            }
        }
        Log.d(TAG, "checkLocationIsExit: " + re);
        return re;
    }

    public int getIndex(String cityName) {
        int re = -1;
        for (int i = 0; i < mListData.size(); i++) {
            if (cityName.trim().equals(mListData.get(i))) {
                re = i;
                break;
            }
        }
        Log.d(TAG, "getIndex: " + re);
        return re;
    }

    public void addCity(String cityName) {
        if (mListData.size() <= 10) {
            Log.d(TAG, "addCity: add new city ");
            mListData.add(0, cityName);
            updateData(mListData);
            loadData();
        } else {
            Log.d(TAG, "addCity: remove lastest location and add new city");
            mListData.remove(mListData.size() - 1);
            mListData.add(0, cityName);
            updateData(mListData);
            loadData();
        }
    }

    //TODO Cập nhật lại database
    public void updateData(ArrayList<String> data) {
        HistoryDatabase historyDatabase = new HistoryDatabase(getApplicationContext());
        historyDatabase.updateData(data);
    }

    public void moveCurrentLocationToFist(int position, String name) {
        mListData.remove(position);
        mListData.add(0, name);
        updateData(mListData);
        loadData();
    }

    public void loadDataLocation(CurrentWeather currentWeather) {
        if (checkLocationIsExit(currentWeather.getName()) == false) {
            //Nếu chưa lưu vị trí hiện tại trước đây thì tạo mới vị trí hiện tại
            addCity(currentWeather.getName());
        } else {
            //Chuyển trang có vị trí hiện tại lên đầu tiên
            moveCurrentLocationToFist(getIndex(currentWeather.getName()), currentWeather.getName());
        }
        Toasty.success(getBaseContext(), "Vị trí hiện tại của bạn là " + currentWeather.getName()).show();
        mPagerAdapter = new CurrentWeatherAdapter(getSupportFragmentManager(), mListData, HomeActivity.this);
        mVPMain.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
    }
}
