package com.github.franckyi.emerald.service.task.instance;

import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.web.resource.mojang.VersionManifest;

public class VanillaInstanceCreatorTask extends InstanceCreatorTask {
    private final String name;
    private final VersionManifest.Version version;

    public VanillaInstanceCreatorTask(String name, VersionManifest.Version version) {
        this.name = name;
        this.version = version;
        this.updateTitle(String.format("Creating new Vanilla instance \"%s\" for version %s", name, version.getId()));
    }

    @Override
    protected Instance call() throws Exception {
        this.updateMessage("Test");
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(10);
            this.updateProgress(i + 1, 1000);
        }
        return null;
    }
}
