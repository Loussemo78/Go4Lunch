package com.example.go4lunch.views;

import com.example.go4lunch.models.RestaurantsResult;
import com.example.go4lunch.models.User;

import java.util.List;

public class RestaurantAddMessage {
    private RestaurantsResult result;
    private List<User> userList;

    public RestaurantsResult getResult() {
        return result;
    }

    public void setResult(RestaurantsResult result) {
        this.result = result;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
