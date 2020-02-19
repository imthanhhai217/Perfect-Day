package com.example.perfectday.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.example.perfectday.R;
import com.example.perfectday.adapter.ForecastAdapter;
import com.example.perfectday.asynctask.RequestForecastAsync;
import com.example.perfectday.asynctask.RequestWeatherAsync;
import com.example.perfectday.model.CurrentWeather;
import com.example.perfectday.model.Day;
import com.example.perfectday.model.ForecastWeather;
import com.example.perfectday.ulti.Global;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FiveDaysActivity extends AppCompatActivity {

    private static final String TAG = "FiveDaysActivity";
    private String lon;
    private String lat;
    private RequestForecastAsync requestForecastAsync;
    @BindView(R.id.imgLoading)
    ImageView imgLoading;
    @BindView(R.id.rvListForecast)
    RecyclerView mRvListForecast;
    @BindView(R.id.imgBackHome)
    ImageView imgBackHome;
    @BindView(R.id.tvFiveDays)
    TextView tvFiveDays;

    ForecastAdapter mForecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_days);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ButterKnife.bind(this);

        Intent intent = getIntent();
        lon = intent.getStringExtra("longitude");
        lat = intent.getStringExtra("latitude");

        loadData();

        imgBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void loadData() {
        requestForecastAsync = (RequestForecastAsync) new RequestForecastAsync() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Glide.with(getApplicationContext()).asGif().load(R.drawable.loading_resize
                ).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgLoading);
                imgLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(ForecastWeather forecastWeather) {
                super.onPostExecute(forecastWeather);
                imgLoading.setVisibility(View.GONE);
                Log.d(TAG, "onPostExecute: " + formatDate(forecastWeather.getList().get(0).getDtTxt()));

                ArrayList<Day> days = new ArrayList<>();
                for (int i = 0; i < forecastWeather.getList().size(); i++) {
                    Day day = new Day();
                    day.setDate(formatDate(forecastWeather.getList().get(i).getDtTxt()));
                    day.setTemp(convertToDegrees((int) Double.parseDouble(forecastWeather.getList().get(i).getMain().getTemp())));
                    day.setIcon(forecastWeather.getList().get(i).getWeather().get(0).getIcon());
                    days.add(day);
                }

//                loadData(days);
                mForecastAdapter = new ForecastAdapter(days, getApplicationContext());
                mRvListForecast.setAdapter(mForecastAdapter);
                mRvListForecast.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL));
                mForecastAdapter.notifyDataSetChanged();

                tvFiveDays.setText(forecastWeather.getCity().getName() + "dự báo 5 ngày/3 giờ");
            }
        }.execute(Global.getForecast(lat, lon));
    }

    public String formatDate(String date) {
        int fist = date.indexOf("-");
        String month = date.substring(fist + 1, fist + 3);
        String day = date.substring(fist + 4, fist + 6);
        Log.d(TAG, "formatDate: " + day + "/" + month);
        return day + "/" + month;
    }

    public int convertToDegrees(int kelvin) {
        Log.d(TAG, ": " + kelvin);
        return (int) (kelvin - 273.5);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}