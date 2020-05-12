package com.github.franckyi.emerald.util;

import com.sun.javafx.PlatformUtil;

public final class SystemUtils {

    private static OperatingSystem os;

    public static OperatingSystem getOS() {
        if (os == null) {
            if (PlatformUtil.isWindows()) {
                os = OperatingSystem.WINDOWS;
            } else if (PlatformUtil.isMac()) {
                os = OperatingSystem.MAC;
            } else {
                os = OperatingSystem.LINUX;
            }
        }
        return os;
    }

    public enum OperatingSystem {
        WINDOWS, LINUX, MAC
    }

}
