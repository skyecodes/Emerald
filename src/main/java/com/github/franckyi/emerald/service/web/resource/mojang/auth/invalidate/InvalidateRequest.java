package com.github.franckyi.emerald.service.web.resource.mojang.auth.invalidate;

public class InvalidateRequest {
    private final String accessToken;
    private final String clientToken;

    public InvalidateRequest(String accessToken, String clientToken) {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
    }
}
