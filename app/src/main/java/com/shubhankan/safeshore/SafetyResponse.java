package com.shubhankan.safeshore;

public class SafetyResponse {
    private String safety_status; // Adjust the property name based on your API response

    // Getter
    public String getSafetyStatus() {
        return safety_status;
    }

    // Setter
    public void setSafetyStatus(String safety_status) {
        this.safety_status = safety_status;
    }
}