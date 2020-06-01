package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.storage.LauncherStorage;
import com.github.franckyi.emerald.service.task.launcher.LauncherSetupTask;
import com.github.franckyi.emerald.service.web.resource.mojang.VersionManifest;
import org.tinylog.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public final class Minecraft {
    private static VersionManifest versionManifest;

    public static VersionManifest getVersionManifest() throws IOException {
        if (versionManifest == null) {
            Logger.debug("Loading Minecraft version manifest");
            versionManifest = Emerald.getGson().fromJson(new InputStreamReader(
                            new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json").openStream()),
                    VersionManifest.class);
        }
        return versionManifest;
    }

    public static void launch(Instance instance) {
        if (!LauncherStorage.isLauncherInitialized()) {
            EmeraldApp.getInstance().getMainController().getMenuController().showTasks();
            EmeraldApp.getInstance().getMainController().getMenuController().getTaskListController().submit(LauncherSetupTask.create());
        }
    }

}
