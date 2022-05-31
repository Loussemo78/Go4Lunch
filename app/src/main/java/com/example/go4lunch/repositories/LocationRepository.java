package com.example.go4lunch.repositories;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class LocationRepository extends LiveData<Location>{

    private static LocationRepository locationFactory = null;
    private FusedLocationProviderClient fusedLocationClient;
    private final Context mCurrentContext;
    private final LocationRequest locationRequest;


    private LocationCallback locationUpdateCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            setValue(locationResult.getLastLocation());
        }
    };

    public LocationRepository(Context context) {
        this.mCurrentContext = context.getApplicationContext();
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.locationRequest = LocationRequest.create()
                .setInterval(60000)
                .setFastestInterval(60000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(60000);

    }

    //SINGLETON
    public static LocationRepository getInstance(Context context){
        if (locationFactory == null){
            synchronized(LocationRepository.class){
                if (locationFactory == null){
                    locationFactory = new LocationRepository(context.getApplicationContext());
                }
            }
        }
        return locationFactory;
    }

    public void startService() {
        onActive();
    }

    @Override
    protected void onActive() {
        super.onActive();
        if (ActivityCompat.checkSelfPermission(mCurrentContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mCurrentContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            findLocation();
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        fusedLocationClient.removeLocationUpdates(locationUpdateCallback);
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_FINE_LOCATION"})
    private void findLocation(){
        Looper looper = Looper.myLooper();
        fusedLocationClient.requestLocationUpdates(locationRequest,locationUpdateCallback,looper);
    }

}