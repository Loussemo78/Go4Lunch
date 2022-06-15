package com.example.go4lunch.views;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.WorkmatesRepository;


public class ProfilViewModel extends AndroidViewModel {

    private final WorkmatesRepository workmatesRepository;




    public ProfilViewModel(@NonNull Application application) {
        super(application);
        workmatesRepository = new WorkmatesRepository();
    }

    public MutableLiveData<User>getUsersAtRestaurant(){
        return workmatesRepository.getUserFromFirestore();
    }


}
