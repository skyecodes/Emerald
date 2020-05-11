package com.github.franckyi.emerald.service.web.resource.mojang.auth;

public class AuthRequest {
    private Agent agent;
    private String username;
    private String password;
    private boolean requestUser;

    private AuthRequest() {
    }

    public static AuthRequest create(String username, String password) {
        Agent agent = new Agent();
        agent.name = "Minecraft";
        agent.version = 1;
        AuthRequest request = new AuthRequest();
        request.agent = agent;
        request.username = username;
        request.password = password;
        request.requestUser = true;
        return request;
    }

    public static class Agent {
        private String name;
        private int version;

        private Agent() {
        }

    }
}
