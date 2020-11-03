package com.github.franckyi.emerald.service.task.launcher.run;

import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.util.PathUtils;

public class WindowsLauncherRunTask extends LauncherRunTask {
    public WindowsLauncherRunTask(Instance instance) {
        super(instance);
    }

    @Override
    protected void runLauncher() throws Exception {
        Process process = new ProcessBuilder(PathUtils.getLauncherPath().resolve("Minecraft.exe").toAbsolutePath().toString(),
                "--workDir", PathUtils.getInstanceMinecraftPath(instance.getId()).toString())
                .inheritIO()
                .start();
        process.waitFor();
    }
}
