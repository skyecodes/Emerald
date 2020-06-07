package com.github.franckyi.emerald.service.task.launcher.setup;

import com.github.franckyi.emerald.Emerald;
import com.github.franckyi.emerald.service.task.base.DownloadFileTask;
import com.github.franckyi.emerald.service.task.base.LinuxUnzipFileTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LinuxLauncherSetupTask extends LauncherSetupTask {
    private Path tmp;

    @Override
    protected Void call() throws Exception {
        this.downloadLauncher();
        this.unzipLauncher();
        Files.delete(tmp);
        return null;
    }

    private void downloadLauncher() throws IOException {
        this.updateMessage("Downloading launcher...");
        tmp = Files.createTempFile("emerald-launcher", null);
        DownloadFileTask task = new DownloadFileTask("https://launcher.mojang.com/download/Minecraft.tar.gz", tmp);
        task.progressProperty().addListener((obs, oldVal, newVal) -> this.updateProgress(newVal.doubleValue(), 2));
        task.run();
    }

    private void unzipLauncher() {
        this.updateMessage("Decompressing...");
        Path launcherPath = Emerald.getApplicationPath();
        LinuxUnzipFileTask task = new LinuxUnzipFileTask(tmp, launcherPath);
        task.progressProperty().addListener((obs, oldVal, newVal) -> this.updateProgress(newVal.doubleValue() + 1, 2));
        task.run();
    }
}
