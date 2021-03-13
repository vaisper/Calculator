package com.example.alculator.operations;

import androidx.annotation.NonNull;

public class Multiplication implements Operation {
    @Override
    public Double execute(Double a, Double b) {
        return a * b;
    }

    @NonNull
    @Override
    public String toString() {
        return "×";
    }
}
