package com.github.franckyi.emerald.service.task.base;

import javafx.concurrent.Task;

import java.io.File;

public class LaunchMinecraftTask extends Task<Void> {
    private final File launcherDirectory;
    private final File instanceDirectory;

    public LaunchMinecraftTask(File launcherDirectory, File instanceDirectory) {
        this.launcherDirectory = launcherDirectory;
        this.instanceDirectory = instanceDirectory;
    }

    @Override
    protected Void call() throws Exception {
        Process process = new ProcessBuilder("./minecraft-launcher", "-w", instanceDirectory.getPath())
                .directory(launcherDirectory)
                .inheritIO()
                .start();
        process.waitFor();
        return null;
    }
}
