package com.example.perfectday.model;

public class Day {
    double temp;
    String date;
    String description;
    String icon;
    String hours;

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

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


    public Day(double temp, String date, String description, String icon, String hours) {
        this.temp = temp;
        this.date = date;
        this.description = description;
        this.icon = icon;
        this.hours = hours;
    }

    public Day() {
    }
}
