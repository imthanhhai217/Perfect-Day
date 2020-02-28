package com.example.perfectday.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.perfectday.R;
import com.example.perfectday.activity.FiveDaysActivity;
import com.example.perfectday.asynctask.RequestWeatherAsync;
import com.example.perfectday.model.CurrentWeather;
import com.example.perfectday.ulti.Global;
import com.github.tianma8023.model.Time;
import com.github.tianma8023.ssv.SunriseSunsetView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class CurrentWeatherFragment extends Fragment {

    private static final String TAG = "CurrentWeatherFragment";
    private RequestWeatherAsync requestWeatherAsync;
    private String url;
    public int longitude;
    public int latitude;

    @BindView(R.id.tvTemp)
    TextView tvTemp;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvCityName)
    TextView tvCityName;
    @BindView(R.id.tvSpeed)
    TextView tvSpeed;
    @BindView(R.id.tvFeelsLike)
    TextView tvFeelsLike;
    @BindView(R.id.tvHumidity)
    TextView tvHumidity;
    @BindView(R.id.tvPressure)
    TextView tvPressure;
    @BindView(R.id.ssv)
    SunriseSunsetView ssv;
    @BindView(R.id.tvSunrise)
    TextView tvSunrise;
    @BindView(R.id.tvSunset)
    TextView tvSunset;
    @BindView(R.id.tvFiveDays)
    TextView tvFiveDays;
    @BindView(R.id.imgLoading)
    ImageView imgLoading;

    public static CurrentWeatherFragment newInstance(int position, String cityName) {
        Bundle args = new Bundle();
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        args.putInt("position", position);
        args.putString("cityName", cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_all_infomation, container, false);
        ButterKnife.bind(this, view);

        url = Global.LINK_API + Global.CURRENT_WEATHER_CITY_NAME + getArguments().getString("cityName").trim() + Global.API_KEY;

        loadData(url);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void loadData(String url) {
        requestWeatherAsync = (RequestWeatherAsync) new RequestWeatherAsync() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Glide.with(getContext()).asGif().load(R.drawable.loading_resize
                ).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgLoading);
                imgLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(CurrentWeather currentWeather) {
                super.onPostExecute(currentWeather);
                if (currentWeather.getCod() == 200) {
                    imgLoading.setVisibility(View.GONE);

                    //TODO city
                    tvDescription.setText(currentWeather.getWeather().get(0).getDescription());
                    tvTemp.setText(currentWeather.getMain().getTemp() + (char) 0x00B0 + "");
                    tvCityName.setText(currentWeather.getName());

                    //TODO wind
                    String speed = currentWeather.getWind().getSpeed();
                    String feelsLike = currentWeather.getMain().getFeelsLike() + Global.DEGREES;
                    String humidity = currentWeather.getMain().getHumidity() + " %";
                    String pressure = currentWeather.getMain().getPressure();
                    tvSpeed.setText(speed);
                    tvFeelsLike.setText(feelsLike);
                    tvHumidity.setText(humidity);
                    tvPressure.setText(pressure);

                    //TODO Sunrise & sunset
                    int hoursRise = getHours(Long.parseLong(currentWeather.getSys().getSunrise() + ""));
                    int minuteRise = getMinute(Long.parseLong(currentWeather.getSys().getSunrise() + ""));
                    int hoursSet = getHours(Long.parseLong(currentWeather.getSys().getSunset() + ""));
                    int minuteSet = getMinute(Long.parseLong(currentWeather.getSys().getSunset() + ""));
                    Log.d(TAG, "onPostExecute: rise : " + currentWeather.getSys().getSunrise() + " set : " + currentWeather.getSys().getSunset());
                    tvSunrise.setText(hoursRise + "giờ" + minuteRise + "phút");
                    tvSunset.setText(hoursSet + "giờ" + minuteSet + "phút");
                    ssv.setSunriseTime(new Time(hoursRise, minuteRise));
                    ssv.setSunsetTime(new Time(hoursSet, minuteSet));
                    ssv.startAnimate();

                    //TODO 5 days
                    String longitude = currentWeather.getCoord().getLon();
                    String latitude = currentWeather.getCoord().getLat();
                    getLatLong(latitude, longitude);
                } else {
                    Toasty.error(getContext(), "Có lỗi khi tải dữ liệu !").show();
                }
            }
        }.execute(url);
    }

    public void getLatLong(String lat, String lon) {
        tvFiveDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FiveDaysActivity.class);
                intent.putExtra("latitude", lat);
                intent.putExtra("longitude", lon);
                startActivity(intent);
            }
        });
    }

    public int getHours(long epochTime) {
        int re;
        Date date = new Date(epochTime * 1000);
        DateFormat format = new SimpleDateFormat("HH");
        Calendar calendar = Calendar.getInstance();
        format.setTimeZone(calendar.getTimeZone());
        String formatted = format.format(date);
        re = Integer.parseInt(formatted);
        return re;
    }

    public int getMinute(long epochTime) {
        int re;
        Date date = new Date(epochTime * 1000);
        DateFormat format = new SimpleDateFormat("HH");
        Calendar calendar = Calendar.getInstance();
        format.setTimeZone(calendar.getTimeZone());
        String formatted = format.format(date);
        re = Integer.parseInt(formatted);
        return re;
    }
}
