package com.example.perfectday.ulti;

import android.util.Log;

import com.example.perfectday.model.CurrentWeather;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Global {
    public static final String DEGREES = (char) 0x00B0 + "C";
    public static final String URL_TEST = "https://api.openweathermap.org/data/2.5/weather?lat=21.04031599&lon=105.77440535&lang=en&appid=fdfe4db14d94b779bbc518a81f17d9fa";
    public static final String PRESSURE_UNIT = " hPa";

    public static final String LINK_API = "https://api.openweathermap.org/data/2.5/";
    public static final String FORECAST = "forecast?";
    public static double longitude = 0;
    public static double latitude = 0;
    public static final String CURRENT_WEATHER_CITY_NAME = "weather?q=";
    public static final String CURRENT_WEATHER_LOCATION_LAT = "weather?lat=";
    public static final String CURRENT_WEATHER_LOCATION_LONG = "&lon=";
    public static final String API_KEY = "&lang=en&appid=fdfe4db14d94b779bbc518a81f17d9fa";

    public static String getImageLink(String idImage) {
        return "http://openweathermap.org/img/wn/" + idImage + "@2x.png";
    }

    public static String getForecast(String latitude, String longitude) {
        Log.d(TAG, "forecast url : " + LINK_API + FORECAST + "lat=" + latitude + "&lon=" + longitude + API_KEY);
        return LINK_API + FORECAST + "lat=" + latitude + "&lon=" + longitude + API_KEY;
    }
}
