package com.example.go4lunch.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.models.LikedRestaurant;
import com.example.go4lunch.models.User;
import com.example.go4lunch.utils.FirebaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesRepository {
    private static final String COLLECTION_NAME = "users";
    public static final String COLLECTION_LIKE_RESTAURANT = "likedRestaurants";

    private final MutableLiveData<List<User>> listOfWorkmates = new MutableLiveData<>();
    //private final MutableLiveData<User> users = new MutableLiveData<>();

    private static WorkmatesRepository sWorkmatesRepository;
    private final FirebaseHelper mFirebaseHelper;

    public static WorkmatesRepository getInstance() {
        if (sWorkmatesRepository == null) {
            sWorkmatesRepository = new WorkmatesRepository();
        }
        return sWorkmatesRepository;
    }

    public WorkmatesRepository() {
        mFirebaseHelper = FirebaseHelper.getInstance();
        // Uncomment this method to populate your firebase database, it will upload some data
        // Comment it again after the first launch
        //initData();
    }

    public MutableLiveData<List<User>> getAllWorkmates() {
        mFirebaseHelper.getAllUsers().addSnapshotListener((value, error) ->  {
            if (value != null) {
                listOfWorkmates.setValue(value.toObjects(User.class));
            } else {
                listOfWorkmates.setValue(null);
            }
        });

        return listOfWorkmates;
    }

    public MutableLiveData<List<User>> getWorkmates() {
        mFirebaseHelper.getCurrentUser().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<User> workmates = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    workmates.add(document.toObject(User.class));
                }
                listOfWorkmates.postValue(workmates);
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //handle error
                listOfWorkmates.postValue(null);
            }
        });
        return listOfWorkmates;
    }



    public MutableLiveData<User> getUserFromFirestore() {
        String uId = this.getCurrentUserUID();
        MutableLiveData<User> user = new MutableLiveData<>();
        this.getUsersCollection().document(uId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user.setValue(documentSnapshot.toObject(User.class));
            } else {
                user.setValue(null);
            }
        });
        return user;
    }
//methode pour afficher le user courant dans la page d'accueil

    public MutableLiveData<LikedRestaurant> getUserLikedFirestore() {
        //Query collection Firebase helper
        String uId = this.getCurrentUserUID();
        MutableLiveData<LikedRestaurant> likedRestaurantMutableLiveData = new MutableLiveData<>();
        this.getLikedCollection().document(uId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                likedRestaurantMutableLiveData.setValue(documentSnapshot.toObject(LikedRestaurant.class));
            } else {
                likedRestaurantMutableLiveData.setValue(null);
            }
        });
        return likedRestaurantMutableLiveData;
    }

    public MutableLiveData<User> getLikedRestaurantById(String restaurantId){
        String uId = getCurrentUserUID();
        MutableLiveData<User> user = new MutableLiveData<>();
        this.getLikedCollection().document(uId).collection(COLLECTION_LIKE_RESTAURANT).document(restaurantId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                user.setValue(documentSnapshot.toObject(User.class));
            }else {
                user.setValue(null);
            }
        }).addOnFailureListener(e -> user.setValue(null));
        return user;
    }

    public MutableLiveData<List<User>> getUserFirestore(String placeId){
        mFirebaseHelper.getUsersFromFirestore(placeId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<User> workmates = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    workmates.add(document.toObject(User.class));
                }
                listOfWorkmates.postValue(workmates);
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //handle error
                listOfWorkmates.postValue(null);
            }
        });//ajouter postvalue user
        return listOfWorkmates;
    }

    public void setRestaurantNameAndIdToFirestore(String detailId , String detailName) {
          mFirebaseHelper.setUsersFromFirestore(detailId , detailName);
    }

    public void updateUserRestaurant(User mUser) {
        mFirebaseHelper.updateUserRestaurant(mUser);
        // String uId = this.getCurrentUserUID();
    }

    
    public void setLikedRestaurantById(String placeId , String restaurantName) {
        mFirebaseHelper.setLikedRestaurantById(placeId , restaurantName);
    }


    private CollectionReference getUsersCollection() {
        //return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        return mFirebaseHelper.getDb().collection(COLLECTION_NAME);
    }
    private CollectionReference getLikedCollection() {
        //return FirebaseFirestore.getInstance().collection(COLLECTION_LIKE_RESTAURANT);
        return mFirebaseHelper.getDb().collection(COLLECTION_LIKE_RESTAURANT);
    }

    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }



    private void initData() {
        FirebaseHelper.getInstance().usersRef.add(new User("hrRJcZSX0GZuHQ4HGZNJ","Virgile","https://avatars.githubusercontent.com/u/32424601?s=400&u=22568998290937ae2a2a682654e5af68cbfe27fd&v=4"));
        FirebaseHelper.getInstance().usersRef.add(new User("hrRJdZSX0GpuHQ4HGZNJut","Franck","https://brandandcelebrities.com/wp-content/uploads/2017/04/Franck_Dubosc.jpg"));
        FirebaseHelper.getInstance().usersRef.add(new User("cqRJeZSX0GouHQ4pGaNJutiK","The ROCK","https://www.avcesar.com/source/actualites/00/00/74/25/la-vraie-vie-de-dwayne-the-rock-johnson-avant-le-cinema_prev_01014840.jpg"));
    }

    public Task<Void> insertLikedRestaurant(LikedRestaurant likedRestaurant) {
        //String uId = this.getCurrentUserUID();
        return this.getLikedCollection().document().collection(COLLECTION_LIKE_RESTAURANT).document(likedRestaurant.getId()).set(likedRestaurant);
    }

    public void deleteLikedRestaurant() {
        mFirebaseHelper.deleteLikedRestaurant();
    }





}
