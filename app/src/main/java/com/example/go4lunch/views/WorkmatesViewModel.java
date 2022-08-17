package com.example.go4lunch.views;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class WorkmatesViewModel extends AndroidViewModel {

    private final UserRepository userRepository;


    public WorkmatesViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    /** GET **/

    public FirebaseUser getCurrentUser(){ return userRepository.getCurrentUser();}

    public LiveData<User> getCurrentUserData(){
        return userRepository.getUserFromFirestore();
    }


    /** INSERT **/
    public void createUser(User user) { userRepository.createUser(user);}



}
