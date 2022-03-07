package com.example.go4lunch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantsInfo {
    @SerializedName("html attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("results")
    @Expose
    private List<RestaurantsResult> results = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("name")
    private String name;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("opening_hours")
    private OpeningHours openingHours;
    @SerializedName("vicinity")
    private String vicinity;
    @SerializedName("rating")
    private Float rating;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @SerializedName("photos")
    private List<Photo> photos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }




    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<RestaurantsResult> getResults() {
        return results;
    }

    public void setResults(List<RestaurantsResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "MapPlacesInfo{" +
                "htmlAttributions=" + htmlAttributions +
                ", results=" + results +
                ", status='" + status + '\'' +
                '}';
    }
}
