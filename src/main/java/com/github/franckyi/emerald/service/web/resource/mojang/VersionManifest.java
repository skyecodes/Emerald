package com.github.franckyi.emerald.service.web.resource.mojang;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

public class VersionManifest {
    private Latest latest;
    private List<Version> versions;

    public Latest getLatest() {
        return latest;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public static class Latest {
        private String release;
        private String snapshot;

        public String getRelease() {
            return release;
        }

        public String getSnapshot() {
            return snapshot;
        }
    }

    public static class Version {
        private String id;
        private Type type;
        private String url;
        private Instant time;
        private Instant releaseTime;

        public String getId() {
            return id;
        }

        public Type getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        public Instant getTime() {
            return time;
        }

        public Instant getReleaseTime() {
            return releaseTime;
        }

        public enum Type {
            @SerializedName("release") RELEASE,
            @SerializedName("snapshot") SNAPSHOT,
            @SerializedName("old_beta") BETA,
            @SerializedName("old_alpha") ALPHA
        }
    }
}
