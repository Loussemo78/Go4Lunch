package com.example.go4lunch;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.go4lunch.models.RestaurantDetail;
import com.example.go4lunch.models.User;
import com.example.go4lunch.utils.Utility;
import com.example.go4lunch.views.RestaurantDetailViewModel;
import com.example.go4lunch.views.UserAdapter;
import com.example.go4lunch.views.ViewModelWorkmates;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.util.ObjectsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.go4lunch.databinding.ActivityRestaurantsDetailBinding;

import java.util.List;
import java.util.Objects;

public class RestaurantsDetailActivity extends AppCompatActivity implements Utility.OnClickButtonAlertDialog {

    private ActivityRestaurantsDetailBinding binding;
    private RestaurantDetailViewModel restaurantDetailViewModel;
    private ViewModelWorkmates viewModelWorkmates;
    private List<User> users;
    private Utility utils;
    private UserAdapter userAdapter;
    public static final String DETAIL_RESTAURANT = "place_id";
    public static final String RESTAURANT_ID = "restaurantId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        utils = new Utility(this);
        configureViewModel();
        configureWorkmatesViewModel();
        Intent intent = getIntent();
        if (intent.hasExtra(DETAIL_RESTAURANT)) {
            //configureRecyclerView();
            initUserList();
            restaurantDetailViewModel.getRestaurants(intent.getStringExtra(DETAIL_RESTAURANT)).observe(this, this::initView);
            restaurantDetailViewModel.getUsersAtRestaurant(intent.getStringExtra(DETAIL_RESTAURANT)).observe(this, userAdapter::submitList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initUserList() {
        binding.restaurantDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter();
        binding.restaurantDetailsRecyclerView.setAdapter(userAdapter);
        binding.restaurantDetailsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initView(RestaurantDetail detail) {

        binding.restaurantDetailsName.setText(detail.getResult().getDetailName());
        binding.restaurantDetailsAddress.setText(detail.getResult().getDetailVicinity());
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + detail.getResult().getDetailPhotos().get(0).getPhotoReference()
                + "&key="
                + BuildConfig.GOOGLE_MAP_API_KEY;
        Glide.with(binding.imgRestaurantDetails.getContext())
                .load(url)
                .into(binding.imgRestaurantDetails);

        binding.ButtonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.restaurantDetailsFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurantDetailViewModel.getUsersToRestaurant().observe(RestaurantsDetailActivity.this, user -> {
                    if (user.getRestaurantId() != null && user.getRestaurantId().equals(detail.getResult().getDetailPlaceId())) {
                        restaurantDetailViewModel.updateUserRestaurant("", "");
                    } else if (user.getRestaurantId() == null || user.getRestaurantId().equals("")) {
                        restaurantDetailViewModel.updateUserRestaurant(detail.getResult().getDetailPlaceId(), detail.getResult().getDetailName());
                    } else if (!user.getRestaurantId().equals(detail.getResult().getDetailPlaceId())) {
                        restaurantDetailViewModel.setRestaurantNameAndIdToFirestore(detail.getResult().getDetailPlaceId(), detail.getResult().getDetailName());
                        binding.restaurantDetailsFloatingBtn.setImageResource(R.drawable.ic_baseline_settings_24);
                    }
                });
            }
        });

        binding.restaurantCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurantDetailViewModel.getRestaurants(detail.getResult().getDetailPlaceId()).observe(RestaurantsDetailActivity.this, restaurant -> {
                    if (!restaurant.getResult().getFormattedPhoneNumber().equals(getString(R.string.phone_dont_set))) {
                        String tel = restaurant.getResult().getFormattedPhoneNumber().replace(" ", "");
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + tel));
                        startActivity(callIntent);
                    } else {
                        alertDialog(2, getString(R.string.error_no_phone));
                    }
                });
            }
        });

        binding.restaurantLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurantDetailViewModel.getUsersLikedRestaurant().observe(RestaurantsDetailActivity.this, likedRestaurant -> {
                    if (likedRestaurant == null || !ObjectsCompat.equals(likedRestaurant.getId(), detail.getResult().getDetailPlaceId())) {
                        restaurantDetailViewModel.setLikedRestaurantById(detail.getResult().getDetailPlaceId(), detail.getResult().getDetailName());
                        binding.restaurantLikeButton.setImageResource(R.drawable.ic_baseline_star);
                    } else {
                        restaurantDetailViewModel.deleteLikedRestaurant();
                        binding.restaurantLikeButton.setImageResource(R.drawable.ic_baseline_settings_24);
                    }
                });
            }
        });

        binding.restaurantWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurantDetailViewModel.getRestaurants(detail.getResult().getDetailPlaceId()).observe(RestaurantsDetailActivity.this, restaurant -> {
                    if (!restaurant.getResult().getWebsite().equals(getString(R.string.no_website_found))) {
                        String url = restaurant.getResult().getWebsite();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } else {
                        alertDialog(1, getString(R.string.error_no_website));

                    }
                });
            }
        });

    }

    private void configureViewModel() {
        restaurantDetailViewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);
    }

    private void configureWorkmatesViewModel() {
        viewModelWorkmates = new ViewModelProvider(this).get(ViewModelWorkmates.class);
    }


    public static void navigate(FragmentActivity activity, String placeId) {
        Intent intent = new Intent(activity, RestaurantsDetailActivity.class);
        intent.putExtra(DETAIL_RESTAURANT, placeId);
        ActivityCompat.startActivity(activity, intent, null);
    }

    private void alertDialog(int id, String message) {
        utils.showAlertDialog(this, getString(R.string.warning), message,
                getString(R.string.ok_btn), getString(R.string.cancel_btn), R.drawable.com_facebook_auth_dialog_background, R.drawable.com_facebook_button_background, id);
    }

    @Override
    public void positiveButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch) {

    }

    @Override
    public void negativeButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch) {

    }
}

