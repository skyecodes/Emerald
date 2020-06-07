package com.github.franckyi.emerald;

import com.github.franckyi.emerald.data.Configuration;
import com.github.franckyi.emerald.data.User;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.util.ConfigManager;
import com.github.franckyi.emerald.util.PreferenceManager;
import com.github.franckyi.emerald.util.adapter.InstantTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.tinylog.Logger;

import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Emerald {
    public static void main(String[] args) {
        Logger.info("Hello world!");
        EmeraldApp.launch(EmeraldApp.class, args);
    }

    private static volatile Gson gson;
    private static volatile ExecutorService executorService;
    private static volatile Configuration configuration;
    private static volatile ObjectProperty<User> user;
    private static volatile ObservableList<Instance> instances;
    private static volatile Path applicationPath;

    public static Gson getGson() {
        if (gson == null) {
            synchronized (Emerald.class) {
                if (gson == null) {
                    Logger.debug("Loading Gson");
                    gson = new GsonBuilder()
                            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                            .create();
                }
            }
        }
        return gson;
    }

    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (Emerald.class) {
                if (executorService == null) {
                    Logger.debug("Loading executor service");
                    executorService = Executors.newCachedThreadPool();
                }
            }
        }
        return executorService;
    }

    public static Configuration getConfiguration() {
        if (configuration == null) {
            synchronized (Emerald.class) {
                if (configuration == null) {
                    configuration = ConfigManager.load();
                }
            }
        }
        return configuration;
    }

    public static ObjectProperty<User> getUser() {
        if (user == null) {
            synchronized (Emerald.class) {
                if (user == null) {
                    user = new SimpleObjectProperty<>();
                }
            }
        }
        return user;
    }

    public static ObservableList<Instance> getInstances() {
        if (instances == null) {
            synchronized (Emerald.class) {
                if (user == null) {
                    instances = FXCollections.observableArrayList();
                }
            }
        }
        return instances;
    }

    public static Path getApplicationPath() {
        if (applicationPath == null) {
            synchronized (Emerald.class) {
                if (user == null) {
                    applicationPath = PreferenceManager.loadApplicationPath();
                }
            }
        }
        return applicationPath;
    }
}
