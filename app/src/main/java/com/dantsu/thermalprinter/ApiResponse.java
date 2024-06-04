package com.dantsu.thermalprinter;

public class ApiResponse {
    private boolean condition;
    private String message;

    // Getters and setters
    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}