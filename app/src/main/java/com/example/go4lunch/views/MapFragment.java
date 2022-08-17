package com.example.go4lunch.views;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.ObjectsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.RestaurantsDetailActivity;
import com.example.go4lunch.models.RestaurantDetail;
import com.example.go4lunch.models.RestaurantDetailsResult;
import com.example.go4lunch.models.RestaurantsResult;
import com.example.go4lunch.models.User;
import com.example.go4lunch.utils.UserHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    public static final String DETAIL_RESTAURANT = "place_id";
    private static final String TAG = "Map Fragment";
    private Go4LunchViewModel go4LunchViewModel;
    //private WorkmatesViewModel workmatesViewModel;
    private boolean userGoing = true;
    private GoogleMap map;
    private MapView mapView;
    private final List<RestaurantsResult> listRestaurants = new ArrayList<>();


    public MapFragment() {
        // Required empty public constructor
    }

    public MapFragment(GoogleMap map) {
        this.map = map;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        //workmatesViewModel = new ViewModelProvider(requireActivity()).get(WorkmatesViewModel.class);
        go4LunchViewModel = new ViewModelProvider(requireActivity()).get(Go4LunchViewModel.class);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mediator live data , parce que  observe dans un observe
        go4LunchViewModel.getRestaurantsViewState().observe(getViewLifecycleOwner(), listRestaurant -> {
            listRestaurants.clear();
            listRestaurants.addAll(listRestaurant);
            googleMap.clear();
            for (RestaurantsResult result: listRestaurants) {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(result.getGeometry().getLocation().getLat(),
                        result.getGeometry()
                                .getLocation()
                                .getLng()))
                        .icon(setMarkerOnMap(result.getUserInterested()))
                        .title(result.getName()));
                marker.setTag(result);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                RestaurantsResult result = (RestaurantsResult) marker.getTag();
                Intent intent = new Intent(getContext(), RestaurantsDetailActivity.class);
                String place = result.getPlaceId();
                intent.putExtra(DETAIL_RESTAURANT, place);
                startActivity(intent);
                return false;
            }
        });
        //observer location repository au lieu d'avoir paris

        //location du repository
        go4LunchViewModel.getLiveDataLocation().observe(getViewLifecycleOwner(),location -> {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition resto = CameraPosition.builder().target(latLng)
                    .zoom(16)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(resto));
        });
        // [START_EXCLUDE silent]
    }



    private BitmapDescriptor setMarkerOnMap(int userInterested) {
        if (userInterested > 0) {
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        } else {
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

        }

    }

}






