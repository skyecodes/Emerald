package com.github.franckyi.emerald.service.web.resource.twitch;

import java.time.Instant;

public class ModloaderInfo {
    private int id;
    private int gameVersionId;
    private int minecraftGameVersionId;
    private String forgeVersion;
    private String downloadUrl;
    private String filename;
    private int installMethod;
    private boolean latest;
    private boolean recommended;
    private boolean approved;
    private Instant dateModified;
    private String mavenVersionString;
    private String versionJson;
    private String librariesInstallLocation;
    private String minecraftVersion;
    private String additionalFilesJson;
    private String modLoaderGameVersionId;
    private String modLoaderGameVersionTypeId;
    private String modLoaderGameVersionStatus;
    private String modLoaderGameVersionTypeStatus;
    private String mcGameVersionId;
    private String mcGameTypeId;
    private String mcGameVersionStatus;
    private String mcGameVersionTypeStatus;
    private String installProfileJson;

    public int getId() {
        return id;
    }

    public int getGameVersionId() {
        return gameVersionId;
    }

    public int getMinecraftGameVersionId() {
        return minecraftGameVersionId;
    }

    public String getForgeVersion() {
        return forgeVersion;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getFilename() {
        return filename;
    }

    public int getInstallMethod() {
        return installMethod;
    }

    public boolean isLatest() {
        return latest;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public boolean isApproved() {
        return approved;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public String getMavenVersionString() {
        return mavenVersionString;
    }

    public String getVersionJson() {
        return versionJson;
    }

    public String getLibrariesInstallLocation() {
        return librariesInstallLocation;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public String getAdditionalFilesJson() {
        return additionalFilesJson;
    }

    public String getModLoaderGameVersionId() {
        return modLoaderGameVersionId;
    }

    public String getModLoaderGameVersionTypeId() {
        return modLoaderGameVersionTypeId;
    }

    public String getModLoaderGameVersionStatus() {
        return modLoaderGameVersionStatus;
    }

    public String getModLoaderGameVersionTypeStatus() {
        return modLoaderGameVersionTypeStatus;
    }

    public String getMcGameVersionId() {
        return mcGameVersionId;
    }

    public String getMcGameTypeId() {
        return mcGameTypeId;
    }

    public String getMcGameVersionStatus() {
        return mcGameVersionStatus;
    }

    public String getMcGameVersionTypeStatus() {
        return mcGameVersionTypeStatus;
    }

    public String getInstallProfileJson() {
        return installProfileJson;
    }
}
