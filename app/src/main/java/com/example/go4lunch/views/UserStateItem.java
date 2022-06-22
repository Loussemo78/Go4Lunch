package com.example.go4lunch.views;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.go4lunch.models.RestaurantsResult;
import com.example.go4lunch.models.User;

import java.util.List;
import java.util.Objects;

public class UserStateItem {



    @NonNull
    private String uid;
    @NonNull
    private String username;
    @NonNull
    private String urlPicture;

    private RestaurantsResult chosenRestaurant;
    private List<UserStateItem> getUsersStateItem;



    private static boolean favoris = true;

    public UserStateItem() {
    }

    public UserStateItem(@NonNull String uid, @NonNull String username, @NonNull String urlPicture, @NonNull Boolean isRestoFavoris) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@NonNull String urlPicture) {
        this.urlPicture = urlPicture;
    }


    @NonNull
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(@NonNull List<User> users) {
        this.users = users;
    }

    @NonNull
    private List<User> users ;

    public UserStateItem(@NonNull String uid, @NonNull String username, @NonNull String urlPicture , RestaurantsResult chosenRestaurant) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.chosenRestaurant = chosenRestaurant;
    }

    public UserStateItem(User user){
        this.uid = user.getUid();
        this.username = user.getUsername();
        this.urlPicture = user.getUrlPicture();
    }

    public RestaurantsResult getChosenRestaurant() {
        return chosenRestaurant;
    }

    public void setChosenRestaurant(RestaurantsResult chosenRestaurant) {
        this.chosenRestaurant = chosenRestaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStateItem that = (UserStateItem) o;
        return uid.equals(that.uid) && username.equals(that.username) && urlPicture.equals(that.urlPicture);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(uid, username, urlPicture);
    }

    public static boolean isFavoris() {
        return favoris;
    }

    public List<UserStateItem> getGetUsersStateItem() {
        return getUsersStateItem;
    }

    public void setGetUsersStateItem(List<UserStateItem> getUsersStateItem) {
        this.getUsersStateItem = getUsersStateItem;
    }
}
