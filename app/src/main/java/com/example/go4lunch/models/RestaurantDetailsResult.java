package com.example.go4lunch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RestaurantDetailsResult implements Serializable {
    @SerializedName("name")
    private String detailName;
    @SerializedName("opening_hours")
    private OpeningHours detailOpeningHours;
    @SerializedName("photos")
    private List<Photo> detailPhotos;
    @SerializedName("rating")
    private Double detailRating;
    @SerializedName("vicinity")
    private String detailVicinity;
    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;
    @SerializedName("geometry")
    private Geometry detailGeometry;
    @SerializedName("website")
    private String website;
    @SerializedName("place_id")
    private String detailPlaceId;
    @SerializedName("id")
    private String detailId;
    @SerializedName("reference")
    private String detailReference;

    public String getLikedRestaurant() {
        return likedRestaurant;
    }

    public void setLikedRestaurant(String likedRestaurant) {
        this.likedRestaurant = likedRestaurant;
    }

    private String likedRestaurant;



    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public OpeningHours getDetailOpeningHours() {
        return detailOpeningHours;
    }

    public void setDetailOpeningHours(OpeningHours detailOpeningHours) {
        this.detailOpeningHours = detailOpeningHours;
    }

    public List<Photo> getDetailPhotos() {
        return detailPhotos;
    }

    public void setDetailPhotos(List<Photo> detailPhotos) {
        this.detailPhotos = detailPhotos;
    }

    public Double getDetailRating() {
        return detailRating;
    }

    public void setDetailRating(Double detailRating) {
        this.detailRating = detailRating;
    }

    public String getDetailVicinity() {
        return detailVicinity;
    }

    public void setDetailVicinity(String detailVicinity) {
        this.detailVicinity = detailVicinity;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public Geometry getDetailGeometry() {
        return detailGeometry;
    }

    public void setDetailGeometry(Geometry detailGeometry) {
        this.detailGeometry = detailGeometry;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDetailPlaceId() {
        return detailPlaceId;
    }

    public void setDetailPlaceId(String detailPlaceId) {
        this.detailPlaceId = detailPlaceId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getDetailReference() {
        return detailReference;
    }

    public void setDetailReference(String detailReference) {
        this.detailReference = detailReference;
    }
}
