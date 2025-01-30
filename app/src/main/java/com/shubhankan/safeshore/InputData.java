package com.shubhankan.safeshore;

import java.util.List;

public class InputData {
    private List<Double> input;

    // Constructor
    public InputData(List<Double> input) {
        this.input = input;
    }

    // Getter
    public List<Double> getInput() {
        return input;
    }

    // Setter
    public void setInput(List<Double> input) {
        this.input = input;
    }
}