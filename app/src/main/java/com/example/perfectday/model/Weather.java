
package com.example.perfectday.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = transtale(description);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String transtale(String description) {
        String re = "";
        switch (description.trim()) {
            case "clear sky":
                re = "Không mây";
                break;
            case "few clouds":
                re = "ít mây";
                break;
            case "scattered clouds":
                re = "Mây rải rác";
                break;
            case "broken clouds":
                re = "Mây rải rác, nắng nhẹ";
                break;
            case "shower rain":
                re = "Mưa nhỏ";
                break;
            case "rain":
                re = "mưa";
                break;
            case "thunderstorm":
                re = "Giông";
                break;
            case "snow":
                re = "Tuyết rơi";
                break;
            case "mist":
                re = "Sương mù";
                break;
            default:
                re = description.trim();
                break;
        }
        return re;
    }
}
