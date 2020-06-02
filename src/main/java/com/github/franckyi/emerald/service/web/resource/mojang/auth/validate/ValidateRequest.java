package com.github.franckyi.emerald.service.web.resource.mojang.auth.validate;

public class ValidateRequest {
    private final String accessToken;
    private final String clientToken;

    public ValidateRequest(String accessToken, String clientToken) {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
    }
}
