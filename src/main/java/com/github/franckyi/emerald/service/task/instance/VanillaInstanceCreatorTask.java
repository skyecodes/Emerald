package com.github.franckyi.emerald.service.task.instance;

import com.github.franckyi.emerald.Emerald;
import com.github.franckyi.emerald.data.LauncherProfiles;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.storage.InstanceStorage;
import com.github.franckyi.emerald.service.task.base.DownloadFileTask;
import com.github.franckyi.emerald.service.web.resource.mojang.VersionManifest;
import com.github.franckyi.emerald.util.PathUtils;
import com.google.gson.Gson;
import org.tinylog.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class VanillaInstanceCreatorTask extends InstanceCreatorTask {
    private final VersionManifest.Version version;
    private final Instance instance;

    public VanillaInstanceCreatorTask(String displayName, VersionManifest.Version version) {
        super(displayName);
        this.version = version;
        this.instance = new Instance();
        this.updateTitle(String.format("Creating new Vanilla instance \"%s\" for version %s", this.getDisplayName(), version.getId()));
    }

    @Override
    protected Instance call() throws Exception {
        this.updateMessage("Creating environment");
        Path instanceDirectory = this.createEnvironment();
        this.updateProgress(1, 3);

        this.updateMessage("Downloading version file");
        Path minecraftDir = PathUtils.getInstanceMinecraftPath(instanceDirectory);
        Path versionDir = Files.createDirectories(PathUtils.getInstanceVersionsPath(minecraftDir).resolve(version.getId()));
        Path versionFile = versionDir.resolve(version.getId() + ".json");
        this.downloadVersionFile(versionFile);

        this.updateMessage("Setting up launcher files");
        this.setupLauncherProfile(minecraftDir);
        this.updateProgress(1, 1);

        Logger.info("Instance \"{}\" created", this.getDisplayName());
        return instance;
    }

    private Path createEnvironment() throws IOException {
        instance.setId(this.getName());
        instance.setName(this.getDisplayName());
        instance.setCreationDate(Instant.now());
        instance.setMinecraftVersion(version.getId());
        return InstanceStorage.createInstance(instance);
    }

    private void downloadVersionFile(Path versionFile) {
        DownloadFileTask task = new DownloadFileTask(version.getUrl(), versionFile);
        task.progressProperty().addListener((obs, oldVal, newVal) -> this.updateProgress(newVal.doubleValue() + 1, 3));
        task.run();
    }

    private void setupLauncherProfile(Path minecraftDir) throws IOException {
        Gson gson = Emerald.getGson();
        InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("/minecraft/launcher_profiles.json"));
        LauncherProfiles profiles = gson.fromJson(reader, LauncherProfiles.class);
        reader.close();
        LauncherProfiles.Profile profile = new LauncherProfiles.Profile();
        profile.setCreated(Instant.now());
        profile.setIcon("Grass");
        profile.setLastUsed(Instant.now());
        profile.setLastVersionId(version.getId());
        profile.setName(instance.getName());
        profile.setType("custom");
        profiles.getProfiles().put("00000000000000000000000000000000", profile);
        Path launcherProfilesFile = minecraftDir.resolve("launcher_profiles.json");
        BufferedWriter writer = Files.newBufferedWriter(launcherProfilesFile);
        gson.toJson(profiles, writer);
        writer.close();
    }
}
