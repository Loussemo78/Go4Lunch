package com.example.go4lunch.views;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.models.LikedRestaurant;
import com.example.go4lunch.models.RestaurantDetail;
import com.example.go4lunch.models.RestaurantsResult;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.Go4LunchRepository;
import com.example.go4lunch.repositories.WorkmatesRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RestaurantDetailViewModel extends AndroidViewModel {

    private final Go4LunchRepository go4LunchRepository;

    private final WorkmatesRepository workmatesRepository;

    private MutableLiveData<RestaurantsResult> restaurantsResult = new MutableLiveData<>();

    private MutableLiveData<RestaurantDetail> listDetailRestaurants;


    public RestaurantDetailViewModel(@NonNull @NotNull Application application) {
        super(application);
        go4LunchRepository = new Go4LunchRepository();
        workmatesRepository = new WorkmatesRepository();
    }

    public MutableLiveData<RestaurantDetail> getRestaurants(String placeId) {
        listDetailRestaurants = loadDetail(placeId);
        return listDetailRestaurants;
    }

    private MutableLiveData<RestaurantDetail> loadDetail(String placeId) {
        return go4LunchRepository.getDetail(placeId);
    }

    public MutableLiveData<RestaurantsResult> getDetailRestaurant(String placeId) {
        return restaurantsResult;
    }

    //public void getLikedRestaurantById(String rId){ workmatesRepository.setLikedRestaurantById(rId);}


    public void setRestaurantNameAndIdToFirestore(String detailId, String detailName) {
        workmatesRepository.setRestaurantNameAndIdToFirestore(detailId, detailName);
        //user current add id and name resto and then call repo to send in firebase
        // or call Detail.getResults
    }

    public void updateUserRestaurant(String restaurantId, String restaurantName) {
        User mUser = new User();
        mUser.setRestaurantId(restaurantId);
        mUser.setRestaurantName(restaurantName);
        workmatesRepository.updateUserRestaurant(mUser);
    }

    public void setLikedRestaurantById(String restaurantId, String restaurantName) {
        workmatesRepository.setLikedRestaurantById(restaurantId, restaurantName);
    }

    public void deleteLikedRestaurant() {
        workmatesRepository.deleteLikedRestaurant();
    }


    /*public LiveData<User> getUsersAtRestaurant(String extra) {
        return workmatesRepository.getUserAtFirestore(extra);
    }*/
    public LiveData<User> getLikedRestaurantById(String restaurantId) {
        return workmatesRepository.getLikedRestaurantById(restaurantId);
    }

    public MutableLiveData<List<User>> getUsersAtRestaurant(String placeId) {
        return workmatesRepository.getUserFirestore(placeId);
    }

    public MutableLiveData<User> getUsersToRestaurant() {
        return workmatesRepository.getUserAtFirestore();
    }

    public MutableLiveData<LikedRestaurant> getUsersLikedRestaurant() {
        return workmatesRepository.getUserLikedFirestore();
    }

}
