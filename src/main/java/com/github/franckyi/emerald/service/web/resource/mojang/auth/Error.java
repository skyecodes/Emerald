package com.github.franckyi.emerald.service.web.resource.mojang.auth;

public class Error {
    private ErrorType error;
    private String errorMessage;
    private String cause;

    public ErrorType getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getCause() {
        return cause;
    }
}
