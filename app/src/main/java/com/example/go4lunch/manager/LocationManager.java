package com.example.go4lunch.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.go4lunch.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class LocationManager {

    private Context context;


    public LocationManager(Context context) {
        this.context = context;
    }


    public LatLng getCurrentLatLng() {
        //Get current location
        SharedPreferences pref = context.getSharedPreferences("Go4Lunch", Context.MODE_PRIVATE);
        double currentLat = Double.parseDouble(pref.getString("CurrentLatitude", "48.7437922"));
        double currentLng = Double.parseDouble(pref.getString("CurrentLongitude", "2.4320896"));

        return new LatLng(currentLat, currentLng);
    }


}
