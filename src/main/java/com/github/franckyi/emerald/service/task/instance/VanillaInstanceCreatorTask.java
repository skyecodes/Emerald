package com.github.franckyi.emerald.service.task.instance;

import com.github.franckyi.emerald.service.web.resource.mojang.VersionManifest;

public class VanillaInstanceCreatorTask extends InstanceCreatorTask {
    private final String text;
    private final VersionManifest.Version version;

    public VanillaInstanceCreatorTask(String text, VersionManifest.Version version) {
        this.text = text;
        this.version = version;
    }

    @Override
    protected Void call() throws Exception {
        return null;
    }
}
