package com.example.alculator.operations;

import androidx.annotation.NonNull;

import com.example.alculator.operations.Operation;

public class Addition implements Operation {
    @Override
    public Double execute(Double a, Double b) {
        return a + b;
    }

    @NonNull
    @Override
    public String toString() {
        return "+";
    }
}
