package com.github.franckyi.emerald.service.task.launcher.run;

import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.util.PathUtils;

public class LinuxLauncherRunTask extends LauncherRunTask {
    public LinuxLauncherRunTask(Instance instance) {
        super(instance);
    }

    @Override
    protected void runLauncher() throws Exception {
        Process process = new ProcessBuilder("./minecraft-launcher", "-w", PathUtils.getInstanceMinecraftPath(instance.getId()).toString())
                .directory(PathUtils.getLauncherPath().toFile())
                .inheritIO()
                .start();
        process.waitFor();
    }
}
