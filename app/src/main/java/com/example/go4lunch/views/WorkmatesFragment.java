package com.example.go4lunch.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.RestaurantsDetailActivity;
import com.example.go4lunch.databinding.FragmentWorkMatesBinding;
import com.example.go4lunch.utils.Utility;


public class WorkmatesFragment extends Fragment  {
    private ViewModelWorkmates workmatesViewModel;
    private FragmentWorkMatesBinding binding;
    private RecyclerView recyclerView;
    private WorkmatesAdapter workmatesAdapter;
    private Utility utility = new Utility();


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
}