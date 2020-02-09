
package com.example.perfectday.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Main {

    @SerializedName("temp")
    @Expose
    private String temp;
    @SerializedName("feels_like")
    @Expose
    private String feelsLike;
    @SerializedName("temp_min")
    @Expose
    private String tempMin;
    @SerializedName("temp_max")
    @Expose
    private String tempMax;
    @SerializedName("pressure")
    @Expose
    private String pressure;
    @SerializedName("humidity")
    @Expose
    private String humidity;

    public String getTemp() {
        Log.d(TAG, "getTemp: " + temp);
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

}
