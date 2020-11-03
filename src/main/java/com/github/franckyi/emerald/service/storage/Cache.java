package com.github.franckyi.emerald.service.storage;

import com.github.franckyi.emerald.util.PathUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.tinylog.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Cache {
    public static final String AVATARS = "avatars";

    public static void put(String category, String name, InputStream is) {
        try (OutputStream os = Files.newOutputStream(Files.createDirectories(
                PathUtils.getCachePath().resolve(category)).resolve(name))) {
            IOUtils.copy(is, os);
        } catch (IOException e) {
            Logger.error(e, "Could not save data to cache");
        }
    }

    public static InputStream get(String category, String name) {
        Path path = PathUtils.getCachePath().resolve(category).resolve(name);
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
        try {
            HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
            if (c.getResponseCode() >= 200 && c.getResponseCode() < 300) {
                is = c.getInputStream();
                put(category, name, is);
            } else {
                return c.getErrorStream();
            }
            return get(category, name);
        } catch (IOException e) {
            Logger.error(e, "Could not load data from URL");
        }
        return null;
    }

}
