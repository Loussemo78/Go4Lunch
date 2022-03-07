package com.example.go4lunch.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RestaurantDetail implements Serializable {
    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;
    @SerializedName("result")
    private RestaurantDetailsResult result;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("photo")
    private List<Photo> detailPhotos;
    @SerializedName("vicinity")
    private String detailVicinity;
    @SerializedName("place_id")
    private String detailPlaceId;

    public String getDetailPlaceId() {
        return detailPlaceId;
    }

    public void setDetailPlaceId(String detailPlaceId) {
        this.detailPlaceId = detailPlaceId;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    @SerializedName("name")
    private String detailName;

    public List<Photo> getDetailPhotos() {
        return detailPhotos;
    }

    public void setDetailPhotos(List<Photo> detailPhotos) {
        this.detailPhotos = detailPhotos;
    }

    public String getDetailVicinity() {
        return detailVicinity;
    }

    public void setDetailVicinity(String detailVicinity) {
        this.detailVicinity = detailVicinity;
    }

    public List<Photo> getDetailPhoto() {
        return detailPhoto;
    }

    public void setDetailPhoto(List<Photo> detailPhoto) {
        this.detailPhoto = detailPhoto;
    }

    @SerializedName("photos")
    private List<Photo> detailPhoto;

    public List<Object> getmHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setmHtmlAttributions(List<Object> mHtmlAttributions) {
        this.mHtmlAttributions = mHtmlAttributions;
    }

    public RestaurantDetailsResult getResult() {
        return result;
    }

    public void setResult(RestaurantDetailsResult result) {
        this.result = result;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
