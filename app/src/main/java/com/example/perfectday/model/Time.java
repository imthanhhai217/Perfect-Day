package com.example.perfectday.model;

public class Time {
    int hours;
    int minutes;
    int timeZone;

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public Time() {
    }

    public Time(int hours, int minutes, int timeZone) {
        this.hours = hours;
        this.minutes = minutes;
        this.timeZone = timeZone;
    }
}
