package com.example.go4lunch.views;

import androidx.core.util.ObjectsCompat;

import java.util.Objects;

public class PlacesAutocompleteModel {

    private String restaurantId;
    private String restaurantName;
    private String restaurantVicinity;

    public PlacesAutocompleteModel(String restaurantId, String restaurantName, String restaurantVicinity) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantVicinity = restaurantVicinity;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantVicinity() {
        return restaurantVicinity;
    }

    public void setRestaurantVicinity(String restaurantVicinity) {
        this.restaurantVicinity = restaurantVicinity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacesAutocompleteModel that = (PlacesAutocompleteModel) o;
        return restaurantId.equals(that.restaurantId) && restaurantName.equals(that.restaurantName) && ObjectsCompat.equals(restaurantVicinity, that.restaurantVicinity);
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(restaurantId, restaurantName);
    }
}
