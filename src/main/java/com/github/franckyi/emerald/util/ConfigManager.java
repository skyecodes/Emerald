package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.data.Configuration;
import com.google.gson.Gson;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class ConfigManager {

    private static final String FILE_NAME = "config.json";

    public static Configuration createDefaultConfig() {
        Configuration c = new Configuration();
        c.setVersion(1);
        c.setTheme(Configuration.Theme.DARK);
        c.setCustomTheme("");
        return c;
    }

    public static Configuration load() throws IOException {
        Logger.debug("Loading configuration");
        Gson gson = EmeraldUtils.getGson();
        File path = PreferenceManager.getApplicationPath();
        File configFile = new File(path, FILE_NAME);
        Configuration configuration;
        if (configFile.isFile()) {
            FileReader reader = new FileReader(configFile);
            configuration = gson.fromJson(reader, Configuration.class);
            reader.close();
        } else {
            Logger.debug("Creating default configuration file");
            configuration = createDefaultConfig();
            save(configuration);
        }
        return configuration;
    }

    public static void save(Configuration config) throws IOException {
        Logger.debug("Saving configuration");
        Gson gson = EmeraldUtils.getGson();
        File path = PreferenceManager.getApplicationPath();
        File configFile = new File(path, FILE_NAME);
        FileWriter writer = new FileWriter(configFile);
        gson.toJson(config, writer);
        writer.close();
    }

}
