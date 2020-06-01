package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.data.Configuration;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.util.adapter.InstantTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.tinylog.Logger;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Emerald {
    private static Gson gson;
    private static ExecutorService executorService;
    private static Configuration configuration;
    private static ObservableList<Instance> instances;

    public static Gson getGson() {
        if (gson == null) {
            Logger.debug("Loading Gson");
            gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                    .create();
        }
        return gson;
    }

    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            Logger.debug("Loading executor service");
            executorService = Executors.newCachedThreadPool();
        }
        return executorService;
    }

    public static Configuration getConfiguration() {
        if (configuration == null) {
            configuration = ConfigManager.load();
        }
        return configuration;
    }

    public static ObservableList<Instance> getInstances() {
        if (instances == null) {
            instances = FXCollections.observableArrayList();
        }
        return instances;
    }

}
