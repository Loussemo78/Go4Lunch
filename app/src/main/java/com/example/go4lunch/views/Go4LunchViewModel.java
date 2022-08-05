package com.example.go4lunch.views;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.go4lunch.models.PlacesAutocomplete;
import com.example.go4lunch.models.Prediction;
import com.example.go4lunch.models.RestaurantsInfo;
import com.example.go4lunch.models.RestaurantsResult;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.Go4LunchRepository;
import com.example.go4lunch.repositories.LocationRepository;
import com.example.go4lunch.repositories.WorkmatesRepository;

import java.util.ArrayList;
import java.util.List;

public class Go4LunchViewModel extends AndroidViewModel {
    private static Go4LunchViewModel go4LunchViewModelFactory;
    private final Go4LunchRepository go4LunchRepository;
    private final WorkmatesRepository workmatesRepository;
    private final LocationRepository locationRepository;
    private MutableLiveData<Pair<String, String>> searchPingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<PlacesAutocomplete>> listPredictions = new MutableLiveData<>();
    private final MutableLiveData<Location>currentLocation = new MutableLiveData<>();


    private LiveData<List<Prediction>> listPlaceAutoComplete = Transformations.switchMap(
            searchPingMutableLiveData, new Function<Pair<String, String>, LiveData<List<Prediction>>>() {
                @Override
                public LiveData<List<Prediction>> apply(Pair<String, String> input) {
                    return Transformations.map(go4LunchRepository.getPlacesAutocomplete(input.first, input.second, 300), new Function<PlacesAutocomplete, List<Prediction>>() {
                        @Override
                        public List<Prediction> apply(PlacesAutocomplete input) {

                            return input.getPredictions();
                        }
                    });
                }
            }
    );
    private MediatorLiveData<List<RestaurantsResult>> viewStateMldMediatorLiveData = new MediatorLiveData<>();


    // private MutableLiveData<Prediction> predictionMutableLiveData = new MutableLiveData<>();

    public Go4LunchViewModel(@NonNull Application application) {
        super(application);
        go4LunchRepository = new Go4LunchRepository();
        workmatesRepository = new WorkmatesRepository();
        locationRepository = new LocationRepository(application.getApplicationContext());
        LiveData<RestaurantsInfo> restaurantLiveData = go4LunchRepository.getRestaurants();
        LiveData<List<User>> userLiveData = workmatesRepository.getAllWorkmates();
        viewStateMldMediatorLiveData.addSource(restaurantLiveData, restaurantsInfo -> {
            combine(restaurantsInfo, userLiveData.getValue());
        });
        viewStateMldMediatorLiveData.addSource(userLiveData, userList -> {
            combine(restaurantLiveData.getValue(), userList);
        });
        locationRepository.observeForever(location -> {
            //load restaurants
            loadRestaurants("" + location.getLatitude() + "," + location.getLongitude(), 3000, "restaurant");
            currentLocation.postValue(location);
        });
    }

    private void combine(RestaurantsInfo value, List<User> workmates) {
        if (value == null || workmates == null) {
            return;
        }
        List<RestaurantsResult> listRestaurants = value.getResults();
        for (int i = 0, listRestaurantsSize = listRestaurants.size(); i < listRestaurantsSize; i++) {
            RestaurantsResult result = listRestaurants.get(i);
            int userInterested = 0;
            for (User u : workmates) {
                if (ObjectsCompat.equals(u.getRestaurantId(), result.getPlaceId())) {
                    userInterested++;
                }
            }
            listRestaurants.get(i).setUserInterested(userInterested);
        }
        viewStateMldMediatorLiveData.setValue(listRestaurants);

    }


    //RESTAURANTS LISTS
    public MutableLiveData<RestaurantsInfo> getRestaurants() {
        return go4LunchRepository.getRestaurants();
    }

    public LiveData<List<RestaurantsResult>> getRestaurantsViewState() {
        return viewStateMldMediatorLiveData;
    }
    public LiveData<Location> getLiveDataLocation(){
        return currentLocation;
    }


    public void loadRestaurants(String location, int radius, String type) {
        go4LunchRepository.loadRestaurants(location, radius, type);
    }


    //application ?
    public static  Go4LunchViewModel getInstance(Application application){
       if(go4LunchViewModelFactory == null){
           synchronized(Go4LunchViewModel.class){
               go4LunchViewModelFactory = new Go4LunchViewModel((Application) application.getApplicationContext());
           }
       }
       return go4LunchViewModelFactory;
    }

    public void loadAutocomplete(String query, String location, int radius) {
        go4LunchRepository.getPlacesAutocomplete(query, location, radius);
    }

    public void startService(){
        locationRepository.startService();
    }

    //AUTOCOMPLETE
   /* public MutableLiveData<PlacesAutocompleteModel> getAutocomplete() {
        return go4LunchRepository.getCurrentSearchQuery();
    }

    /*public void loadAutocomplete(Place place) {
          listPlaceAutoComplete.setValue(place);
    }*/

    public MutableLiveData<List<User>> getUsersAtRestaurant(String placeId) {
        return workmatesRepository.getUserFirestore(placeId);
    }

    private LiveData<List<User>> mapDataUserToViewState(LiveData<List<User>> workmates) {
        return workmates;
    }

    public LiveData<List<User>> getAllUsers() {
        return mapDataUserToViewState(workmatesRepository.getAllWorkmates());
    }

    public LiveData<List<Prediction>> getAutocompleteResults() {
        return listPlaceAutoComplete;
    }

    public MutableLiveData<PlacesAutocomplete> getPredictions(String query, Location location, int radius) {
        return go4LunchRepository.getPlacesAutocomplete(query, location.getLatitude() + "," + location.getLongitude(), radius);
    }


    public void startRequest(String newQuery, Location location) {
        searchPingMutableLiveData.setValue(new Pair<>(newQuery, "" + location.getLatitude() + "," + location.getLongitude()));
    }

    public void onAutocompleteResultsClick() {

    }
    public LiveData<List<UserStateItem>>getUsersChoiceRestaurant(){
        return Transformations.map(workmatesRepository.getAllWorkmates(), users -> {
            List<UserStateItem> userStateItems = new ArrayList<>();
            for(User u : users) {
                if (u.getRestaurantName() != null) {
                    userStateItems.add(new UserStateItem(u.getUid(),u.getUsername(),u.getUrlPicture(),u.getIsEatingAt()));
                }
            }
            return userStateItems;
        });
    }
    /*public void getPredictions(String input, String location ) {
        go4LunchRepository.getPlacesAutocomplete()
       // listPlaceAutoComplete.setValue(place);
    }*/

}
