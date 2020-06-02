package com.github.franckyi.emerald.service.web.resource.mojang.auth.signout;

public class SignoutRequest {
    private final String username;
    private final String password;

    public SignoutRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
