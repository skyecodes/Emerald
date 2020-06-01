package com.github.franckyi.emerald.service.task.launcher;

import com.github.franckyi.emerald.service.task.base.DownloadFileTask;

import java.nio.file.Files;
import java.nio.file.Path;

public class LinuxLauncherSetupTask extends LauncherSetupTask {
    @Override
    protected Void call() throws Exception {
        Path downloadPath = Files.createTempFile("launcher", null);
        DownloadFileTask task = new DownloadFileTask("https://launcher.mojang.com/download/Minecraft.tar.gz", downloadPath);
        task.progressProperty().addListener((obs, oldVal, newVal) -> this.updateProgress(newVal.doubleValue(), 2));
        task.run();
        if (task.isDone()) {

        }
        return null;
    }
}
