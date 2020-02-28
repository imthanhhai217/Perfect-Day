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
import com.example.perfectday.adapter.ParentAdapter;
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
    ParentAdapter mParentAdapter;

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

                ArrayList<Day> days = new ArrayList<>();
                ArrayList<String> listDays = new ArrayList<>();
                for (int i = 0; i < forecastWeather.getList().size(); i++) {
                    Day day = new Day();

                    long epoch = forecastWeather.getList().get(i).getDt();
                    forecastWeather.getList().get(i).setDayOfWeek(epoch);
                    String dayOfWeek = forecastWeather.getList().get(i).getDayOfWeek();
                    day.setDate(formatDate(forecastWeather.getList().get(i).getDtTxt()));
                    listDays.add(day.getDate());

                    day.setHours(getHours(forecastWeather.getList().get(i).getDt()));

                    day.setTemp(convertToDegrees((int) Double.parseDouble(forecastWeather.getList().get(i).getMain().getTemp())));

                    day.setIcon(forecastWeather.getList().get(i).getWeather().get(0).getIcon());

                    day.setDescription(forecastWeather.getList().get(i).getWeather().get(0).getDescription());

                    days.add(day);
                }

                ArrayList<String> miniList = new ArrayList<>();
                ArrayList<String> listDaysOfWeek = new ArrayList<>();
                for (int i = 0; i < listDays.size(); i++) {
                    if (!miniList.contains(listDays.get(i))) {
                        miniList.add(listDays.get(i));
                        listDaysOfWeek.add(forecastWeather.getList().get(i).getDayOfWeek());
                    }
                }
                Log.d(TAG, "onPostExecute: dayOfWeek : " + listDaysOfWeek.get(listDaysOfWeek.size() - 1));

                mParentAdapter = new ParentAdapter(miniList, days, getApplicationContext());
//                loadData(days);
//                mForecastAdapter = new ForecastAdapter(days, getApplicationContext());
//                mRvListForecast.setAdapter(mForecastAdapter);
//                mRvListForecast.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL));
//                mForecastAdapter.notifyDataSetChanged();
                mRvListForecast.setAdapter(mParentAdapter);
                mRvListForecast.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                mParentAdapter.notifyDataSetChanged();

                tvFiveDays.setText(forecastWeather.getCity().getName() + " dự báo 5 ngày/3 giờ");
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

    public String getHours(long epochTime) {
        Date date = new Date(epochTime * 1000);
        DateFormat format = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        format.setTimeZone(calendar.getTimeZone());
        String formatted = format.format(date);
        return formatted;
    }

    public int convertToDegrees(int kelvin) {
        Log.d(TAG, ": " + kelvin);
        return (int) (kelvin - 273.5);
    }

    public String getDateTranslate(Long date) {
        String re = "";
        Date d = new Date(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                re = "Chủ nhật,";
                break;
            case 2:
                re = "Thứ hai, ";
                break;
            case 3:
                re = "Thứ ba, ";
                break;
            case 4:
                re = "Thứ tư, ";
                break;
            case 5:
                re = "Thứ năm, ";
                break;
            case 6:
                re = "Thứ sáu, ";
                break;
            case 7:
                re = "Thứ bảy, ";
                break;
            default:
                re = "Thứ 2";
                break;
        }
        calendar.clear();
        return re;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
