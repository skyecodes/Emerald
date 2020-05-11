package com.github.franckyi.emerald.service.web.resource.twitch;

import java.time.Instant;

public class MinecraftVersion {
    private int id;
    private int gameVersionId;
    private String versionString;
    private String jarDownloadUrl;
    private String jsonDownloadUrl;
    private boolean approved;
    private Instant dateModified;
    private int gameVersionTypeId;
    private int gameVersionStatus;
    private int gameVersionTypeStatus;

    public int getId() {
        return id;
    }

    public int getGameVersionId() {
        return gameVersionId;
    }

    public String getVersionString() {
        return versionString;
    }

    public String getJarDownloadUrl() {
        return jarDownloadUrl;
    }

    public String getJsonDownloadUrl() {
        return jsonDownloadUrl;
    }

    public boolean isApproved() {
        return approved;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public int getGameVersionTypeId() {
        return gameVersionTypeId;
    }

    public int getGameVersionStatus() {
        return gameVersionStatus;
    }

    public int getGameVersionTypeStatus() {
        return gameVersionTypeStatus;
    }
}
