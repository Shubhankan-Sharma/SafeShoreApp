package com.shubhankan.safeshore;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        searchBar = findViewById(R.id.search_bar);
        gridLayout = findViewById(R.id.grid_layout); // Ensure this matches the ID in XML

        // Set up click listeners for each beach image
        setupBeachClickListeners();

        // Add a text change listener for the search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Implement search functionality here
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private void setupBeachClickListeners() {
        findViewById(R.id.beach1).setOnClickListener(v -> fetchSafetyStatus("1.0, 2.0, 3.0, 1.0, 2.0, 3.0")); // Example input
        findViewById(R.id.beach2).setOnClickListener(v -> fetchSafetyStatus("1.1, 2.1, 3.1, 1.1, 2.1, 3.1")); // Example input
        findViewById(R.id.beach3).setOnClickListener(v -> fetchSafetyStatus("1.2, 2.2, 3.2, 1.2, 2.2, 3.2")); // Example input
        findViewById(R.id.beach4).setOnClickListener(v -> fetchSafetyStatus("1.3, 2.3, 3.3, 1.3, 2.3, 3.3")); // Example input
    }
    private void openBeachDetail(int imageResId, String beachName, String windSpeed, String currentSpeed, String tideLength, String uvIndex) {
        Intent intent = new Intent(MainActivity.this, BeachDetailActivity.class);
        intent.putExtra("imageResId", imageResId);
        intent.putExtra("BEACH_NAME", beachName);
        intent.putExtra("windSpeed", windSpeed);
        intent.putExtra("currentSpeed", currentSpeed);
        intent.putExtra("tideLength", tideLength);
        intent.putExtra("uvIndex", uvIndex);
        startActivity(intent);
    }

    private void fetchSafetyStatus(String input) {
        // Convert the input string to a list of doubles
        List<Double> inputList = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Double::parseDouble)
                .collect(Collectors.toList());

        // Create the input data object
        InputData inputData = new InputData(inputList);

        // Create the Retrofit service
        Log.e("Input data: ", inputData.toString());
        ApiService apiService = RetrofitClient.getClient("https://safeshore.onrender.com/predict/").create(ApiService.class);
        Call<SafetyResponse> call = apiService.getSafetyStatus(inputData);

        call.enqueue(new Callback<SafetyResponse>() {
            @Override
            public void onResponse(Call<SafetyResponse> call, Response<SafetyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String safetyStatus = response.body().getSafetyStatus();
                    // Show the safety status in a Toast or update the UI accordingly
                    Toast.makeText(MainActivity.this, "Safety Status: " + safetyStatus, Toast.LENGTH_LONG).show();
                    Log.d("Safety status: ",safetyStatus.toString());
                } else {
                    Toast.makeText(MainActivity.this, "Failed to retrieve safety status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SafetyResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(MainActivity.this, BeachDetailActivity.class);
        startActivity(intent);
        finish();
    }
}