package com.github.franckyi.emerald.service.web.resource.mojang.auth.authenticate;

import java.util.List;

public class AuthenticateResponse {
    private User user;
    private String accessToken;
    private String clientToken;
    private List<Profile> availableProfiles;
    private Profile selectedProfile;

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getClientToken() {
        return clientToken;
    }

    public List<Profile> getAvailableProfiles() {
        return availableProfiles;
    }

    public Profile getSelectedProfile() {
        return selectedProfile;
    }

    public static class User {
        private String username;
        private String id;

        public String getUsername() {
            return username;
        }

        public String getId() {
            return id;
        }
    }

    public static class Profile {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
}
