package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.EmeraldApp;

import java.io.File;
import java.util.prefs.Preferences;

public final class PreferenceManager {

    private static final String APPLICATION_PATH_KEY = "APPLICATION_PATH";

    private static Preferences preferences;
    private static File applicationPath;

    public static File getApplicationPath() {
        if (applicationPath == null) {
            String path = getPreferences().get(APPLICATION_PATH_KEY, null);
            if (path != null) {
                applicationPath = new File(path);
                if (!applicationPath.isDirectory()) {
                    applicationPath.mkdir();
                }
            }
        }
        return applicationPath;
    }

    public static void setApplicationPath(File newPath) {
        if (!newPath.equals(applicationPath)) {
            getPreferences().put(APPLICATION_PATH_KEY, newPath.getAbsolutePath());
            applicationPath = newPath;
        }
    }

    private static Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(EmeraldApp.class);
        }
        return preferences;
    }

}
