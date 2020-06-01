package com.github.franckyi.emerald.service.task.instance;

import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.storage.InstanceStorage;
import com.github.franckyi.emerald.service.web.resource.mojang.VersionManifest;
import org.tinylog.Logger;

import java.time.Instant;

public class VanillaInstanceCreatorTask extends InstanceCreatorTask {
    private final VersionManifest.Version version;

    public VanillaInstanceCreatorTask(String displayName, VersionManifest.Version version) {
        super(displayName);
        this.version = version;
        this.updateTitle(String.format("Creating new Vanilla instance \"%s\" for version %s", this.getDisplayName(), version.getId()));
    }

    @Override
    protected Instance call() throws Exception {
        this.updateMessage("Creating environment");
        Instance instance = new Instance();
        instance.setName(this.getName());
        instance.setDisplayName(this.getDisplayName());
        instance.setCreationDate(Instant.now());
        instance.setMinecraftVersion(version.getId());
        InstanceStorage.createInstance(instance);
        Logger.info("Instance \"{}\" created", this.getDisplayName());
        return instance;
    }
}
