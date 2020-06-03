package com.github.franckyi.emerald.util;

import org.apache.commons.compress.utils.IOUtils;
import org.tinylog.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Cache {
    public static final String AVATARS = "avatars";

    public static void put(String category, String name, InputStream is) {
        try (OutputStream os = Files.newOutputStream(Files.createDirectories(
                Emerald.getApplicationPath().resolve("cache").resolve(category)).resolve(name))) {
            IOUtils.copy(is, os);
        } catch (IOException e) {
            Logger.error(e, "Could not save data to cache");
        }
    }

    public static InputStream get(String category, String name) {
        Path path = Emerald.getApplicationPath().resolve("cache").resolve(category).resolve(name);
        if (Files.isRegularFile(path)) {
            try {
                return Files.newInputStream(path);
            } catch (IOException e) {
                Logger.error(e, "Could not load data from cache");
            }
        }
        return null;
    }

    public static InputStream getOrDefault(String category, String name, String url) {
        InputStream is = get(category, name);
        if (is != null) return is;
        try (InputStream is1 = new URL(url).openStream()) {
            put(category, name, is1);
            return get(category, name);
        } catch (IOException e) {
            Logger.error(e, "Could not load data from URL");
        }
        return null;
    }

}
