package com.example.alculator.operations;
import androidx.annotation.NonNull;

import com.example.alculator.operations.Operation;

public class Divison implements Operation {
    @Override
    public Double execute(Double a, Double b) {
        return a / b;
    }

    @Override
    public boolean validateOperation(Double a, Double b) {
        if (b == 0d) {
            return false;
        }
        return true;
    }

    @Override
    public String getResultForNonValidatedOperation() {
        return "Cannot divide by zero";
    }

    @NonNull
    @Override
    public String toString() {
        return "÷";
    }
}
