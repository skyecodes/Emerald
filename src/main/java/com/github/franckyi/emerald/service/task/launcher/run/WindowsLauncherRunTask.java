package com.github.franckyi.emerald.service.task.launcher.run;

import com.github.franckyi.emerald.model.Instance;

public class WindowsLauncherRunTask extends LauncherRunTask {
    public WindowsLauncherRunTask(Instance instance) {
        super(instance);
    }

    @Override
    protected void runLauncher() throws Exception {
        Process process = new ProcessBuilder(path.resolve("minecraft-launcher").resolve("Minecraft.exe").toAbsolutePath().toString(),
                "--workDir", path.resolve("instances").resolve(instance.getName()).resolve(".minecraft").toString())
                .inheritIO()
                .start();
        process.waitFor();
    }
}
