package com.github.franckyi.emerald.data;

public class User {
    private String userName;
    private String displayName;
    private String userId;
    private String profileId;
    private String accessToken;
    private String clientToken;

    public User() {
    }

    public User(String userName, String displayName, String userId, String profileId, String accessToken, String clientToken) {
        this.userName = userName;
        this.displayName = displayName;
        this.userId = userId;
        this.profileId = profileId;
        this.accessToken = accessToken;
        this.clientToken = clientToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }
}
