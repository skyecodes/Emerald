package com.github.franckyi.emerald.service.storage;

import com.github.franckyi.emerald.util.PathUtils;
import com.github.franckyi.emerald.util.SystemUtils;

import java.nio.file.Files;

public final class LauncherStorage {
    public static boolean isLauncherInitialized() {
        switch (SystemUtils.getOS()) {
            case WINDOWS:
                return Files.isRegularFile(PathUtils.getLauncherPath().resolve("Minecraft.exe"));
            case LINUX:
                return Files.isDirectory(PathUtils.getLauncherPath());
            case MAC:
                break;
        }
        return false;
    }
}
