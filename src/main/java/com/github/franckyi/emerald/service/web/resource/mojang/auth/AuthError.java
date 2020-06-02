package com.github.franckyi.emerald.service.web.resource.mojang.auth;

import com.google.gson.annotations.SerializedName;

public class AuthError {
    private Type error;
    private String errorMessage;
    private String cause;

    public Type getType() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getCause() {
        return cause;
    }

    public enum Type {
        @SerializedName("Method Not Allowed") METHOD_NOT_ALLOWED,
        @SerializedName("Not Found") NOT_FOUND,
        @SerializedName("ForbiddenOperationException") FORBIDDEN_OPERATION_EXCEPTION,
        @SerializedName("IllegalArgumentException") ILLEGAL_ARGUMENT_EXCEPTION,
        @SerializedName("Unsupported Media Type") UNSUPPORTED_MEDIA_TYPE
    }
}
