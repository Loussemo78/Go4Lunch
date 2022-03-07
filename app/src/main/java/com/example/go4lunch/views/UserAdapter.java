package com.example.go4lunch.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.databinding.FragmentItemUserBinding;
import com.example.go4lunch.models.User;

public class UserAdapter extends ListAdapter<User, UsersViewHolder> {



    public UserAdapter() { super(new UsersViewHolder.ListRestaurantItemCallback()); }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersViewHolder(FragmentItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position) {
        usersViewHolder.updateUserItem(getItem(position));
    }
}