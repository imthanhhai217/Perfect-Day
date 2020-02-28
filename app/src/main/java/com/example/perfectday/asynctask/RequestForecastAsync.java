package com.example.perfectday.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.perfectday.model.City;
import com.example.perfectday.model.Clouds;
import com.example.perfectday.model.Coord;
import com.example.perfectday.model.Coordinate;
import com.example.perfectday.model.CurrentWeather;
import com.example.perfectday.model.ForecastWeather;
import com.example.perfectday.model.List;
import com.example.perfectday.model.Main;
import com.example.perfectday.model.Rain;
import com.example.perfectday.model.Weather;
import com.example.perfectday.model.Wind;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.resolveSize;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class RequestForecastAsync extends AsyncTask<String, Void, ForecastWeather> {
    @Override
    protected ForecastWeather doInBackground(String... strings) {
        return getForecastWeather(loadJsonString(strings[0]));
    }

    @Override
    protected void onPostExecute(ForecastWeather forecastWeather) {
        super.onPostExecute(forecastWeather);
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

    public ForecastWeather getForecastWeather(String jsonForecast) {
        ForecastWeather model = new ForecastWeather();
        model.setCod(getCod(jsonForecast));
        model.setCity(getCity(jsonForecast));
        model.setList(getList(jsonForecast));
        return model;
    }

    public int getCod(String jsonForecast) {
        int cod = -1;
        try {
            JSONObject jsonObject = new JSONObject(jsonForecast);
            cod = jsonObject.getInt("cod");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cod;
    }

    public ArrayList<List> getList(String jsonForecast) {
        ArrayList<List> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonForecast);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                List objectList = new List();
                JSONObject object = jsonArray.getJSONObject(i);

                //TODO Main
                Main main = new Main();
                JSONObject mainJS = object.getJSONObject("main");
                main.setTemp(mainJS.getString("temp"));
                main.setFeelsLike(mainJS.getString("feels_like"));
                main.setTempMin(mainJS.getString("temp_min"));
                main.setTempMax(mainJS.getString("temp_max"));
                main.setPressure(mainJS.getString("pressure"));
                main.setSeaLevel(mainJS.getString("sea_level"));
                main.setGrnd_level(mainJS.getString("grnd_level"));
                main.setHumidity(mainJS.getString("humidity"));
                objectList.setMain(main);

                //TODO Weather
                ArrayList<Weather> weatherArr = new ArrayList<>();
                JSONArray weatherJS = object.getJSONArray("weather");
                for (int j = 0; j < weatherJS.length(); j++) {
                    Weather weather = new Weather();
                    JSONObject weatherObject = weatherJS.getJSONObject(j);

                    weather.setId(weatherObject.getString("id"));
                    weather.setMain(weatherObject.getString("main"));
                    weather.setDescription(weatherObject.getString("description"));
                    weather.setIcon(weatherObject.getString("icon"));
                    weatherArr.add(weather);
                }
                objectList.setWeather(weatherArr);

                //TODO Clouds
                Clouds clouds = new Clouds();
                JSONObject cloudsJS = object.getJSONObject("clouds");
                clouds.setAll(cloudsJS.getInt("all"));
                objectList.setClouds(clouds);

                //TODO wind
                try {
                    Wind wind = new Wind();
                    JSONObject windJS = object.getJSONObject("wind");
                    try {
                        wind.setSpeed(windJS.getString("speed"));
                    } catch (Exception e) {
                        wind.setSpeed(0 + "");
                    }
                    try {
                        wind.setDeg(windJS.getString("deg"));
                    } catch (Exception e) {
                        wind.setDeg(0 + "");
                    }
                    objectList.setWind(wind);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //TODO Rain
                Rain rain = new Rain();
                try {
                    JSONObject rainJS = object.getJSONObject("rain");
                    rain.set3h(rainJS.getDouble("3h"));
                    objectList.setRain(rain);
                } catch (Exception e) {
                    e.printStackTrace();
                    rain.set3h(0.0);
                }

                String dt_txt = object.getString("dt_txt");
                objectList.setDtTxt(dt_txt);

                int dt = object.getInt("dt");
                objectList.setDt(dt);

                list.add(objectList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public City getCity(String jsonForecast) {
        City city = new City();
        try {
            JSONObject object = new JSONObject(jsonForecast);
            JSONObject cityJS = object.getJSONObject("city");
            city.setId(cityJS.getString("id"));
            city.setName(cityJS.getString("name"));
            city.setCountry(cityJS.getString("country"));

            JSONObject coordJS = object.getJSONObject("coord");
            Coordinate coord = new Coordinate(coordJS.getString("lon"), coordJS.getString("lat"));
            city.setCoord(coord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }
}
