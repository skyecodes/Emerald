package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.data.Configuration;
import com.github.franckyi.emerald.util.adapter.InstantTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class EmeraldUtils {
    private static Gson gson;
    private static ExecutorService executorService;
    private static Configuration configuration;

    public static Gson getGson() {
        return gson;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void init() throws IOException {
        Logger.debug("Initializing core services");
        gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();
        executorService = Executors.newCachedThreadPool();
        configuration = ConfigManager.load();
        WebServiceManager.init(gson);
    }

}
