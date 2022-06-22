package com.example.go4lunch.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;


import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.RestaurantsDetailActivity;
import com.example.go4lunch.databinding.RestaurantItemBinding;
import com.example.go4lunch.models.RestaurantsResult;
import com.example.go4lunch.repositories.WorkmatesRepository;
import com.example.go4lunch.utils.GeometryUtil;
import com.example.go4lunch.utils.UserHelper;

import java.util.List;
import java.util.Objects;


public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private final RestaurantItemBinding binding;
    private Context context;
    public static final String DETAIL_RESTAURANT = "place_id";
    private List<UserStateItem> userStateItemList;



    public RestaurantViewHolder( RestaurantItemBinding restaurantItemBinding) {
        super(restaurantItemBinding.getRoot());
          this.binding = restaurantItemBinding;
    }

    public void amendWithData(RestaurantsResult result, Context context) {
        this.context = context;
        // Displays name of  restaurant
        binding.restaurantsName.setText(result.getName());

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
        double distance = GeometryUtil.calculateDistance(context, result.getGeometry().getLocation().getLng(), result.getGeometry().getLocation().getLat());
        int distanceInMeters = (int) distance;
        if (distanceInMeters > 999) {
            binding.restaurantsDistance.setText(GeometryUtil.getString1000Less(distance));
        } else {
            String distanceString = distanceInMeters + " m";
            binding.restaurantsDistance.setText(distanceString);
        }
        binding.numberPicture.getDrawable();

        int numberEatingAt = 0;
        UserHelper userHelper = new UserHelper(WorkmatesRepository.getInstance());
        LiveData<List<UserStateItem>> usersChoiceRestaurant = userHelper.getUsersChoiceRestaurant();
        for (int i = 0; i < Objects.requireNonNull(usersChoiceRestaurant.getValue()).size(); i++) {
            if (usersChoiceRestaurant.getValue().get(i).getChosenRestaurant().getPlaceId().equals(result.getPlaceId())){
                numberEatingAt++;
            }
        }

        if (numberEatingAt > 0){
            binding.workmatesNumber.setVisibility(View.VISIBLE);
            binding.workmatesNumber.setText(String.valueOf(numberEatingAt));
        }else{
            binding.workmatesNumber.setVisibility(View.INVISIBLE);
        }

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
                intent.putExtra( DETAIL_RESTAURANT, img );
                v.getContext().startActivity(intent);
            }
        });
    }


    private void loadImage(RestaurantsResult result) {
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + result.getPhotos().get(0).getPhotoReference()
                + "&key="
                + BuildConfig.GOOGLE_MAP_API_KEY;
        Glide.with(context)
                .load(url)
                .into(binding.mainPic);
    }
}
