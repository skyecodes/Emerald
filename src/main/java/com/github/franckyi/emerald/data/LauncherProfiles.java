package com.github.franckyi.emerald.data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class LauncherProfiles {
    private Map<String, Authentication> authenticationDatabase;
    private String clientToken;
    private LauncherVersion launcherVersion;
    private Map<String, Profile> profiles;
    private SelectedUser selectedUser;
    private Settings settings;

    public Map<String, Authentication> getAuthenticationDatabase() {
        return authenticationDatabase;
    }

    public void setAuthenticationDatabase(Map<String, Authentication> authenticationDatabase) {
        this.authenticationDatabase = authenticationDatabase;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public LauncherVersion getLauncherVersion() {
        return launcherVersion;
    }

    public void setLauncherVersion(LauncherVersion launcherVersion) {
        this.launcherVersion = launcherVersion;
    }

    public Map<String, Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, Profile> profiles) {
        this.profiles = profiles;
    }

    public SelectedUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(SelectedUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public static class Authentication {
        private String accessToken;
        private Map<String, UserProfile> profiles;
        private List<String> properties;
        private String username;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Map<String, UserProfile> getProfiles() {
            return profiles;
        }

        public void setProfiles(Map<String, UserProfile> profiles) {
            this.profiles = profiles;
        }

        public List<String> getProperties() {
            return properties;
        }

        public void setProperties(List<String> properties) {
            this.properties = properties;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public static class UserProfile {
            private String displayName;

            public String getDisplayName() {
                return displayName;
            }

            public void setDisplayName(String displayName) {
                this.displayName = displayName;
            }
        }
    }

    public static class LauncherVersion {
        private int format;
        private String name;
        private int profilesFormat;

        public int getFormat() {
            return format;
        }

        public void setFormat(int format) {
            this.format = format;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProfilesFormat() {
            return profilesFormat;
        }

        public void setProfilesFormat(int profilesFormat) {
            this.profilesFormat = profilesFormat;
        }
    }

    public static class Profile {
        private Instant created;
        private String icon;
        private Instant lastUsed;
        private String lastVersionId;
        private String name;
        private String type;

        public Instant getCreated() {
            return created;
        }

        public void setCreated(Instant created) {
            this.created = created;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Instant getLastUsed() {
            return lastUsed;
        }

        public void setLastUsed(Instant lastUsed) {
            this.lastUsed = lastUsed;
        }

        public String getLastVersionId() {
            return lastVersionId;
        }

        public void setLastVersionId(String lastVersionId) {
            this.lastVersionId = lastVersionId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class SelectedUser {
        private String account;
        private String profile;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }
    }

    public static class Settings {
        private String channel;
        private boolean crashAssistance;
        private boolean enableAdvanced;
        private boolean enableAnalytics;
        private boolean enableHistorical;
        private boolean enableReleases;
        private boolean enableSnapshots;
        private boolean keepLauncherOpen;
        private String locale;
        private String profileSorting;
        private boolean showGameLog;
        private boolean showMenu;
        private boolean soundOn;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public boolean isCrashAssistance() {
            return crashAssistance;
        }

        public void setCrashAssistance(boolean crashAssistance) {
            this.crashAssistance = crashAssistance;
        }

        public boolean isEnableAdvanced() {
            return enableAdvanced;
        }

        public void setEnableAdvanced(boolean enableAdvanced) {
            this.enableAdvanced = enableAdvanced;
        }

        public boolean isEnableAnalytics() {
            return enableAnalytics;
        }

        public void setEnableAnalytics(boolean enableAnalytics) {
            this.enableAnalytics = enableAnalytics;
        }

        public boolean isEnableHistorical() {
            return enableHistorical;
        }

        public void setEnableHistorical(boolean enableHistorical) {
            this.enableHistorical = enableHistorical;
        }

        public boolean isEnableReleases() {
            return enableReleases;
        }

        public void setEnableReleases(boolean enableReleases) {
            this.enableReleases = enableReleases;
        }

        public boolean isEnableSnapshots() {
            return enableSnapshots;
        }

        public void setEnableSnapshots(boolean enableSnapshots) {
            this.enableSnapshots = enableSnapshots;
        }

        public boolean isKeepLauncherOpen() {
            return keepLauncherOpen;
        }

        public void setKeepLauncherOpen(boolean keepLauncherOpen) {
            this.keepLauncherOpen = keepLauncherOpen;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getProfileSorting() {
            return profileSorting;
        }

        public void setProfileSorting(String profileSorting) {
            this.profileSorting = profileSorting;
        }

        public boolean isShowGameLog() {
            return showGameLog;
        }

        public void setShowGameLog(boolean showGameLog) {
            this.showGameLog = showGameLog;
        }

        public boolean isShowMenu() {
            return showMenu;
        }

        public void setShowMenu(boolean showMenu) {
            this.showMenu = showMenu;
        }

        public boolean isSoundOn() {
            return soundOn;
        }

        public void setSoundOn(boolean soundOn) {
            this.soundOn = soundOn;
        }
    }
}
