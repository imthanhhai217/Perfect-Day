package com.example.perfectday.model;

import java.util.ArrayList;

public class ExpandItem {
    private ArrayList<Day> days;
    private boolean isExpand;

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public ExpandItem(ArrayList<Day> days, boolean isExpand) {
        this.days = days;
        this.isExpand = isExpand;
    }

    public ExpandItem() {
    }
}
