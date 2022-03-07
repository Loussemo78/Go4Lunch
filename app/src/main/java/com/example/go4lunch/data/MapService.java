package com.example.go4lunch.data;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.models.RestaurantsInfo;
import com.example.go4lunch.models.PlacesAutocomplete;
import com.example.go4lunch.models.RestaurantDetail;
import com.example.go4lunch.models.RestaurantsResult;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MapService {
     static final String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;

    //GoogleMap API Request
    @GET("maps/api/place/nearbysearch/json?key="+GOOGLE_MAP_API_KEY)
    Call<RestaurantsInfo>getRestaurants(@Query("location") String location , @Query("radius") int radius, @Query("type") String type);

    //PlaceDetails API Request
    @GET("maps/api/place/details/json?key="+GOOGLE_MAP_API_KEY)
    Call<RestaurantDetail> getDetails(@Query("place_id") String placeId);

    //Autocomplete API Request
    @GET("maps/api/place/autocomplete/json?strictbounds=true&types=establishment&key="+GOOGLE_MAP_API_KEY)
    Call<PlacesAutocomplete> getPlaceAutoComplete(@Query("input") String query, @Query("location") String location, @Query("radius") int radius);
}
