package com.example.go4lunch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo {
    @SerializedName("height")
    private Long height;
    @SerializedName("html_attributions")
    private List<Object> htmlAttributions;
    @SerializedName("photo_reference")
    private String photoReference;
    @SerializedName("width")
    private Long width;

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }
}
