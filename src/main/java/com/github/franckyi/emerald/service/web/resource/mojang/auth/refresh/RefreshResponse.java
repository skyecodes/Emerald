package com.github.franckyi.emerald.service.web.resource.mojang.auth.refresh;

import java.util.List;

public class RefreshResponse {
    private String accessToken;
    private String clientToken;
    private SelectedProfile selectedProfile;
    private User user;

    public String getAccessToken() {
        return accessToken;
    }

    public String getClientToken() {
        return clientToken;
    }

    public SelectedProfile getSelectedProfile() {
        return selectedProfile;
    }

    public User getUser() {
        return user;
    }

    public static class SelectedProfile {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class User {
        private String id;
        private List<Property> properties;

        public String getId() {
            return id;
        }

        public List<Property> getProperties() {
            return properties;
        }

        private static class Property {
            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }
        }
    }
}
