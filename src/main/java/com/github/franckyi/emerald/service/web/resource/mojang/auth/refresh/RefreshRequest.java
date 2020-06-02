package com.github.franckyi.emerald.service.web.resource.mojang.auth.refresh;

public class RefreshRequest {
    private final String accessToken;
    private final String clientToken;
    private final boolean requestUser;

    public RefreshRequest(String accessToken, String clientToken, boolean requestUser) {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
        this.requestUser = requestUser;
    }
}
