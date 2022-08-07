package com.example.go4lunch.views;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentListBinding;
import com.example.go4lunch.databinding.SearchListBinding;
import com.example.go4lunch.models.PlacesAutocomplete;
import com.example.go4lunch.models.Prediction;
import com.example.go4lunch.models.RestaurantsInfo;
import com.example.go4lunch.models.RestaurantsResult;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {
    private FragmentListBinding binding;
    private List<RestaurantsResult> listRestaurants = new ArrayList<>();
    private List<Prediction> predictions = new ArrayList<>();

    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private Go4LunchViewModel go4LunchViewModel;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        go4LunchViewModel = new ViewModelProvider(requireActivity()).get(Go4LunchViewModel.class);
        configureRecyclerView();
        configureViewModel();
        //configureAutocomplete();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //  Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = binding.recyclerViewRestaurants;
        recyclerView.setLayoutManager(layoutManager);
        restaurantAdapter = new RestaurantAdapter(listRestaurants, requireActivity());
        recyclerView.setAdapter(restaurantAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void configureViewModel() {
        go4LunchViewModel.getRestaurants().observe(getViewLifecycleOwner(), listRestaurant -> {
            listRestaurants.clear();
            listRestaurants.addAll(listRestaurant.getResults());
            recyclerView.getAdapter().notifyDataSetChanged();
        });
    }
}