package com.example.perfectday.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.perfectday.model.Clouds;
import com.example.perfectday.model.Coord;
import com.example.perfectday.model.CurrentWeather;
import com.example.perfectday.model.Main;
import com.example.perfectday.model.Sys;
import com.example.perfectday.model.Weather;
import com.example.perfectday.model.Wind;
import com.example.perfectday.ulti.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RequestWeatherAsync extends AsyncTask<String, Void, CurrentWeather> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected CurrentWeather doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: ");
        return getCurrentWeather(loadJsonString(strings[0]));
    }

    @Override
    protected void onPostExecute(CurrentWeather currentWeather) {
        super.onPostExecute(currentWeather);
    }

    public String loadJsonString(String url) {
        String res = "";
        try {
            URL mUrl = new URL(url);
            URLConnection urlConnection = mUrl.openConnection();
            InputStream is = urlConnection.getInputStream();
            int byteChar;
            while ((byteChar = is.read()) != -1) {
                res += (char) byteChar;
            }
            Log.d(TAG, "loadJsonString: " + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public CurrentWeather getCurrentWeather(String jsonWeather) {
        CurrentWeather mCurrentWeather = new CurrentWeather();
        mCurrentWeather.setCoord(getCoord(jsonWeather));
        mCurrentWeather.setWeather(getWeather(jsonWeather));
        mCurrentWeather.setMain(getMain(jsonWeather));
        mCurrentWeather.setWind(getWind(jsonWeather));
        mCurrentWeather.setClouds(getClouds(jsonWeather));
        mCurrentWeather.setSys(getSys(jsonWeather));
        mCurrentWeather.setTimezone(getTimezone(jsonWeather));
        mCurrentWeather.setName(getName(jsonWeather));
        mCurrentWeather.setCod(getCod(jsonWeather));
        return mCurrentWeather;
    }

    public List<Weather> getWeather(String jsonWeather) {
        List<Weather> weatherList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonWeather);
            JSONArray weatherArr = jsonObject.getJSONArray("weather");
            for (int i = 0; i < weatherArr.length(); i++) {
                Weather weather = new Weather();
                JSONObject weatherJs = weatherArr.getJSONObject(i);
                weather.setId(weatherJs.getString("id"));
                weather.setMain(weatherJs.getString("main"));
                weather.setDescription(weatherJs.getString("description"));
                weather.setIcon(weatherJs.getString("icon"));
                weatherList.add(weather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherList;
    }

    public Coord getCoord(String jsonWeather) {
        Coord coord = new Coord();
        try {
            JSONObject jsonObject = new JSONObject(jsonWeather);
            JSONObject coordJS = jsonObject.getJSONObject("coord");
            coord.setLat(coordJS.getString("lat"));
            coord.setLon(coordJS.getString("lon"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coord;
    }

    public Main getMain(String jsonWeather) {
        Main main = new Main();
        try {
            JSONObject jsonObject = new JSONObject(jsonWeather);
            JSONObject mainJs = jsonObject.getJSONObject("main");
            main.setTemp(convertToDegrees(mainJs.getInt("temp")) + "");
            main.setFeelsLike(convertToDegrees(mainJs.getInt("feels_like")) + "");
            main.setTempMin(convertToDegrees(mainJs.getInt("temp_min")) + "");
            main.setTempMax(convertToDegrees(mainJs.getInt("temp_max")) + "");
            main.setPressure(mainJs.getInt("pressure") + "");
            main.setHumidity(mainJs.getInt("humidity") + "");
            Log.d(TAG, "getMain: " + main.getTemp());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return main;
    }

    public Wind getWind(String jsonWeather) {
        Wind wind = new Wind();
        try {
            JSONObject jsonObject = new JSONObject(jsonWeather);
            JSONObject windJS = jsonObject.getJSONObject("wind");
            wind.setSpeed(windJS.getString("speed"));
            wind.setDeg(windJS.getString("deg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wind;
    }

    public Clouds getClouds(String jsonWeather) {
        Clouds clouds = new Clouds();
        try {
            JSONObject jsonObject = new JSONObject(jsonWeather);
            JSONObject cloudsJS = jsonObject.getJSONObject("clouds");
            clouds.setAll(cloudsJS.getInt("all"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return clouds;
    }

    public Sys getSys(String jsonWeather) {
        Sys sys = new Sys();
        try {
            JSONObject jsonObject = new JSONObject(jsonWeather);
            JSONObject sysJS = jsonObject.getJSONObject("sys");
            sys.setCountry(sysJS.getString("country"));
            sys.setSunrise(sysJS.getInt("sunrise"));
            sys.setSunset(sysJS.getInt("sunset"));
            sys.setId(sysJS.getInt("id"));
            sys.setType(sysJS.getString("type"));
            Log.d(TAG, "getSys: type : "+sys.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sys;
    }

    public String getName(String jsonWeather) {
        String res = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonWeather);
            res = jsonObject.getString("name");
            Log.d(TAG, "getName: res : " + res);
        } catch (JSONException e) {
            e.printStackTrace();
            res = "N/A";
        }
        return res;
    }

    public int getTimezone(String jsonWeather) {
        int res = 0;
        try {
            JSONObject jsonObject = new JSONObject(jsonWeather);
            res = jsonObject.getInt("timezone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public int getCod(String jsonWeather) {
        int res2 = 0;
        try {
            JSONObject jsonObject = new JSONObject(jsonWeather);
            res2 = jsonObject.getInt("cod");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res2;
    }

    public int convertToDegrees(int kelvin) {
        Log.d(TAG, ": " + kelvin);
        return (int) (kelvin - 273.5);
    }
}
