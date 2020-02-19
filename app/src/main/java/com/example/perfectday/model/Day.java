package com.example.perfectday.model;

public class Day {
    double temp;
    String date;
    String description;
    String icon;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public Day(int temp, String date, String description) {
        this.temp = temp;
        this.date = date;
        this.description = description;
    }

    public Day() {
    }
}
