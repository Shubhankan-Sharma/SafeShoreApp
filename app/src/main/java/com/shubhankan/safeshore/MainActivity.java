package com.shubhankan.safeshore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText searchBar;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupOverflowMenu();

        searchBar = findViewById(R.id.search_bar);
        gridLayout = findViewById(R.id.grid_layout); // Ensure this matches the ID in your layout XML

        setupBeachClickListeners();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }



    private void setupOverflowMenu() {
        ImageView overflowMenu = findViewById(R.id.overflow_menu); // Ensure you have an ImageView with this ID in your layout
        overflowMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, v);
            popup.getMenuInflater().inflate(R.menu.overflow_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_logout) {
                    // Handle logout click: clear login state and navigate to login activity
                    SharedPreferences sharedPreferences = getSharedPreferences("SafeShorePrefs", MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Close MainActivity
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }


    private void setupBeachClickListeners() {
        findViewById(R.id.beach1).setOnClickListener(v ->
                fetchBeachData("https://beach1.onrender.com/api/beach/beach1", R.drawable.beach1, "Beach 1")
        );
        findViewById(R.id.beach2).setOnClickListener(v ->
                fetchBeachData("https://beach2.onrender.com/api/beach/beach2", R.drawable.beach2, "Beach 2")
        );
        findViewById(R.id.beach3).setOnClickListener(v ->
                fetchBeachData("https://beach3.onrender.com/api/beach/beach3", R.drawable.beach3, "Beach 3")
        );
        findViewById(R.id.beach4).setOnClickListener(v ->
                fetchBeachData("https://beach4.onrender.com/api/beach/beach4", R.drawable.beach4, "Beach 4")
        );
    }

    private void showSafetyDialog(int safetyStatus, int imageResId, String beachName,
                                  double temperature, double currentspeed, double turbidity,
                                  double scattering, double ph, double tidelength,
                                  BeachPredictionResponse predictionResponse) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_safety_custom, null);

        TextView titleView = dialogView.findViewById(R.id.dialog_title);
        TextView messageView = dialogView.findViewById(R.id.dialog_message);
        Button okButton = dialogView.findViewById(R.id.dialog_ok_button);

        titleView.setText("Beach Safety Status");

        if (safetyStatus == 1) { // Safe case
            messageView.setText("The beach is safe, enjoy!!!");
            messageView.setBackgroundColor(Color.parseColor("#E0F7E0"));  // Very light green tint
            messageView.setTextColor(Color.parseColor("#256029"));        // Dark green text
            okButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#166534"))); // Dark green button
        } else { // Unsafe case
            messageView.setText("The beach is NOT safe for activities!");
            messageView.setBackgroundColor(Color.parseColor("#FDECEA"));  // Very light red tint
            messageView.setTextColor(Color.parseColor("#991B1B"));        // Dark red text
            okButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7F1D1D"))); // Dark red button
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialog.show();

        okButton.setTextColor(Color.WHITE);
        okButton.setOnClickListener(v -> {
            dialog.dismiss();
            openBeachDetail(imageResId, beachName, temperature, currentspeed,
                    turbidity, scattering, ph, tidelength, predictionResponse);
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }





    private void fetchBeachData(String apiUrl, int imageResId, String beachName) {
        ApiService apiService = RetrofitClient.getClient("https://safeshore.onrender.com").create(ApiService.class);
        Call<BeachDataResponse> call = apiService.getBeachData(apiUrl);

        call.enqueue(new Callback<BeachDataResponse>() {
            @Override
            public void onResponse(Call<BeachDataResponse> call, Response<BeachDataResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BeachData beachData = response.body().getData();

                    String beachName = beachData.getLocation();
                    double temperature = beachData.getTemperature();
                    double currentspeed = beachData.getCurrentspeed();
                    double ph = beachData.getPh();
                    double tidelength = beachData.getTidelength();
                    double turbidity = beachData.getTurbidity();
                    double scattering = beachData.getScattering();

                    // Log the fetched beach data
                    Log.e("BeachData", "Beach Name: " + beachName);
                    Log.e("BeachData", "Temperature: " + temperature);
                    Log.e("BeachData", "Current Speed: " + currentspeed);
                    Log.e("BeachData", "pH: " + ph);
                    Log.e("BeachData", "Turbidity: "+ turbidity);
                    Log.e("BeachData", "Scattering: "+ scattering);
                    Log.e("BeachData", "Tide Length: " + tidelength);

                    // Send the data to the prediction API
                    sendBeachDataForPrediction(temperature, currentspeed, ph, tidelength, turbidity, scattering, imageResId, beachName);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to retrieve beach data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeachDataResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void sendBeachDataForPrediction(double temperature, double currentspeed, double ph, double tidelength, double turbidity, double scattering, int imageResId, String beachName) {
        // Create the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("temperature", temperature);
        requestBody.put("currentspeed", currentspeed);
        requestBody.put("ph", ph);
        requestBody.put("scattering", scattering);
        requestBody.put("tideLength", tidelength);
        requestBody.put("turbidity", turbidity);

        // Log the request body
        Log.e("RequestBody:", new Gson().toJson(requestBody)); // Using Gson to convert the object to JSON

        ApiService apiService = RetrofitClient.getClient("https://safeshore.onrender.com").create(ApiService.class);
        Call<BeachPredictionResponse> call = apiService.predictBeachSafety(requestBody);

        // Execute the call
        call.enqueue(new Callback<BeachPredictionResponse>() {
            @Override
            public void onResponse(Call<BeachPredictionResponse> call, Response<BeachPredictionResponse> response) {
                Log.e("API Response", "Code: " + response.code() + ", Message: " + response.message());
                if (response.isSuccessful() && response.body() != null) {
                    BeachPredictionResponse predictionResponse = response.body();
                    // Log the full response for debugging
                    Log.e("PredictionResponse", new Gson().toJson(predictionResponse));

                    int safetyStatus = predictionResponse.getSafetyPrediction(); // Assuming this returns "Safe" or "Not Safe"
                    showSafetyDialog(safetyStatus, imageResId, beachName,
                            temperature, currentspeed, turbidity,
                            scattering, ph, tidelength, predictionResponse);

                    // Handle the response as needed

                } else {
                    Log.e("API Error", "Unsuccessful response: " + response.message());
                    Toast.makeText(MainActivity.this, "Failed to retrieve prediction data: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BeachPredictionResponse> call, Throwable t) {
                Log.e("API Failure", t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openBeachDetail(int imageResId, String beachName, double temperature, double currentspeed, double ph, double tidelength, double turbidity, double scattering, BeachPredictionResponse predictionResponse) {
        Intent intent = new Intent(MainActivity.this, BeachDetailActivity.class);
        intent.putExtra("imageResId", imageResId);
        intent.putExtra("BEACH_NAME", beachName);
        intent.putExtra("temperature", temperature);
        intent.putExtra("currentspeed", currentspeed);
        intent.putExtra("turbidity", turbidity);
        intent.putExtra("scattering", scattering);
        intent.putExtra("ph", ph);
        intent.putExtra("tidelength", tidelength);
        intent.putExtra("safety_prediction", predictionResponse.getSafetyPrediction());
        intent.putExtra("swimming", predictionResponse.getSwimming());
        intent.putExtra("scuba_diving", predictionResponse.getScubaDiving());
        intent.putExtra("surfing", predictionResponse.getSurfing());
        intent.putExtra("jet_skiing", predictionResponse.getJetSkiing());
        intent.putExtra("sunbathing", predictionResponse.getSunbathing());
        intent.putExtra("beach_volleyball", predictionResponse.getBeachVolleyball());
        startActivity(intent);
    }
}
