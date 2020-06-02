package com.github.franckyi.emerald.service.web;

import com.github.franckyi.emerald.service.web.resource.mojang.auth.AuthError;

import java.io.IOException;

public class MojangAuthException extends IOException {
    private final AuthError error;

    public MojangAuthException(AuthError error) {
        super(error.getErrorMessage());
        this.error = error;
    }

    public AuthError getError() {
        return error;
    }
}
