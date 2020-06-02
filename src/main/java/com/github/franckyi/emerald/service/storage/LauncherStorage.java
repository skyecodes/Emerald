package com.github.franckyi.emerald.service.storage;

import com.github.franckyi.emerald.util.Emerald;
import com.github.franckyi.emerald.util.SystemUtils;

import java.nio.file.Files;

public final class LauncherStorage {
    public static boolean isLauncherInitialized() {
        switch (SystemUtils.getOS()) {
            case WINDOWS:
                return Files.isRegularFile(Emerald.getApplicationPath().resolve("minecraft-launcher").resolve("Minecraft.exe"));
            case LINUX:
                return Files.isDirectory(Emerald.getApplicationPath().resolve("minecraft-launcher"));
            case MAC:
                break;
        }
        return false;
    }
}
