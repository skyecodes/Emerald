package com.github.franckyi.emerald.service.task.launcher.run;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.data.LauncherProfiles;
import com.github.franckyi.emerald.data.User;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.task.EmeraldTask;
import com.github.franckyi.emerald.service.web.MojangAuthException;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.refresh.RefreshRequest;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.refresh.RefreshResponse;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.validate.ValidateRequest;
import com.github.franckyi.emerald.util.*;
import com.google.gson.Gson;
import javafx.application.Platform;
import org.tinylog.Logger;
import retrofit2.Call;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;

public abstract class LauncherRunTask extends EmeraldTask<Void> {
    protected final Instance instance;

    protected LauncherRunTask(Instance instance) {
        this.instance = instance;
    }

    @Override
    protected final Void call() throws Exception {
        if (this.refreshProfile()) {
            this.updateLauncherProfileData();
            this.runLauncher();
        }
        return null;
    }

    protected abstract void runLauncher() throws Exception;

    private boolean refreshProfile() throws IOException {
        User user = Emerald.getUser().get();
        Call<Void> call = WebServiceManager.getMojangAuthService().validate(new ValidateRequest(user.getAccessToken(), user.getClientToken()));
        try {
            Response<Void> response = call.execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (MojangAuthException e) {
            Logger.info(e.getMessage() + " - refreshing user");
            Call<RefreshResponse> call1 = WebServiceManager.getMojangAuthService().refresh(new RefreshRequest(user.getAccessToken(), user.getClientToken(), false));
            try {
                Response<RefreshResponse> response1 = call1.execute();
                if (response1.isSuccessful()) {
                    RefreshResponse body = response1.body();
                    user.setAccessToken(body.getAccessToken());
                    user.setClientToken(body.getClientToken());
                    UserManager.save();
                    return true;
                }
            } catch (MojangAuthException e1) {
                Logger.warn(e.getMessage() + " - asking for credentials");
                Platform.runLater(() -> EmeraldApp.getInstance().getMainController().getMenuController().showLogin(user0 -> Minecraft.launch(instance), e.getMessage()));
            }
        }
        return false;
    }

    private void updateLauncherProfileData() throws IOException {
        Gson gson = Emerald.getGson();
        User user = Emerald.getUser().get();
        Path launcherProfilesFile = PathUtils.getInstanceMinecraftPath(instance.getName()).resolve("launcher_profiles.json");

        BufferedReader reader = Files.newBufferedReader(launcherProfilesFile);
        LauncherProfiles profiles = gson.fromJson(reader, LauncherProfiles.class);
        reader.close();

        LauncherProfiles.Authentication.UserProfile profile = new LauncherProfiles.Authentication.UserProfile();
        profile.setDisplayName(user.getDisplayName());
        LauncherProfiles.Authentication auth = new LauncherProfiles.Authentication();
        auth.setAccessToken(user.getAccessToken());
        if (auth.getProfiles() == null) {
            auth.setProfiles(new HashMap<>());
        }
        auth.getProfiles().put(user.getProfileId(), profile);
        auth.setUsername(user.getUserName());
        if (profiles.getAuthenticationDatabase() == null) {
            profiles.setAuthenticationDatabase(new HashMap<>());
        }
        if (auth.getProperties() == null) {
            auth.setProperties(Collections.emptyList());
        }
        profiles.getAuthenticationDatabase().put(user.getUserId(), auth);
        if (profiles.getSelectedUser() == null) {
            profiles.setSelectedUser(new LauncherProfiles.SelectedUser());
        }
        profiles.getSelectedUser().setAccount(user.getUserId());
        profiles.getSelectedUser().setProfile(user.getProfileId());
        profiles.setClientToken(user.getClientToken());

        BufferedWriter writer = Files.newBufferedWriter(launcherProfilesFile);
        gson.toJson(profiles, writer);
        writer.close();
    }

    public static LauncherRunTask create(Instance instance) {
        switch (SystemUtils.getOS()) {
            case WINDOWS:
                return new WindowsLauncherRunTask(instance);
            case MAC:
                return new MacLauncherRunTask(instance);
            case LINUX:
            default:
                return new LinuxLauncherRunTask(instance);
        }
    }

}
