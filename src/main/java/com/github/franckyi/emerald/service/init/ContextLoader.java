package com.github.franckyi.emerald.service.init;

import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.util.EmeraldUtils;
import com.github.franckyi.emerald.util.PreferenceManager;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContextLoader {
    public static List<Instance> loadInstances() throws IOException {
        File instanceFolder = new File(PreferenceManager.getApplicationPath(), "instances/");
        List<Instance> instances = new ArrayList<>();
        if (instanceFolder.isDirectory()) {
            File[] instanceDirs = instanceFolder.listFiles(File::isDirectory);
            if (instanceDirs != null) {
                for (File instanceDir : instanceDirs) {
                    File instanceJsonFile = new File(instanceDir, "instance.json");
                    if (instanceJsonFile.isFile()) {
                        FileReader reader = new FileReader(instanceJsonFile);
                        Instance instance = EmeraldUtils.getGson().fromJson(reader, Instance.class);
                        reader.close();
                        Logger.debug("Instance {} found", instance.getName());
                        instances.add(instance);
                    } else {
                        Logger.error("No instance.json found in folder {}", instanceDir.getAbsolutePath());
                    }
                }
            }
        }
        return instances;
    }

    public static boolean isLauncherInitialized() {
        File launcherFolder = new File(PreferenceManager.getApplicationPath(), "launcher/");
        return launcherFolder.isDirectory();
    }
}
