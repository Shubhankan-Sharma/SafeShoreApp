package com.shubhankan.safeshore;

import java.util.Map;

public class SafetyResponse {
    private String safety_status;
    private Map<String, String> activities;

    public String getSafetyStatus() {
        return safety_status;
    }

    public Map<String, String> getActivities() {
        return activities;
    }
}