package dev.lab.fnffoodtruckmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodTruck {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;

    @SerializedName("operator_name")
    @Expose
    private String operatorName;

    @SerializedName("menu")
    @Expose
    private String menu;

    @SerializedName("schedule")
    @Expose
    private String schedule;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getMenu() {
        return menu;
    }

    public String getSchedule() {
        return schedule;
    }
}
