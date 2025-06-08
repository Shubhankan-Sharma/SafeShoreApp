package com.shubhankan.safeshore;

import com.google.gson.annotations.SerializedName;

public class BeachPredictionResponse {
    @SerializedName("Beach Volleyball")
    private int beachVolleyball;

    @SerializedName("Jet Skiing")
    private int jetSkiing;

    @SerializedName("Scuba Diving")
    private int scubaDiving;

    @SerializedName("Sunbathing")
    private int sunbathing;

    @SerializedName("Surfing")
    private int surfing;

    @SerializedName("Swimming")
    private int swimming;

    @SerializedName("safety_prediction")
    private int safetyPrediction;

    @SerializedName("currentspeed")
    private double currentSpeed;

    @SerializedName("ph")
    private double ph;

    @SerializedName("temperature")
    private double temperature;

    @SerializedName("tideLength")
    private double tideLength;

    @SerializedName("turbidity")
    private double turbidity;

    @SerializedName("scattering")
    private double scattering;

    // Getters
    public int getBeachVolleyball() { return beachVolleyball; }
    public int getJetSkiing() { return jetSkiing; }
    public int getScubaDiving() { return scubaDiving; }
    public int getSunbathing() { return sunbathing; }
    public int getSurfing() { return surfing; }
    public int getSwimming() { return swimming; }
    public int getSafetyPrediction() { return safetyPrediction; }
    public double getCurrentSpeed() { return currentSpeed; }
    public double getPh() { return ph; }
    public double getTemperature() { return temperature; }
    public double getTideLength() { return tideLength; }
    public double getTurbidity() { return turbidity; }
    public double getScattering() { return scattering; }
}

