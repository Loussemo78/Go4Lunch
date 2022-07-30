package com.example.go4lunch.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.RestaurantsDetailActivity;
import com.example.go4lunch.SettingsActivity;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repositories.UserRepository;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Utility {
    private static final String PREFS = "com.example.go4lunch";
    private static final String LOCALE_KEY = "locale";
    private static final String PREFS_LIKES = "likes";
    private static final String PREFS_TOGGLE = "toggle";
    private static final String KEY_ID = "key_id";
    private final Gson gson = new Gson();
    private static volatile User instance;
    private OnClickButtonAlertDialog onClickButtonAlertDialog;


    public Utility(OnClickButtonAlertDialog onClickButtonAlertDialog) {
        this.onClickButtonAlertDialog = onClickButtonAlertDialog;
    }


    public Utility(){

    }
    public interface OnClickButtonAlertDialog {
        void positiveButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch);
        void negativeButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch);
    }
    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public String retreiveLocaleFromPrefs(Context context) {
        return getSharedPreferences(context).getString(LOCALE_KEY, "en");
    }

    public void setLocaleInPrefs(Context context, String locale) {
        getSharedPreferences(context)
                .edit()
                .putString(LOCALE_KEY, locale)
                .apply();
    }

    public void setLikeListInPrefs(Context context, List<String> likeList) {
        String json = gson.toJson(likeList);
        getSharedPreferences(context)
                .edit()
                .putString(PREFS_LIKES, json)
                .apply();
    }

    public List<String> retriveLikeList(Context context) {
        String json = getSharedPreferences(context).getString(PREFS_LIKES, "[]");
        ArrayList<String> list;
        if (json == null || json.isEmpty()) {
            list = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            list = gson.fromJson(json, type);
        }

        return list;
    }

    public String distance(double userLat, double targetLat, double userLng, double targetLng) {

        final int earth = 6371; // Earth radius

        double latDistance = Math.toRadians(targetLat - userLat);
        double lonDistance = Math.toRadians(targetLng - userLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(targetLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earth * c * 1000; // convert to meters

        double height = 0.0 - 0.0;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        // Rounded
        return Math.round(Math.sqrt(distance)) + "m";
    }

    public float convertRating(double rating) {
        rating = (rating * 3) / 5;
        return (float) rating;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void showAlertDialog(Context context, String dialogTitle, String dialogMessage,
                                String positiveButtonText, String negativeButtonText,
                                int dialogDrawableBackground, int dialogDrawableIcon, int dialogIdForSwitch){

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle(dialogTitle);
        dialogBuilder.setMessage(dialogMessage);
        dialogBuilder.setPositiveButton(positiveButtonText, (dialog, which) -> onClickButtonAlertDialog.positiveButtonDialogClicked(dialog, dialogIdForSwitch));
        dialogBuilder.setNegativeButton(negativeButtonText, (dialog, which) -> onClickButtonAlertDialog.negativeButtonDialogClicked(dialog, dialogIdForSwitch));
        alertBody(context, dialogDrawableBackground, dialogDrawableIcon, dialogBuilder);
    }

    private void alertBody(Context context, int dialogDrawableBackground, int dialogDrawableIcon, MaterialAlertDialogBuilder dialogBuilder) {
        dialogBuilder.setIcon(dialogDrawableIcon);
        dialogBuilder.setBackground(ActivityCompat.getDrawable(context, dialogDrawableBackground));
        dialogBuilder.show();
    }

    // Get the date of the day
    public  String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        // Create a SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        // Get today date
        return sdf.format(calendar.getTime());
    }

    public void showSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    public void startDetailsRestaurantActivity(Context context, String id) {
        Intent intent = new Intent(context, RestaurantsDetailActivity.class);
        intent.putExtra(KEY_ID, id);
        context.startActivity(intent);
    }

}
