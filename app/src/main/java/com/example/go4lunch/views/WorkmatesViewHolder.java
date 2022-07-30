package com.example.go4lunch.views;



import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.RestaurantsDetailActivity;
import com.example.go4lunch.databinding.FragmentItemUserBinding;
import com.example.go4lunch.models.RestaurantsResult;


public  class WorkmatesViewHolder extends RecyclerView.ViewHolder  {

    private static final String DETAIL_RESTAURANT = "place_id";
    private FragmentItemUserBinding binding;



    public WorkmatesViewHolder(FragmentItemUserBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    
    public void updateUserItem(UserStateItem user) {

        binding.userText.setText(user.getUsername());
        String url = user.getUrlPicture();
        Glide.with(binding.userPhoto)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.userPhoto);
        String restaurantName = user.getRestaurantName();
        String restaurantChoice = "(" + restaurantName + ")";

        binding.restaurantName.setText(restaurantChoice);
        binding.itemConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getChosenRestaurant() != null) {
                    Intent intent = new Intent(v.getContext(), RestaurantsDetailActivity.class);
                    String result = user.getChosenRestaurant().getId();
                    intent.putExtra(DETAIL_RESTAURANT, result );
                    v.getContext().startActivity(intent);
                }

            }
        });
    }




    public static class ListRestaurantItemCallback extends DiffUtil.ItemCallback<UserStateItem> {
        @Override
        public boolean areItemsTheSame(@NonNull UserStateItem oldItem, @NonNull UserStateItem newItem) {
            return oldItem.getUsername().equals(newItem.getUsername()) && oldItem.getUrlPicture().equals(newItem.getUrlPicture());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserStateItem oldItem, @NonNull UserStateItem newItem) {
            return oldItem.equals(newItem);
        }
    }


}

