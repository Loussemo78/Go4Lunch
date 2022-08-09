package com.example.go4lunch.utils;

import static android.content.Context.LOCATION_SERVICE;
import static java.lang.Math.acos;

import android.content.Context;
import android.util.Log;

import com.example.go4lunch.manager.LocationManager;
import com.example.go4lunch.models.Location;
import com.example.go4lunch.models.RestaurantsResult;
import com.google.android.gms.maps.model.LatLng;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import androidx.annotation.NonNull;

public class GeometryUtil {


      @NonNull
     private  static Location getDistances(Context context) {
        Location location = new Location();
        GPSTracker gps = new GPSTracker(context);
        if(gps.canGetLocation()){
            Log.e("testGpsTrue", "true");
            double latitude = gps.getLatitude(); // returns latitude
            double lng = gps.getLongitude(); // returns longitude
            location.setLng(lng);
            location.setLat(latitude);
        }
        return location;
    }

//    public static double calculateDistance(Context context, double toLong, double toLat) {
//        double fromLat = getDistances(context).getLat();
//        double fromLong = getDistances(context).getLng();
//        double d2r = Math.PI / 180;
//        double dLong = (toLong - fromLong) * d2r;
//        double dLat = (toLat - fromLat) * d2r;
//        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
//                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double d = 6367000 * c;
//        return Math.round(d);
//    }
    public String calculateDistance(RestaurantsResult result , Context context) {
        Double lat = result.getGeometry().getLocation().getLat();
        Double lng = result.getGeometry().getLocation().getLng();
        LatLng latLng1 = new LatLng(lat, lng);

        //Get current location
        LocationManager locationManager = new LocationManager(context);
        LatLng latLng2 = locationManager.getCurrentLatLng();


        //Calculate distance in meter
        double distanceDouble = greatCircleInMeters(latLng1, latLng2);
        String distance;
        //If distance is more than 900 m, convert to km
        if (distanceDouble > 900) {
            distanceDouble = distanceDouble / 1000;
            distance = String.valueOf(roundOneDecimal(distanceDouble) + "km");
        } else {
            distance = String.valueOf(Math.round(distanceDouble)) + "m";
        }
        return distance;

    }




public static String getString1000Less(double distance){
        double d = distance / 1000;
        double dR = Math.round(d*10.0/10.0);
        return dR + " km";
    }

   public static  double greatCircleInMeters(LatLng latLng1, LatLng latLng2) {
        double value;
        value = greatCircleInKilometers(latLng1.latitude, latLng1.longitude, latLng2.latitude,
                latLng2.longitude) * 1000;
        return value;

    }

    public static double roundOneDecimal(double value) {
        int scale = (int) Math.pow(10, 1);

        //Round result to one decimal
        return (double) Math.round(value * scale) / scale;
    }

   public static double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {
        double PI_RAD = Math.PI / 180.0;
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;

        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }



}
