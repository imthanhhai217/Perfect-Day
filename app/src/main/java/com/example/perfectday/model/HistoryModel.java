package com.example.perfectday.model;

public class HistoryModel {
    String cityName;
    String imgName;
    String temp;
    String humidity;
    String deg;
    String speed;
    String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public HistoryModel(String cityName, String imgName, String temp, String humidity, String deg, String speed, String country) {
        this.cityName = cityName;
        this.imgName = imgName;
        this.temp = temp;
        this.humidity = humidity;
        this.deg = deg;
        this.speed = speed;
        this.country = country;
    }

    public HistoryModel() {
    }
}
