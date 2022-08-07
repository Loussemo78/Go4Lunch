package com.example.go4lunch.views;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.RestaurantsDetailActivity;
import com.example.go4lunch.databinding.RestaurantItemBinding;
import com.example.go4lunch.models.RestaurantsResult;
import com.example.go4lunch.utils.GeometryUtil;
import com.example.go4lunch.utils.UserHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;


public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private final RestaurantItemBinding binding;
    private Context context;
    public static final String DETAIL_RESTAURANT = "place_id";
    private int number_users;
    private List<UserStateItem> userStateItemList;


    public RestaurantViewHolder(RestaurantItemBinding restaurantItemBinding) {
        super(restaurantItemBinding.getRoot());
        this.binding = restaurantItemBinding;
    }

    public void amendWithData(RestaurantsResult result, Context context) {
        this.context = context;
        // Displays name of  restaurant
        binding.restaurantsName.setText(result.getName());
        checkIfUser(result.getPlaceId());

        // Displays restaurant address
        binding.restaurantsAddress.setText(result.getVicinity());

        // Displays if the restaurant is open at the moment
        if (result.getOpeningHours() != null) {
            if (result.getOpeningHours().getOpenNow()) {
                binding.restaurantsOpening.setText(R.string.open);
                binding.restaurantsOpening.getResources().getColor(R.color.teal_200);
            } else {
                binding.restaurantsOpening.setText(R.string.closed);
                binding.restaurantsOpening.getResources().getColor(R.color.red);
            }
        }
        // Displays distance
        double distanceDouble = GeometryUtil.calculateDistance(context, result.getGeometry().getLocation().getLng(), result.getGeometry().getLocation().getLat());
        String distanceString;
        Double convertData = distanceDouble / 1000;
        //int distanceInMeters = (int) distance;
        if (distanceDouble > 900.0) {
            distanceString = String.valueOf(GeometryUtil.roundOneDecimal(convertData)+"km");
            binding.restaurantsDistance.setText(distanceString);
        } else {
             distanceString = String.valueOf(Math.round(convertData) + "m");
            binding.restaurantsDistance.setText(distanceString);
        }
        binding.numberPicture.getDrawable();

        // Shows a number of stars based on the restaurant's rating
        if (result.getRating() != null) {
            float numStars = binding.ratingBar.getNumStars();
            if (numStars > 1) {
                binding.ratingBar.setRating(1);
            }
            if (numStars > 2.5) {
                binding.ratingBar.setRating(2);
            }
            if (numStars > 4) {
                binding.ratingBar.setRating(3);
            }
        }
        //Display restaurant photo
        loadImage(result);
        binding.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RestaurantsDetailActivity.class);
                String img = result.getPlaceId();
                intent.putExtra(DETAIL_RESTAURANT, img);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void checkIfUser(String restaurantId) {
        number_users = 0;
        UserHelper.getUsersCollection()
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                number_users++;
                            }
                            if (number_users > 0) {
                                binding.workmatesNumber.setVisibility(View.VISIBLE);
                                //users.setVisibility(View.VISIBLE);
                                String numberOfUsers = "(" + number_users + ")";
                                binding.workmatesNumber.setText(numberOfUsers);
                            } else {
                                binding.workmatesNumber.setVisibility(View.INVISIBLE);
                                //users.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Log.d("manager", "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("checkIfUser", e.getMessage());
            }
        });

    }


    private void loadImage(RestaurantsResult result) {
        if (result != null && result.getPhotos() != null && !result.getPhotos().isEmpty()) {
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + result.getPhotos().get(0).getPhotoReference()
                    + "&key="
                    + BuildConfig.GOOGLE_MAP_API_KEY;
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.mainPic);
        }

    }
}
