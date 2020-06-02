package com.github.franckyi.emerald.service.web.resource.mojang.auth.authenticate;

public class AuthenticateRequest {
    private final Agent agent;
    private final String username;
    private final String password;
    private final boolean requestUser;

    public AuthenticateRequest(String username, String password) {
        Agent agent = new Agent();
        agent.name = "Minecraft";
        agent.version = 1;
        this.agent = agent;
        this.username = username;
        this.password = password;
        this.requestUser = true;
    }

    private static class Agent {
        private String name;
        private int version;

        private Agent() {
        }
    }
}
