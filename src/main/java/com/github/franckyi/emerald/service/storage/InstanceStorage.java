package com.github.franckyi.emerald.service.storage;

import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.util.Emerald;
import com.google.gson.Gson;
import javafx.application.Platform;
import org.tinylog.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public final class InstanceStorage {
    public static void reloadInstances() {
        Logger.debug("Reloading instances");
        List<Instance> instances = Emerald.getInstances();
        instances.clear();
        try {
            Path instancesDirectory = Emerald.getApplicationPath().resolve("instances/");
            if (Files.isDirectory(instancesDirectory)) {
                Files.list(instancesDirectory).filter(Files::isDirectory).forEach(instanceDirectory -> {
                    Path instanceJsonFile = instanceDirectory.resolve("instance.json");
                    if (Files.isRegularFile(instanceJsonFile)) {
                        try (BufferedReader reader = Files.newBufferedReader(instanceJsonFile)) {
                            Instance instance = Emerald.getGson().fromJson(reader, Instance.class);
                            Logger.debug("Instance {} found", instance.getDisplayName());
                            instances.add(instance);
                        } catch (IOException e) {
                            Logger.error(e, "Unable to load instances");
                        }
                    } else {
                        Logger.error("No instance.json found in directory {}", instanceDirectory.toString());
                    }
                });
            }
        } catch (IOException e) {
            Logger.error(e, "Unable to load instances");
        }
        instances.sort(Comparator.comparing(Instance::getCreationDate));
    }

    public static Path createInstance(Instance instance) throws IOException {
        Gson gson = Emerald.getGson();
        Path instanceDirectory = Emerald.getApplicationPath().resolve("instances").resolve(instance.getName());
        Files.createDirectories(instanceDirectory);
        Path instanceJsonFile = instanceDirectory.resolve("instance.json");
        BufferedWriter writer = Files.newBufferedWriter(instanceJsonFile);
        gson.toJson(instance, writer);
        writer.close();
        return instanceDirectory;
    }

    public static void deleteInstance(Instance instance) {
        try {
            Storage.deleteDirectory(Emerald.getApplicationPath().resolve("instances/").resolve(instance.getName()));
            Platform.runLater(() -> Emerald.getInstances().remove(instance));
            Logger.info("Instance \"{}\" deleted", instance.getDisplayName());
        } catch (IOException e) {
            Logger.error(e, "Unable to delete instance \"{}\"", instance.getDisplayName());
        }
    }
}
