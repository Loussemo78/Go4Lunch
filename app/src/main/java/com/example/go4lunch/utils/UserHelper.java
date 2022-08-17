package com.example.go4lunch.utils;


import com.example.go4lunch.repositories.WorkmatesRepository;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserHelper {

    public static final String COLLECTION_NAME = "users";

    private  final WorkmatesRepository workmatesRepository;


    public UserHelper() {
        this.workmatesRepository = WorkmatesRepository.getInstance();
    }

    public UserHelper(WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;
    }

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public  CollectionReference getCollectionUsers() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public  Task<DocumentSnapshot> getCurrentUsers(String id) {
        return UserHelper.getUsersCollection().document(id).get();
    }


}
