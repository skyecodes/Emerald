package com.github.franckyi.emerald.service.task.launcher.setup;

import com.github.franckyi.emerald.service.task.base.DownloadFileTask;
import com.github.franckyi.emerald.util.PathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WindowsLauncherSetupTask extends LauncherSetupTask {
    @Override
    protected Void call() throws Exception {
        this.downloadLauncher();
        return null;
    }

    private void downloadLauncher() throws IOException {
        this.updateMessage("Downloading launcher...");
        Path path = Files.createDirectories(PathUtils.getLauncherPath()).resolve("Minecraft.exe");
        DownloadFileTask task = new DownloadFileTask("https://launcher.mojang.com/download/Minecraft.exe", path);
        task.progressProperty().addListener((obs, oldVal, newVal) -> this.updateProgress(newVal.doubleValue(), 1));
        task.run();
    }
}
