package com.globant.util;

public class BudgetCheckResult {
    private boolean success;
    private String errorMessage;

    public BudgetCheckResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}