package com.shubhankan.safeshore;

public class BeachPredictionRequest {
    private double temperature;
    private double currentspeed;
    private double ph;
    private double scattering;
    private double tidelength;
    private double turbidity;
    public BeachPredictionRequest(double temperature, double currentspeed, double ph, double scattering, double tidelength, double turbidity) {
        this.temperature = temperature;
        this.currentspeed = currentspeed;
        this.ph = ph;
        this.scattering = scattering;
        this.tidelength = tidelength;
        this.turbidity = turbidity;
    }
    // Getters and setters (if needed)
}