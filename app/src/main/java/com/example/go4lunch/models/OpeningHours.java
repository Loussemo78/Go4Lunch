package com.example.go4lunch.models;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {
    @SerializedName("open_now")
    private Boolean openNow;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }
}
