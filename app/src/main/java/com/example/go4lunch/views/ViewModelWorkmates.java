package com.example.go4lunch.views;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.models.LikedRestaurant;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.WorkmatesRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewModelWorkmates extends ViewModel {
    private final WorkmatesRepository repository;
    private MutableLiveData<List<User>> listUsers ;


    public ViewModelWorkmates() {
        this.repository = WorkmatesRepository.getInstance();
    }

    private LiveData<List<UserStateItem>> mapDataToViewState(LiveData<List<User>> workmates) {
        return Transformations.map(workmates, workmate -> {
            List<UserStateItem> userViewStateItems = new ArrayList<>();
            for (User u : workmate) {
                userViewStateItems.add(
                        new UserStateItem(u)
                );
            }
            return userViewStateItems;
        });
    }


    public LiveData<List<UserStateItem>> getAllWorkmates() {
        return mapDataToViewState(repository.getAllWorkmates());
    }

    public LiveData<List<UserStateItem>> getCurrentWorkmates(){
        return mapDataToViewState(repository.getWorkmates());
    }
    public LiveData<User> getCurrentUserData(){
        return repository.getUserFromFirestore();
    }

    public void loadUsers(){
        listUsers = repository.getAllWorkmates();
    }

    //public void setLikedRestaurantById(String restaurantId , String restaurantName){ repository.setLikedRestaurantById(restaurantId, restaurantName);}

    /*public void deleteLikedRestaurant(String restaurantId) {
        repository.deleteLikedRestaurant(restaurantId);
    }*/

    public void insertLikedRestaurant(LikedRestaurant likedRestaurant) {
        repository.insertLikedRestaurant(likedRestaurant);
    }



    //public void updateEatingPlace(String uid, String eatingPlaceName, String eatingPlaceId ) { repository.updateEatingPlace(uid, eatingPlaceName, eatingPlaceId);}

}
