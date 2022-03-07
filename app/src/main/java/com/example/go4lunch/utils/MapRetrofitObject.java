package com.example.go4lunch.utils;

import com.example.go4lunch.data.MapService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapRetrofitObject {
    public static Retrofit retrofit = new Retrofit.Builder()
            //define root URL
            .baseUrl("https://maps.googleapis.com/")
            //serialize Gson
            .addConverterFactory(GsonConverterFactory.create())
            //For RxJava
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public static MapService getInterface() {
        return retrofit.create(MapService.class);
    }
}
