package com.example.go4lunch.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.RestaurantItemBinding;
import com.example.go4lunch.models.RestaurantsInfo;
import com.example.go4lunch.models.RestaurantsResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<RestaurantsResult> listOfRestaurants;
    private Context context;

    public RestaurantAdapter(List<RestaurantsResult> listOfRestaurants, Context context) {
        this.listOfRestaurants = listOfRestaurants;
        this.context = context;
    }



    @NonNull
    @NotNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RestaurantViewHolder(RestaurantItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RestaurantViewHolder restaurantViewHolder, int position) {
        restaurantViewHolder.amendWithData(listOfRestaurants.get(position),context);
    }

    @Override
    public int getItemCount() {
         int itemCount = 0;
         if(listOfRestaurants != null) itemCount = listOfRestaurants.size();
        return itemCount;
    }
}
