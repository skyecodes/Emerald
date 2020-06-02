package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.data.Configuration;
import com.google.gson.Gson;
import org.tinylog.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final String FILE_NAME = "config.json";

    public static Configuration createDefaultConfig() {
        Configuration c = new Configuration();
        c.setVersion(1);
        c.setTheme(Configuration.Theme.DARK);
        return c;
    }

    public static Configuration load() {
        Logger.debug("Loading configuration");
        Gson gson = Emerald.getGson();
        Path configFile = Emerald.getApplicationPath().resolve(FILE_NAME);
        Configuration configuration;
        if (Files.isRegularFile(configFile)) {
            try (BufferedReader reader = Files.newBufferedReader(configFile)) {
                configuration = gson.fromJson(reader, Configuration.class);
            } catch (IOException e) {
                Logger.error(e, "Unable to load configuration file, using defaults");
                configuration = createDefaultConfig();
            }
        } else {
            Logger.info("Creating default configuration file");
            configuration = createDefaultConfig();
            save(configuration);
        }
        return configuration;
    }

    public static void save(Configuration config) {
        Logger.debug("Saving configuration");
        Gson gson = Emerald.getGson();
        Path configFile = Emerald.getApplicationPath().resolve(FILE_NAME);
        try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            Logger.error(e, "Unable to save configuration file");
        }
    }

}
