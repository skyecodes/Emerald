package com.github.franckyi.emerald.util;

import com.sun.javafx.PlatformUtil;
import org.tinylog.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

    public static void openBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            Emerald.getExecutorService().submit(() -> {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException e) {
                    Logger.error(e, "Error while opening web browser for URL {}", url);
                }
            });
        } else {
            Logger.error("Unable to open the web browser for URL {}", url);
        }
    }

}
