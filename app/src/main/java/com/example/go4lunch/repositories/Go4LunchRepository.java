package com.example.go4lunch.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.MapService;
import com.example.go4lunch.models.RestaurantsInfo;
import com.example.go4lunch.models.PlacesAutocomplete;
import com.example.go4lunch.models.RestaurantDetail;
import com.example.go4lunch.utils.MapRetrofitObject;
import com.example.go4lunch.views.PlacesAutocompleteModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Go4LunchRepository {

    private final MapService mapService ;
    private final MutableLiveData<RestaurantsInfo>listOfPlaces = new MutableLiveData<>();
    private final MutableLiveData<RestaurantDetail>listOfDetail = new MutableLiveData<>();
    private final MutableLiveData<PlacesAutocomplete>listAutocomplete = new MutableLiveData<>();

    private final MutableLiveData<PlacesAutocompleteModel> currentSearchQueryMutableLiveData = new MutableLiveData<>();


    public Go4LunchRepository() {
        mapService = MapRetrofitObject.getInterface();
    }


    public MutableLiveData<RestaurantsInfo> loadRestaurants(String location, int radius, String type) {

        Call<RestaurantsInfo> listRestaurants = mapService.getRestaurants( location ,radius ,type);
        listRestaurants.enqueue(new Callback<RestaurantsInfo>() {
            @Override
            public void onResponse(@NotNull Call<RestaurantsInfo> call, @NotNull Response<RestaurantsInfo> response) {
                listOfPlaces.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<RestaurantsInfo> call, @NotNull Throwable t) {
                listOfPlaces.postValue(null);
            }
        });
       return listOfPlaces;
    }

    public MutableLiveData<RestaurantsInfo> getRestaurants(){
        return listOfPlaces;
    }

    public MutableLiveData<RestaurantDetail> getDetail(String placeId) {
        Call<RestaurantDetail>detailList = mapService.getDetails(placeId);
        detailList.enqueue(new Callback<RestaurantDetail>() {
            @Override
            public void onResponse(@NotNull Call<RestaurantDetail> call, @NotNull Response<RestaurantDetail> response) {
                listOfDetail.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<RestaurantDetail> call, @NotNull Throwable t) {
               listOfDetail.postValue(null);
            }
        });
        return listOfDetail;
    }

    public MutableLiveData<PlacesAutocomplete> getPlacesAutocomplete(String query, String location, int radius) {
        Call<PlacesAutocomplete> placesAutocompleteCall = mapService.getPlaceAutoComplete(query,location,radius);
        placesAutocompleteCall.enqueue(new Callback<PlacesAutocomplete>() {
            @Override
            public void onResponse(@NotNull Call<PlacesAutocomplete> call, @NotNull Response<PlacesAutocomplete> response) {
               listAutocomplete.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<PlacesAutocomplete> call, @NotNull Throwable t) {
              listAutocomplete.setValue(null);
            }
        });
        return listAutocomplete;
    }
    public MutableLiveData<PlacesAutocomplete> getListAutocomplete() {
        return listAutocomplete;
    }

        @NonNull
    public MutableLiveData<PlacesAutocompleteModel> getCurrentSearchQuery() {
        return currentSearchQueryMutableLiveData;
    }

    public void setCurrentSearch(@Nullable PlacesAutocompleteModel autocompleteRestaurants) {
        currentSearchQueryMutableLiveData.setValue(autocompleteRestaurants);
    }
}
