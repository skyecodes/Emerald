package com.github.franckyi.emerald.util;

import java.nio.file.Path;

public final class PathUtils {
    public static Path getCachePath() {
        return Emerald.getApplicationPath().resolve("cache");
    }

    public static Path getInstancesPath() {
        return Emerald.getApplicationPath().resolve("instances");
    }

    public static Path getLauncherPath() {
        return Emerald.getApplicationPath().resolve("minecraft-launcher");
    }

    public static Path getInstancePath(String instanceName) {
        return getInstancesPath().resolve(instanceName);
    }

    public static Path getInstanceMinecraftPath(String instanceName) {
        return getInstanceMinecraftPath(getInstancePath(instanceName));
    }

    public static Path getInstanceMinecraftPath(Path instancePath) {
        return instancePath.resolve(".minecraft");
    }

    public static Path getInstanceVersionsPath(String instanceName) {
        return getInstanceVersionsPath(getInstanceMinecraftPath(instanceName));
    }

    public static Path getInstanceVersionsPath(Path instanceMinecraftPath) {
        return instanceMinecraftPath.resolve("versions");
    }
}
