package com.example.go4lunch;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.UserRepository;
import com.example.go4lunch.views.WorkmatesViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int RC_SIGN_IN_EMAIL = 456 ;
    private  WorkmatesViewModel workmatesViewModel ;
    private UserRepository userRepository = UserRepository.getInstance();
    private GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setUpListeners();
        startSignInActivity();
        configureViewModel();
        //setUpListeners();

    }

    private void setUpListeners() {
        if (userRepository.isCurrentUserLogged()){
            startSignInActivity();
        }else{
            startMainActivity();
        }
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateLoginButton();
    }


    // 2 - Launch Sign-In Activity
    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        //.setLogo(R.drawable.ic_logo_auth)
                        .build(),
                RC_SIGN_IN);
    }

    private void updateLoginButton() {
    }


    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                launchMainActivity();
                this.createUserInFirestore();
            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(this, getString(R.string.error_authentication_canceled), Toast.LENGTH_SHORT).show();
                    //startSignInActivity();
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void createUserInFirestore() {
        if (this.getCurrentUser() != null) {
            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();
            User userToCreate = new User(uid,username,urlPicture);
            workmatesViewModel.getCurrentUserData().observe(this, user -> {
                if (user == null) {
                    workmatesViewModel.createUser(userToCreate);
                }
            });

        }
    }

    private void configureViewModel() {
         workmatesViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
    }

    private FirebaseUser getCurrentUser() {
        return workmatesViewModel.getCurrentUser();
    }


    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
        activity.finish();
    }


}