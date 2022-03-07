package com.example.go4lunch.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ItemAutocompleteBinding;
import com.example.go4lunch.models.Prediction;

public class AutocompleteAdapter extends ListAdapter<Prediction, AutocompleteAdapter.AutocompleteViewHolder> {

    @NonNull
    private final OnAutocompleteResultClickCallback onAutocompleteResultClickCallback;

    public AutocompleteAdapter(@NonNull OnAutocompleteResultClickCallback onAutocompleteResultClickCallback) {
        super(new AutocompleteDiffUtil());
        this.onAutocompleteResultClickCallback = onAutocompleteResultClickCallback;
    }


    @NonNull
    @Override
    public AutocompleteAdapter.AutocompleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new AutocompleteViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_autocomplete,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull AutocompleteAdapter.AutocompleteViewHolder holder, int position) {
        holder.bind(getItem(position), onAutocompleteResultClickCallback);

    }
    static class AutocompleteViewHolder extends RecyclerView.ViewHolder{

        @NonNull
        private final ItemAutocompleteBinding binding;


        public AutocompleteViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAutocompleteBinding.bind(itemView);
        }
        void bind(
                @NonNull Prediction autocompleteRestaurant,
                @NonNull OnAutocompleteResultClickCallback onAutocompleteResultClickCallback
        ) {
            binding.searchResultLabel.setText(autocompleteRestaurant.getDescription());

            itemView.setOnClickListener(v -> {
                onAutocompleteResultClickCallback.onClick(autocompleteRestaurant);
            });
        }
    }

    static class AutocompleteDiffUtil extends DiffUtil.ItemCallback<Prediction> {

        @Override
        public boolean areItemsTheSame(
                @NonNull Prediction oldItem,
                @NonNull Prediction newItem
        ) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId());
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull Prediction oldItem,
                @NonNull Prediction newItem
        ) {
            return oldItem.equals(newItem);
        }
    }

    public interface OnAutocompleteResultClickCallback {
        void onClick(@NonNull Prediction restaurantName);
    }
}
