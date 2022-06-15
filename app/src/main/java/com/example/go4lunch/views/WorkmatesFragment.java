package com.example.go4lunch.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.databinding.FragmentWorkMatesBinding;


public class WorkmatesFragment extends Fragment {
    private ViewModelWorkmates workmatesViewModel;
    private FragmentWorkMatesBinding binding;
    private RecyclerView recyclerView;
    private WorkmatesAdapter workmatesAdapter;


    public WorkmatesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workmatesViewModel = new ViewModelProvider(requireActivity()).get(ViewModelWorkmates.class);
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWorkMatesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        configureRecyclerView();
        return view;
    }

    private void configureRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = binding.recyclerViewUsers;
        recyclerView.setLayoutManager(layoutManager);
        workmatesAdapter = new WorkmatesAdapter();
        recyclerView.setAdapter(workmatesAdapter);
        displayUserChoice();
        //getBaseList();
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void getBaseList() {
        workmatesViewModel.getAllWorkmates().observe(getViewLifecycleOwner(), workmatesAdapter::submitList);
    }

   private void displayUserChoice(){
        workmatesViewModel.getUsersOnRestaurant().observe(getViewLifecycleOwner(), users -> {
            workmatesAdapter.submitList(users);
        });
   }


   /* public void getUserProfile() {
        // [START get_user_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        // [END get_user_profile]
    }*/
    /*private void getListOfUsers() {
        //Do list of UID
        UserHelper.getUsersCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                // convert document to POJO
                                List<User> user = document.toObject(User.class);
                                users.addAll(user.getUsers());
                            }
                        } else {
                            Log.d("User fragment", "Error getting documents: ", task.getException());
                        }
                        workmatesAdapter.notifyDataSetChanged();
                    }
                });
    }*/

   /* private void configureViewModel(){
     workmatesViewModel = new ViewModelProvider(requireActivity()).get(WorkmatesViewModel.class);
     workmatesViewModel.getCurrentUserData().observe(getViewLifecycleOwner(), user -> {
         users.clear();
         users.addAll(user.getUsers());
         //users = user.getUsers();
         binding.recyclerViewUsers.getAdapter().notifyDataSetChanged();
     });
    }*/



    /*private void updateUIWithUserData(){
        if(workmatesViewModel.isCurrentUserLogged()){
            FirebaseUser user = workmatesViewModel.getCurrentUser();

            if(user.getPhotoUrl() != null){
                configureRecyclerView();
                //setProfilePicture(user.getPhotoUrl());
            }
            //setTextUserData(user);
        }
    }*/

   /* private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageView);
    }

    /*private void setTextUserData(FirebaseUser user){

        //Get email & username from User
        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

        //Update views with data
        binding.usernameEditText.setText(username);
        binding.emailTextView.setText(email);
    }


    private void updateLoginButton(){
        binding.loginButton.setText(userManager.isCurrentUserLogged() ? getString(R.string.button_login_text_logged) : getString(R.string.button_login_text_not_logged));
    }

     Launching Profile Activity
    private void startProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    private void setupListeners(){
        // Login/Profile Button
        binding.loginButton.setOnClickListener(view -> {
            if(userManager.isCurrentUserLogged()){
                startMainActivity();
            }else{
                startLoginActivity();
            }
        });
    }*/

   /* private void getUserData(){
        workmatesViewModel.getCurrentUserData();            // Set the data with the user information
    }*/

}