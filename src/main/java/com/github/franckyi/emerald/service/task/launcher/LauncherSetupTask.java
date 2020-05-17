package com.github.franckyi.emerald.service.task.launcher;

import com.github.franckyi.emerald.util.SystemUtils;
import javafx.concurrent.Task;

public abstract class LauncherSetupTask extends Task<Void> {

    public static LauncherSetupTask create(SystemUtils.OperatingSystem os) {
        switch (os) {
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
