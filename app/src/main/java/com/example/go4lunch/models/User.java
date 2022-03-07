package com.example.go4lunch.models;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.go4lunch.views.UserStateItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {


    private String uid;
    private String username;
    private String urlPicture ;
    private String restaurantId;
    private String restaurantName;

    public User(String uid, String restaurantName) {
        this.uid = uid;
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }


    private List<User> users ;
    private User user;

    public User(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }


    public User(String uid, String username, String urlPicture, String restaurantId) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restaurantId = restaurantId;
    }

    public User(String uid) {
        this.uid = uid;
    }




    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }



    public User(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }

    public User(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture( String urlPicture) {
        this.urlPicture = urlPicture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return uid.equals(that.uid) && username.equals(that.username) && urlPicture.equals(that.urlPicture);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(uid, username, urlPicture);
    }


    /*public static String  getRestaurant(){
        return getRestaurant();
    }*/
}
