package dev.lab.fnffoodtruckmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Information {

    @SerializedName("id")
    @Expose
    private String id;

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

    public String getId() {
        return id;
    }

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
}
