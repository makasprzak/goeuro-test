package com.goeuro.makasprzak;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("_id")
    private Long id;
    private String name;
    private String type;
    @SerializedName("geo_position")
    private GeoPosition geoPosition;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }
}
