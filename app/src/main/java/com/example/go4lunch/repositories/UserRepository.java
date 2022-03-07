package com.example.go4lunch.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.models.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static final String IS_MENTOR_FIELD = "isMentor";
    private static volatile UserRepository instance;

    public UserRepository() {
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }


    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context) {
        return AuthUI.getInstance().delete(context);
    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser(User userToCreate) {
        this.getUsersCollection().document(userToCreate.getUid()).set(userToCreate);

    }
        /*Task<DocumentSnapshot> userData = getUserData();
        // If the user already exist in Firestore, we get his data (isMentor)
        userData.addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.contains(IS_MENTOR_FIELD)) {
                        userToCreate.setMentor((Boolean) documentSnapshot.get(IS_MENTOR_FIELD));
                    }*/
    // });



    // Get User Data from Firestore
  /* public Task<DocumentSnapshot> getUserData(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).get();
        }else{
            return null;
        }
    }*/

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

    // Update User Username
    public void updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        } else {
            Log.e("", "error");
        }
    }

    // Update User isMentor
    public void updateIsMentor(Boolean isMentor) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).update(IS_MENTOR_FIELD, isMentor);
        }
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).delete();
        }
    }
}
