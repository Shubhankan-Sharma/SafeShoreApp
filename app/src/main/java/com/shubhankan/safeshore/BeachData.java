package com.shubhankan.safeshore;

public class BeachData {
    private double currentspeed;
    private double ph;
    private double temperature;
    private double tidelength;
    private double turbidity;
    private double scattering;
    private String location;
    private String timestamp;  // Include if you want the timestamp; else you can omit this field

    // Getters - required for Retrofit/Gson to deserialize properly
    public String getLocation() {
        return location; // This will return the beach name
    }
    public double getCurrentspeed() {
        return currentspeed;
    }
    public double getScattering() {
        return scattering;
    }
    public double getTurbidity() {
        return turbidity;
    }

    public double getPh() {
        return ph;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTidelength() {
        return tidelength;
    }

    public String getTimestamp() {
        return timestamp;
    }
}