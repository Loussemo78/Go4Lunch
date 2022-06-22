package com.example.go4lunch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantsResult {
    @SerializedName("open_now")
    private Boolean openNow;
    @SerializedName("geometry")
    private Geometry geometry;
    @SerializedName("icon")
    private String icon;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("opening_hours")
    private OpeningHours openingHours;
    @SerializedName("rating")
    private Float rating;
    @SerializedName("photos")
    private List<Photo> photos;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("reference")
    private String reference;
    @SerializedName("types")
    private List<String> types;
    @SerializedName("vicinity")
    private String vicinity;
    private String userChoice;

    public String getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(String userChoice) {
        this.userChoice = userChoice;
    }

    private int userInterested;


    public int getUserInterested() {
        return userInterested;
    }

    public void setUserInterested(int userInterested) {
        this.userInterested = userInterested;
    }


    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }


    @Override
    public String toString() {
        return "ResultPlaces{" +
                "geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", openingHours=" + openingHours +
                ", photos=" + photos +
                ", placeId='" + placeId + '\'' +
                ", reference='" + reference + '\'' +
                ", types=" + types +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }
}
