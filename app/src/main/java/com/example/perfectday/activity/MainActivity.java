package com.example.perfectday.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.perfectday.R;
import com.example.perfectday.asynctask.RequestWeatherAsync;
import com.example.perfectday.fragment.CityWeatherFragment;
import com.example.perfectday.fragment.WindFragment;
import com.example.perfectday.model.CurrentWeather;
import com.example.perfectday.ulti.Global;

public class MainActivity extends AppCompatActivity {

    RequestWeatherAsync requestWeatherAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loadDataCurrentWeather();
        loadDataWindFragment();

    }


    public void loadDataCurrentWeather() {
        CityWeatherFragment cityWeather = new CityWeatherFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.cityFrame, cityWeather).commit();
    }

    public void loadDataWindFragment() {
        requestWeatherAsync = (RequestWeatherAsync) new RequestWeatherAsync() {
            @Override
            protected void onPostExecute(CurrentWeather currentWeather) {
                super.onPostExecute(currentWeather);
                String speed = currentWeather.getWind().getSpeed() + " km/h";
                String feelsLike = currentWeather.getMain().getFeelsLike() + Global.DEGREES;
                String humidity = currentWeather.getMain().getHumidity() + " %";
                String pressure = currentWeather.getMain().getPressure() + " atm";

                FragmentManager fragmentManager = getSupportFragmentManager();
                WindFragment windFragment = WindFragment.newInstance(speed, feelsLike, humidity, pressure);
                fragmentManager.beginTransaction().replace(R.id.windFrame1, windFragment).commit();
                WindFragment windFragment1 = WindFragment.newInstance(speed, feelsLike, humidity, pressure);
                fragmentManager.beginTransaction().replace(R.id.windFrame2, windFragment1).commit();
                WindFragment windFragment2 = WindFragment.newInstance(speed, feelsLike, humidity, pressure);
                fragmentManager.beginTransaction().replace(R.id.windFrame3, windFragment2).commit();
                WindFragment windFragment3 = WindFragment.newInstance(speed, feelsLike, humidity, pressure);
                fragmentManager.beginTransaction().replace(R.id.windFrame4, windFragment3).commit();
            }
        }.execute(Global.URL_TEST);
    }
}
