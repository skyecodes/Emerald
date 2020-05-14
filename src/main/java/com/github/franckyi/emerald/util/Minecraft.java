package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.service.web.resource.mojang.VersionManifest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public final class Minecraft {
    private static VersionManifest versionManifest;

    public static VersionManifest getVersionManifest() throws IOException {
        if (versionManifest == null) {
            versionManifest = EmeraldUtils.getGson().fromJson(new InputStreamReader(
                            new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json").openStream()),
                    VersionManifest.class);
        }
        return versionManifest;
    }

}
