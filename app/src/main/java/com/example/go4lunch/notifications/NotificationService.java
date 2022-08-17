package com.example.go4lunch.notifications;

import static com.example.go4lunch.utils.FirebaseHelper.FIELD_USERNAME;
import static com.example.go4lunch.utils.FirebaseHelper.RESTAURANT_ID;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.go4lunch.MainActivity;
import com.example.go4lunch.R;
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
    private static final String TAG = NotificationService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 321;






    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String messageBody = remoteMessage.getNotification().getBody();
            // Get data and show notification after received message
            getRestaurantData(messageBody);
            Log.i(TAG, messageBody);
        }
    }

    private void getRestaurantData(String messageBody){
        List<String> myLunchWorkmates = new ArrayList<>();
        FirebaseUser currentUser = firebaseHelper.user;

        if (currentUser != null){
            userHelper.getCurrentUsers(currentUser.getUid())
                    // If the user has chosen a restaurant this day, get data to notify

                    .addOnSuccessListener(documentSnapshot -> {
                        currentWorkmate = documentSnapshot.toObject(User.class);
                        if (utils.getNameRestaurant().equals(currentWorkmate.getRestaurantName())){
                            String myLunchRestaurant = currentWorkmate.getRestaurantName();

                            // Get the names of the workmates who chose this restaurant
                           userHelper.getCollectionUsers()
                                 //.whereEqualTo(FIELD_RESTAURANT_DATE, utils.getTodayDate())
                                  .whereEqualTo(RESTAURANT_ID, currentWorkmate.getRestaurantId())
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
                                                 myLunchWorkmates);
                                    });
                        }
                    });
        }

    }

    @SuppressLint("StringFormatInvalid")
    private void sendVisualNotification(String messageBody, String myLunchRestaurant, List<String> myLunchWorkmates){
        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String notificationText;
        if (myLunchWorkmates.isEmpty()) {
            notificationText = getString(R.string.notification_alone, myLunchRestaurant);
        } else {
            // Format the list of workmates
            String formatMyLunchWorkmates = TextUtils.join(", ", myLunchWorkmates);
            // Get notificationText with them
            notificationText = getString(R.string.notification, myLunchRestaurant, formatMyLunchWorkmates);
        }

        // Create a Style for the Notification
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

        // Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_restaurant_24)
                        .setContentTitle(messageBody)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(bigTextStyle.bigText(notificationText));

        // Add the Notification to the NotificationManager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = getString(R.string.message_from_firebase);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show the notification (notify builder)
        notificationManager.notify(TAG, NOTIFICATION_ID, notificationBuilder.build());

    }
}
