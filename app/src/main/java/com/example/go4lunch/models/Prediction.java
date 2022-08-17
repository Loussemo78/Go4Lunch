package com.example.go4lunch.models;

import androidx.core.util.ObjectsCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Prediction {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("place_id")
    @Expose
    private String placeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prediction that = (Prediction) o;
        return ObjectsCompat.equals(description, that.description) && ObjectsCompat.equals(id, that.id) && ObjectsCompat.equals(placeId, that.placeId);
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(description, id, placeId);
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return description ;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
