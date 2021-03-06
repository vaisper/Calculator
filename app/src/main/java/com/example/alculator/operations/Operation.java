package com.example.alculator.operations;

public interface Operation {
    Double execute(Double a, Double b);
    default boolean validateOperation(Double a, Double b) {
        return true;
    }

    default String getResultForNonValidatedOperation() {
        return new String("");
    }
}
