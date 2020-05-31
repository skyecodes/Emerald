package com.github.franckyi.emerald.service.init;

import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.util.Emerald;
import com.github.franckyi.emerald.util.PreferenceManager;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContextLoader {
    public static List<Instance> loadInstances() {
        List<Instance> instances = new ArrayList<>();
        try {
            File instanceFolder = new File(PreferenceManager.getApplicationPath(), "instances/");
            if (instanceFolder.isDirectory()) {
                File[] instanceDirs = instanceFolder.listFiles(File::isDirectory);
                if (instanceDirs != null) {
                    for (File instanceDir : instanceDirs) {
                        File instanceJsonFile = new File(instanceDir, "instance.json");
                        if (instanceJsonFile.isFile()) {
                            FileReader reader = new FileReader(instanceJsonFile);
                            Instance instance = Emerald.getGson().fromJson(reader, Instance.class);
                            reader.close();
                            Logger.debug("Instance {} found", instance.getName());
                            instances.add(instance);
                        } else {
                            Logger.error("No instance.json found in folder {}", instanceDir.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (IOException e) {
            Logger.error(e, "Unable to load instances");
        }
        return instances;
    }

    public static boolean isLauncherInitialized() {
        File launcherFolder = new File(PreferenceManager.getApplicationPath(), "launcher/");
        return launcherFolder.isDirectory();
    }
}
