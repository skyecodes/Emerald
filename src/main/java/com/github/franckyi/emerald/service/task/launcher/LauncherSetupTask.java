package com.github.franckyi.emerald.service.task.launcher;

import com.github.franckyi.emerald.service.task.EmeraldTask;
import com.github.franckyi.emerald.util.SystemUtils;

public abstract class LauncherSetupTask extends EmeraldTask<Void> {

    public static LauncherSetupTask create() {
        switch (SystemUtils.getOS()) {
            case WINDOWS:
                return new WindowsLauncherSetupTask();
            case LINUX:
                return new LinuxLauncherSetupTask();
            case MAC:
                return new MacLauncherSetupTask();
        }
        return null;
    }

}
