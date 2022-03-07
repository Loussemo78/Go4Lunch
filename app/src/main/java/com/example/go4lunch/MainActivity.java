package com.example.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.models.Prediction;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.views.AutocompleteAdapter;
import com.example.go4lunch.views.Go4LunchViewModel;
import com.example.go4lunch.views.ProfilViewModel;
import com.example.go4lunch.views.WorkmatesFragment;
import com.example.go4lunch.views.ListFragment;
import com.example.go4lunch.views.MapFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //FOR DESIGN
    private MapFragment mapFragment = new MapFragment();
    private ListFragment listFragment = new ListFragment();
    private Location mLocation = new Location("");
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private UserRepository userRepository = UserRepository.getInstance();
    private NavigationView navigationView;
    private BottomNavigationView bottomView;
    private Go4LunchViewModel go4LunchViewModel;
    private ProfilViewModel profilViewModel;
    private List<Prediction> predictions = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private AutocompleteAdapter adapter;
    private RecyclerView recyclerView;
    private Executor application;
    private String provider;
    private final String GOOGLE_MAP_API_KEY = BuildConfig.GOOGLE_MAP_API_KEY;
    private Menu menu;
    private SearchView.SearchAutoComplete mSearchAutocomplete;
    private String mPosition;


    static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private ActivityMainBinding binding;

    private Fragment fragment1 = new MapFragment();
    private FragmentManager fm = getSupportFragmentManager();
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private Object Singleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Places.initialize(getApplicationContext(), GOOGLE_MAP_API_KEY);
        PlacesClient placesClient = Places.createClient(this);


        //fm.beginTransaction().add(R.id.activity_main_frame_layout, fragment1 , "1").commit();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        configureToolBar();

        configureActionBar();

        configureDrawerLayout();

        initViewModel();


        configureNavigationView();

        //  configureRecyclerView();


        checkLocationPermission();
    }

    private void configureRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new AutocompleteAdapter(new AutocompleteAdapter.OnAutocompleteResultClickCallback() {
            @Override
            public void onClick(@NonNull Prediction prediction) {
                go4LunchViewModel.onAutocompleteResultsClick();

            }

        });
        // recyclerView = binding.searchList;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void configureActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("Search");
    }


    private void initViewModel() {
        go4LunchViewModel = new ViewModelProvider(this).get(Go4LunchViewModel.class);
        profilViewModel = new ViewModelProvider(this).get(ProfilViewModel.class);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }

        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @NotNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull @NotNull OnTokenCanceledListener onTokenCanceledListener) {
                Log.d("CurrentLocation", "CancelRequest");
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mLocation = location;
                    go4LunchViewModel.loadRestaurants("" + location.getLatitude() + "," + location.getLongitude(), 300, "restaurant");
                    configureBottomView();
                }
            }
        });

        /*go4LunchViewModel.getAutocompleteResults().observe(this, result -> {
            predictions.addAll(result);
            final ArrayAdapter<Prediction> adapter = new ArrayAdapter<Prediction>(this, android.R.layout.simple_spinner_item, predictions);
            mSearchAutocomplete.setAdapter(adapter);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        });
        /*go4LunchViewModel.getAutocompleteResults().observe(this, new Observer<List<Prediction>>() {

            @Override
            public void onChanged(List<Prediction> strings) {
                final ArrayAdapter<Prediction> adapter = new ArrayAdapter<Prediction>(this, android.R.layout.simple_spinner_item, );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //adapter.submitList(strings);
            }
        });*/
    }

    /*public void startAutocompleteActivity() {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                Arrays.asList(Place.Field.ID, Place.Field.NAME))
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setLocationBias(RectangularBounds.newInstance(
                        new LatLng(48.8534, 2.3488),
                        new LatLng(48.866667, 2.333333)
                ))
                .setCountries(Arrays.asList("FR"))
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        go4LunchViewModel.getAutocomplete();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                //Log.i("TAG", "Place:" + place.getName() + "," + place.getId());
                go4LunchViewModel.loadAutocomplete(place);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    private void updateUIWithUserData() {
        if (userRepository.isCurrentUserLogged()) {
            FirebaseUser user = userRepository.getCurrentUser();

            if (user.getPhotoUrl() != null) {
                setProfilePicture(user.getPhotoUrl());
            }
            setTextUserData(user);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl) {
        View header = navigationView.getHeaderView(0);
        ImageView pic = (ImageView) header.findViewById(R.id.header_avatar_pic);
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(pic);
    }

    private void setTextUserData(FirebaseUser user) {
        String username = TextUtils.isEmpty(user.getDisplayName()) ? null : user.getDisplayName();
        TextView name = binding.activityMainNavView.getHeaderView(0).findViewById(R.id.header_avatar_name);
        name.setText(username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.BLUE);
        searchAutoComplete.setTextColor(Color.GREEN);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_bright);


        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchAutoComplete.setText(adapterView.getItemAtPosition(i).toString());
            }
        });
        final ArrayAdapter<Prediction> predictionAdapter = new ArrayAdapter<Prediction>(MainActivity.this, android.R.layout.simple_spinner_item);
        searchAutoComplete.setAdapter(predictionAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //startActivity
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //ajouter view model de l'appel web service
                if (newText.length() > 2) {

                    go4LunchViewModel.getPredictions(newText, mLocation, 300).observe(MainActivity.this, result -> {
                        //predictions.addAll(result.getPredictions());
                        predictionAdapter.clear();
                        predictionAdapter.addAll(result.getPredictions());
                        predictionAdapter.notifyDataSetChanged();
                        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    });
                    //   go4LunchViewModel.startRequest(newText,mLocation);
                } else {
                    predictionAdapter.clear();
                    predictionAdapter.notifyDataSetChanged();
                }
                // Log.d(TAG, "onQueryTextChange: " + newText);
               /* if (newText.length() > 2 ) {
                    // Retrieve coords
                   // mPosition = Singleton.get ;
                    //Log.d(TAG, "onQueryTextChange: input: " + newText + ", location: " + mPosition);
                } else if (newText.length() < 2) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
                    if (fragment instanceof ListFragment) {
                        ((ListFragment) Objects.requireNonNull(fragment)).updateList();
                    } else if (fragment instanceof MapFragment) {
                        ((MapFragment) fragment).updateMap();
                    }
                }*/
                return false;
            }
        });
        MenuItem shareMenuItem = menu.findItem(R.id.app_bar_menu_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareActionProvider.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_profile:
                getYourRestaurant();
                break;
            case R.id.activity_main_drawer_settings:
                break;
            case R.id.activity_main_drawer_logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnSuccessListener(aVoid -> LoginActivity.navigate(this));
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

   /* private void getAndShowSearchedRestaurants(String search){

        placesMgr.executeHttpRequestToFindSearchedRestaurants(search, new DisposableObserver<PlacesAutocomplete>(){

            @Override
            public void onNext(PlacesAutocomplete places) {

                String [] placesIds = new String[places.getPredictions().size()];

                for (int i = 0; i<places.getPredictions().size(); i++){
                    placesIds[i] = places.getPredictions().get(i).getPlaceId();

                    placesMgr.executeHttpRequestToGetRestaurantDetails(placesIds[i], new DisposableObserver<PlacesAutocomplete>() {
                        @Override
                        public void onNext(PlacesAPI placesAPI) {
                            showRestaurantOnMapWithMarker(placesAPI.getResult());
                        }

                        @Override
                        public void onError(Throwable e) {}

                        @Override
                        public void onComplete() {}
                    });
                }
                setRestaurantsList(placesIds);
            }*/


    // 1 - Configure Toolbar
    private void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.header_avatar_name);
        //TextView email = (TextView) header.findViewById(R.id.header_avatar_email);
        ImageView pic = (ImageView) header.findViewById(R.id.header_avatar_pic);
        profilViewModel.getUsersAtRestaurant().observe(this, profil -> {
            name.setText(profil.getUsername());
            Glide.with(pic.getContext())
                    .load(profil.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(pic);
        });

    }

    public void getYourRestaurant() {
        profilViewModel.getUsersAtRestaurant().observe(this, user -> {
                    if (user.getRestaurantId().equals(null)) {
                        Toast.makeText(this, getString(R.string.eating_place_none), Toast.LENGTH_SHORT).show();
                    } else {
                        RestaurantsDetailActivity.navigate(this, user.getRestaurantId());
                    }
                }
        );
    }

    private void configureBottomView() {
        final BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
                menuItem -> {
                    Fragment pickedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.action_map:
                            pickedFragment = new MapFragment();
                            break;
                        case R.id.action_list:
                            pickedFragment = new ListFragment();
                            break;
                        case R.id.action_people:
                            pickedFragment = new WorkmatesFragment();
                            break;
                    }

                    if (pickedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_frame_layout,
                                pickedFragment).commit();
                    }
                    return true;
                };

        this.bottomView = (BottomNavigationView) findViewById(R.id.activity_main_bottom_navigation);
        bottomView.setOnNavigationItemSelectedListener(bottomNavListener);

    }


    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission();
            }
        }
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION
            );
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION
            );
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);


                        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                        provider = locationManager.getBestProvider(criteria, true);
                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);

                        //locationManager.getCurrentLocation(provider,null, );
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }

    }
}