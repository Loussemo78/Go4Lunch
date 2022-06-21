package com.example.go4lunch.views;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.MainActivity;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentItemUserBinding;
import com.example.go4lunch.models.User;

import java.util.Locale;


public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    private FragmentItemUserBinding binding;

    public WorkmatesViewHolder(FragmentItemUserBinding binding ) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void updateUserItem(UserStateItem user) {
        //binding.userText.getResources().getString(R.string.is_eating_at);

        binding.userText.setText(user.getUsername());
        //binding.userText.getResources().getString(R.string.hasn_not_decided);
        String url = user.getUrlPicture();
        Glide.with(binding.userPhoto)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.userPhoto);
    }
    public static class ListRestaurantItemCallback extends DiffUtil.ItemCallback<UserStateItem>{
        @Override
        public boolean areItemsTheSame(@NonNull UserStateItem oldItem, @NonNull UserStateItem newItem) {
            return oldItem.getUsername().equals(newItem.getUsername()) &&  oldItem.getUrlPicture().equals(newItem.getUrlPicture());
        }
        @Override
        public boolean areContentsTheSame(@NonNull UserStateItem oldItem, @NonNull UserStateItem newItem) {
            return oldItem.equals(newItem);
        }
    }
}

