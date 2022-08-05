package com.example.go4lunch.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.go4lunch.RestaurantsDetailActivity;
import com.example.go4lunch.models.LikedRestaurant;
import com.example.go4lunch.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseHelper {
    private static FirebaseHelper sFirebaseHelper;
    private static final String TAG = "firebasehelper";
    public static final String RESTAURANT_ID = "restaurantId";
    public static final String RESTAURANT_NAME = "restaurantName";
    public static final String FIELD_USERNAME = "username";
    public static final String COLLECTION_NAME = "users";
    public static final String USER_ID = "uid";


    public static FirebaseHelper getInstance() {
        if (sFirebaseHelper == null) {
            sFirebaseHelper = new FirebaseHelper();
        }
        return sFirebaseHelper;
    }

    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<DocumentSnapshot> getCurrentWorkmate(String id) {
        return getWorkmatesCollection().document(id).get();
    }

    public  Task<Void> deleteWorkmate(String id) {
        return getWorkmatesCollection().document(id).delete();
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final CollectionReference usersRef = db.collection("users");
    public final CollectionReference likedRef = db.collection("likedRestaurants");
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Task<QuerySnapshot> getCurrentUser() {
        if (user != null) {
            user.getDisplayName();
            user.getPhotoUrl();
        }
        return usersRef.get();
    }



    public CollectionReference getAllUsers(){
        return usersRef;
    }



    public void setUsersFromFirestore(String restaurantId, String restaurantName) {
        String uid = user.getUid();
        String urlPicture = user.getPhotoUrl().toString();
        String name = user.getDisplayName();
        User userinfo = new User(uid, name, urlPicture);
        userinfo.setRestaurantName(restaurantName);
        userinfo.setRestaurantId(restaurantId);
        userinfo.setIsEatingAt(true);

        usersRef.document(user.getUid()).set(userinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("", "Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("", "error");
            }
        });
    }

    public Task<QuerySnapshot> getUsersAtFirestore() {
        return usersRef.get();
    }

    public void setLikedRestaurantById(String restaurantId, String restaurantName) {
        String uid = user.getUid();
        String name = user.getDisplayName();
        LikedRestaurant data = new LikedRestaurant(uid, name);
        data.setId(restaurantId);
        data.setName(restaurantName);
        data.setUid(uid);
        likedRef.document(uid).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("", "Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("", "Error");
            }
        });

    }

    public void updateUserRestaurant(User mUser) {
        usersRef.document(user.getUid()).update(RESTAURANT_ID, mUser.getRestaurantId());
        usersRef.document(user.getUid()).update(RESTAURANT_NAME, mUser.getRestaurantName());
    }


    public Task<QuerySnapshot> getUsersFromFirestore(String eatingPlaceId) {
        return usersRef.whereEqualTo(RestaurantsDetailActivity.RESTAURANT_ID, eatingPlaceId).get();
    }

    public Task<Void> deleteLikedRestaurant() {
        return likedRef.document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public Task<QuerySnapshot> getUsersLikedFromFirestore(String restaurantId) {
        return likedRef.whereEqualTo(RestaurantsDetailActivity.RESTAURANT_ID, restaurantId).get();
    }


    public FirebaseUser getCurrentWorkmates() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }



}
