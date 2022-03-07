package com.example.go4lunch.models;

public class LikedRestaurant {



    private String uid, id, name, photo;

    public LikedRestaurant() {
    }

    public LikedRestaurant(String uid, String id, String name) {
        this.uid = uid;
        this.id = id;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LikedRestaurant(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public LikedRestaurant(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "LikedRestaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
