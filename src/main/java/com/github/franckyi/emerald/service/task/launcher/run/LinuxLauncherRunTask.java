package com.github.franckyi.emerald.service.task.launcher.run;

import com.github.franckyi.emerald.model.Instance;

public class LinuxLauncherRunTask extends LauncherRunTask {
    public LinuxLauncherRunTask(Instance instance) {
        super(instance);
    }

    @Override
    protected void runLauncher() throws Exception {
        Process process = new ProcessBuilder("./minecraft-launcher", "-w", path.resolve("instances").resolve(instance.getName()).resolve(".minecraft").toString())
                .directory(path.resolve("minecraft-launcher").toFile())
                .inheritIO()
                .start();
        process.waitFor();
    }
}
