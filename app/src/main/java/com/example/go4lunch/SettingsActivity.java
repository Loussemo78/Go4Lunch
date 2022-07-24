package com.example.go4lunch;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.go4lunch.databinding.ActivitySettingsBinding;
import com.example.go4lunch.utils.FirebaseHelper;
import com.example.go4lunch.utils.LunchPreferences;
import com.example.go4lunch.utils.Utility;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private ActivitySettingsBinding binding;
    private ConstraintLayout constraintLayout;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private FirebaseUser currentUser;
    private Toolbar toolbar;
    private LunchPreferences prefs;
    private Utility utils = new Utility();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        constraintLayout = (ConstraintLayout) binding.settingsActivityLayout.getRootView();
        configureToolbar();
        prefs = new LunchPreferences(this);
        boolean boxIsChecked = prefs.getNotificationState();
        if (boxIsChecked) {
            binding.settingsActivityNotifications.setChecked(true);
        }
        subscribeNotifications();
        onDeleteClicked();
    }

    // Private methods to configure design

    private void configureToolbar() {
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Set and enable the Up button
        if (ab != null) {
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void subscribeNotifications() {
        binding.settingsActivityNotifications.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) {
                // Save the state of the checkbox and reuse it when SettingsActivity is open
                prefs.saveNotificationState(true);
                FirebaseMessaging.getInstance().subscribeToTopic("notification")
                        .addOnCompleteListener(task -> {
                            String msg = getString(R.string.notification_subscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.notification_unsubscribe_failed);
                            }
                            Log.d(TAG, msg);
                            utils.showSnackbar(constraintLayout, msg);
                        });
                     }else{
                // Save the state of the checkbox and reuse it when SettingsActivity is open
                prefs.saveNotificationState(false);
                FirebaseMessaging.getInstance().subscribeToTopic("notification")
                        .addOnCompleteListener(task -> {
                            String msg = getString(R.string.notification_unsubscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.notification_unsubscribe_failed);
                            }
                            Log.d(TAG, msg);
                            utils.showSnackbar(constraintLayout, msg);
                        });
                   }
             });
         }

         private void onDeleteClicked(){
             currentUser = firebaseHelper.user;
             binding.settingsActivityDeleteAccount.setOnClickListener(v ->{
                 // Create a dialog window to warn the user
                 AlertDialog.Builder warningDialog = new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialogWarning);
                 warningDialog.setTitle(getString(R.string.warning));
                 warningDialog.setMessage(getString(R.string.warning_delete));

                 // Configure cancel button
                 warningDialog.setNegativeButton(getString(R.string.cancel), (DialogInterface dialog, int which) ->
                         dialog.dismiss());

                 // Configure ok button to delete the user account and log out
                 warningDialog.setPositiveButton(getString(R.string.ok), (DialogInterface dialog, int which) -> {
                     // Unsubscribe to notification and save the state of the checkbox at false
                     firebaseHelper.deleteWorkmate(currentUser.getUid());
                     prefs.saveNotificationState(false);
                     // Unsubscribe to notification
                     FirebaseMessaging.getInstance().unsubscribeFromTopic("notification")
                             .addOnCompleteListener(task -> {
                                 String msg = getString(R.string.notification_unsubscribed);
                                 if (!task.isSuccessful()) {
                                     msg = getString(R.string.notification_unsubscribe_failed);
                                 }
                                 Log.d(TAG, msg);
                             });
                 });
                 warningDialog.show();
             });
         }
}
