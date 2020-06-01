package com.github.franckyi.emerald.service.storage;

import com.github.franckyi.emerald.util.PreferenceManager;

import java.nio.file.Files;

public final class LauncherStorage {
    public static boolean isLauncherInitialized() {
        return Files.isDirectory(PreferenceManager.getApplicationPath().resolve("launcher"));
    }
}
