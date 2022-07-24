package com.example.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LunchPreferences {

    // Use to store data for notifications

    private final SharedPreferences prefs;

    // Keys for the notifications
    private static final String NOTIFICATION_KEY = "NOTIFICATION_KEY";

    // Constructor
    public LunchPreferences(Context context){
        prefs = context.getSharedPreferences("Preferences", Context.MODE_MULTI_PROCESS);
    }

    // Save the state of notification
    public void saveNotificationState(Boolean isChecked) {
        prefs.edit().putBoolean(NOTIFICATION_KEY, isChecked).apply();
    }

    // Get the state of notification
    public Boolean getNotificationState() {
        return prefs.getBoolean(NOTIFICATION_KEY, false);
    }
}
