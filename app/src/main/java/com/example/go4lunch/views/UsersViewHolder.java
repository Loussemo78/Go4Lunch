package com.example.go4lunch.views;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.databinding.FragmentItemUserBinding;
import com.example.go4lunch.models.User;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    FragmentItemUserBinding binding;

    public UsersViewHolder(FragmentItemUserBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void updateUserItem(User user) {
        //binding.userText.setText(user.getUsername());
        //binding.userText.getResources().getString(R.string.is_eating_at);

        binding.userText.setText(user.getUsername());
        //binding.userText.getResources().getString(R.string.hasn_not_decided);
        String url = user.getUrlPicture();
        Glide.with(binding.userPhoto)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.userPhoto);
    }
    public static class ListRestaurantItemCallback extends DiffUtil.ItemCallback<User>{
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUsername().equals(newItem.getUsername()) &&  oldItem.getUrlPicture().equals(newItem.getUrlPicture());
        }
        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    }
}