package com.github.franckyi.emerald.service.task.launcher.setup;

import com.github.franckyi.emerald.service.task.EmeraldTask;
import com.github.franckyi.emerald.util.SystemUtils;

public abstract class LauncherSetupTask extends EmeraldTask<Void> {

    protected LauncherSetupTask() {
        this.updateTitle("Setting up launcher");
    }

    public static LauncherSetupTask create() {
        switch (SystemUtils.getOS()) {
            case WINDOWS:
                return new WindowsLauncherSetupTask();
            case MAC:
                return new MacLauncherSetupTask();
            case LINUX:
            default:
                return new LinuxLauncherSetupTask();
        }
    }

}
