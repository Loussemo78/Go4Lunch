package com.example.go4lunch.notifications;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.go4lunch.models.User;
import com.example.go4lunch.utils.FirebaseHelper;
import com.example.go4lunch.utils.UserHelper;
import com.example.go4lunch.utils.Utility;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class NotificationService extends FirebaseMessagingService {

    private User currentWorkmate = new User();
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private Utility utils = new Utility();
    private UserHelper userHelper = new UserHelper();



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            // Get message sent by Firebase
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Log.e("TAG",notification.getBody());
        }
    }

    private void getRestaurantData(){
        List<String> myLunchWorkmates = new ArrayList<>();
        FirebaseUser currentUser = firebaseHelper.user;

        if (currentUser != null){
            userHelper.getCurrentUsers(currentUser.getUid())
                    // If the user has chosen a restaurant this day, get data to notify

                    .addOnSuccessListener(documentSnapshot -> {
                        currentWorkmate = documentSnapshot.toObject(User.class);
                        if (utils.getTodayDate().equals(currentWorkmate.getRestaurantDate())){
                            String myLunchRestaurant = currentWorkmate.getRestaurantName();
                            String myRestaurantAddress = currentWorkmate.getRestaurantsResult().getVicinity();

                            // Get the names of the workmates who chose this restaurant
                            userHelper.getCollectionUsers()
                                    .whereEqualTo(FIELD_RESTAURANT_DATE, utils.getTodayDate())
                                    .whereEqualTo(FIELD_RESTAURANT_ID, currentWorkmate.getRestaurantId())
                                    .get()
                                    .addOnCompleteListener(task -> {
                                       if (task.isSuccessful() && task.getResult() != null){
                                           for (QueryDocumentSnapshot document : task.getResult()){
                                               if (currentUser.getDisplayName() != null && !currentUser.getDisplayName().equals(document.getString(FIELD_USERNAME))){
                                                   // Add them in a list
                                                   myLunchWorkmates.add(document.getString(FIELD_USERNAME));
                                                   Log.d(TAG, document.getId() + " => " + document.getData());

                                               }
                                           }
                                       }else{
                                           Log.d(TAG, "Error getting documents: ", task.getException());
                                       }
                                        sendVisualNotification(messageBody, myLunchRestaurant,
                                                myRestaurantAddress, myLunchWorkmates);
                                    });
                        }
                    });
        }

    }
}
