package com.example.go4lunch.utils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.WorkmatesRepository;
import com.example.go4lunch.views.UserStateItem;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserHelper {

    public static final String COLLECTION_NAME = "users";
    private static final String USER_NAME = "user_name";
    private static final String RESTAURANT_ID = "restaurantId";
    private static final String RESTAURANT_NAME = "restaurantName";
    private static final String RESTAURANT_VICINITY = "restaurantVicinity";
    private static final String RESTAURANT_LIKED_LIST = "restaurantLikedList";
    private static final String PICTURE_URL = "user_profile_picture";
    private  final WorkmatesRepository workmatesRepository;


    public UserHelper() {

        this.workmatesRepository = WorkmatesRepository.getInstance();
    }

    public UserHelper(WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;
    }

    public LiveData<List<UserStateItem>>getUsersChoiceRestaurant(){
        return Transformations.map(workmatesRepository.getAllWorkmates(), users -> {
            List<UserStateItem> userStateItems = new ArrayList<>();
            for(User u : users) {
                if (u.getRestaurantName() != null) {
                    userStateItems.add(new UserStateItem(u.getUid(),u.getUsername(),u.getUrlPicture(),u.getRestaurantsResult()));
                }
            }
            return userStateItems;
        });
    }

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public  CollectionReference getCollectionUsers() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }
    public static Task<Void> createUser(String uid,
                                        String username,
                                        String urlPicture,
                                        String restaurantId,
                                        String mEatingAt,
                                        ArrayList<String> restaurantLikedList) {
        User userToCreate = new User(uid, username, urlPicture, restaurantId,mEatingAt);
        return UserHelper.getUsersCollection()
                .document(uid) // Setting uID for Document
                .set(userToCreate); // Setting object for Document
    }

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Query getAllUsers() {
        return UserHelper.getUsersCollection().orderBy(RESTAURANT_NAME, Query.Direction.DESCENDING).orderBy(USER_NAME);
    }

    public static Task<QuerySnapshot> getRestaurant(String restaurantId) {
        return UserHelper.getUsersCollection().whereEqualTo(RESTAURANT_ID, restaurantId).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update(USER_NAME, username);
    }

    public static Task<Void> updateRestaurantId(String uid, String restaurantId) {
        return UserHelper.getUsersCollection().document(uid).update(RESTAURANT_ID, restaurantId);
    }

    public static Task<Void> updateRestaurantName(String uid, String restaurantName) {
        return UserHelper.getUsersCollection().document(uid).update(RESTAURANT_NAME, restaurantName);
    }

    public static Task<Void> updateRestaurantVicinity(String uid, String restaurantVicinity) {
        return UserHelper.getUsersCollection().document(uid).update(RESTAURANT_VICINITY, restaurantVicinity);
    }

    public static Task<Void> updateRestaurantLiked(String uid, List<String> like) {
        return UserHelper.getUsersCollection().document(uid).update(RESTAURANT_LIKED_LIST, like);
    }

    public static Task<Void> updateProfilePicture(String urlImage, String uid) {
        return UserHelper.getUsersCollection().document(uid).update(PICTURE_URL, urlImage);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

    public  Task<DocumentSnapshot> getCurrentUsers(String id) {
        return UserHelper.getUsersCollection().document(id).get();
    }


    public FirebaseUser getCurrentWorkmate() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
