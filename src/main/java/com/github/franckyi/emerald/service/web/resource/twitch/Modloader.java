package com.github.franckyi.emerald.service.web.resource.twitch;

import java.time.Instant;

public class Modloader {
    private String name;
    private String gameVersion;
    private boolean latest;
    private boolean recommended;
    private Instant dateModified;

    public String getName() {
        return name;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public boolean isLatest() {
        return latest;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public Instant getDateModified() {
        return dateModified;
    }
}
