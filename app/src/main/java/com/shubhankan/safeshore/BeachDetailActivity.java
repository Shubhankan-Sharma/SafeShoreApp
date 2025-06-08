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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Retrieve the data from the intent
        Intent intent = getIntent();
        int imageResId = intent.getIntExtra("imageResId", 0);
        String beachName = intent.getStringExtra("BEACH_NAME");

        double temperature = intent.getDoubleExtra("temperature", 0.0);
        double currentspeed = intent.getDoubleExtra("currentspeed", 0.0);
        double ph = intent.getDoubleExtra("ph", 0.0);
        double tidelength = intent.getDoubleExtra("tidelength", 0.0);
        double turbidity = intent.getDoubleExtra("turbidity", 0.0);
        double scattering = intent.getDoubleExtra("scattering", 0.0);
        int safety = intent.getIntExtra("safety_prediction",0);
        int swimming = intent.getIntExtra("swimming", 0);
        int scuba_diving = intent.getIntExtra("scuba_diving",0);
        int surfing = intent.getIntExtra("surfing", 0);
        int jet_skiing = intent.getIntExtra("jet_skiing",0);
        int sunbathing = intent.getIntExtra("sunbathing",0);
        int beach_volleyball = intent.getIntExtra("beach_volleyball",0);


        // Set the image and parameters
        ImageView beachImage = findViewById(R.id.beach_image);
        beachImage.setImageResource(imageResId);

        TextView beachNameTextView = findViewById(R.id.beach_name_text_view);
        beachNameTextView.setText(beachName);

        // Display safety status
        TextView safetyTextView = findViewById(R.id.safety);
        safetyTextView.setText(safety == 1 ? "Safe" : "Unsafe");

        // Display activity suitability
        TextView swimmingTextView = findViewById(R.id.swimming);
        swimmingTextView.setText(swimming == 1 ? "Safe" : "Unsafe");

        TextView scubaDivingTextView = findViewById(R.id.scuba_diving);
        scubaDivingTextView.setText(scuba_diving == 1 ? "Safe" : "Unsafe");

        TextView surfingTextView = findViewById(R.id.surfing);
        surfingTextView.setText(surfing == 1 ? "Safe" : "Unsafe");

        TextView sunbathingTextView = findViewById(R.id.sunbathing);
        sunbathingTextView.setText(sunbathing == 1 ? "Safe" : "Unsafe");

        TextView beachVolleyballTextView = findViewById(R.id.beach_volleyball);
        beachVolleyballTextView.setText(beach_volleyball == 1 ? "Safe" : "Unsafe");

        TextView jetSkiingTextView = findViewById(R.id.jet_skiing);
        jetSkiingTextView.setText(jet_skiing == 1 ? "Safe" : "Unsafe");

        TextView temperatureTextView = findViewById(R.id.temperature);
        temperatureTextView.setText(String.format("%.2f °C", temperature));

        TextView currentSpeedTextView = findViewById(R.id.current_speed);
        currentSpeedTextView.setText(String.format("%.2f m/s", currentspeed));

        TextView phTextView = findViewById(R.id.ph);
        phTextView.setText(String.format("%.2f", ph));

        TextView tideLengthTextView = findViewById(R.id.tide_length);
        tideLengthTextView.setText(String.format("%.2f m", tidelength));

        TextView turbidityTextView = findViewById(R.id.turbidity);
        turbidityTextView.setText(String.format("%.2f ntu", turbidity));

        TextView scatteringTextView = findViewById(R.id.scattering);
        scatteringTextView.setText(String.format("%.2f m⁻¹ sr⁻¹", scattering));


    }

    @Override
    public boolean onSupportNavigateUp() {
        // Navigate back to the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        return true;
    }
}