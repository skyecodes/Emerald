package com.github.franckyi.emerald.service.init;

import com.github.franckyi.emerald.model.Context;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.util.EmeraldUtils;
import com.github.franckyi.emerald.util.PreferenceManager;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ContextLoader implements Callable<Context> {
    private final File folder;

    public ContextLoader() {
        folder = PreferenceManager.getApplicationPath();
    }

    @Override
    public Context call() throws Exception {
        Context context = new Context();
        context.setInstances(this.getInstances());
        context.setLauncherInitialized(this.isLauncherInitialized());
        return context;
    }

    private boolean isLauncherInitialized() {
        return false;
    }

    private List<Instance> getInstances() throws IOException {
        File instanceFolder = new File(folder, "instances/");
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
        } else {
            instanceFolder.mkdir();
        }
        return instances;
    }
}
