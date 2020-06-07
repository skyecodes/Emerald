package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.EmeraldApp;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

public final class PreferenceManager {
    private static final String APPLICATION_PATH_KEY = "APPLICATION_PATH";
    private static Preferences preferences;

    public static Path loadApplicationPath() {
        String path = getPreferences().get(APPLICATION_PATH_KEY, null);
        if (path != null) {
            Path applicationPath = Paths.get(path);
            if (!Files.isDirectory(applicationPath)) {
                try {
                    Files.createDirectory(applicationPath);
                } catch (IOException e) {
                    Logger.error(e, "Unable to create application directory");
                    return null;
                }
            }
            return applicationPath;
        }
        return null;
    }

    public static void setApplicationPath(Path newPath) {
        getPreferences().put(APPLICATION_PATH_KEY, newPath.toString());
    }

    private static Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(EmeraldApp.class);
        }
        return preferences;
    }

}
