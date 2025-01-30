package com.shubhankan.safeshore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BeachDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beach_detail);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the up button

        // Retrieve the data from the intent
        Intent intent = getIntent();
        int imageResId = intent.getIntExtra("imageResId", 0);
        String beachName = intent.getStringExtra("BEACH_NAME");
        String windSpeedValue = intent.getStringExtra("windSpeed");
        String currentSpeedValue = intent.getStringExtra("currentSpeed");
        String tideLengthValue = intent.getStringExtra("tideLength");
        String uvIndexValue = intent.getStringExtra("uvIndex");

        // Set the image and parameters
        ImageView beachImage = findViewById(R.id.beach_image);
        beachImage.setImageResource(imageResId);

        TextView beachNameTextView = findViewById(R.id.beach_name_text_view);
        beachNameTextView.setText(beachName);

        TextView windSpeedTextView = findViewById(R.id.wind_speed);
        windSpeedTextView.setText("Wind Speed: " + windSpeedValue);

        TextView currentSpeedTextView = findViewById(R.id.current_speed);
        currentSpeedTextView.setText("Current Speed: " + currentSpeedValue);

        TextView tideLengthTextView = findViewById(R.id.tide_length);
        tideLengthTextView.setText("Tide Length: " + tideLengthValue);

        TextView uvIndexTextView = findViewById(R.id.uv_index);
        uvIndexTextView.setText("UV Index: " + uvIndexValue);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close this activity and return to the previous one
        return true;
    }
}