package com.example.go4lunch.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.go4lunch.databinding.FragmentItemUserBinding;




public class WorkmatesAdapter extends ListAdapter <UserStateItem, WorkmatesViewHolder>{



    public WorkmatesAdapter() { super(new WorkmatesViewHolder.ListRestaurantItemCallback()); }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkmatesViewHolder(FragmentItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder workmatesViewHolder, int position) {
       workmatesViewHolder.updateUserItem(getItem(position));
    }
}
