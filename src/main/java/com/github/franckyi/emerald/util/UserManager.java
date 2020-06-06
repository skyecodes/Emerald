package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.data.User;
import com.github.franckyi.emerald.service.web.CallHandler;
import com.github.franckyi.emerald.service.web.MojangAuthException;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.invalidate.InvalidateRequest;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.refresh.RefreshRequest;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.refresh.RefreshResponse;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.validate.ValidateRequest;
import com.google.gson.Gson;
import org.tinylog.Logger;
import retrofit2.Call;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UserManager {
    private static final String FILE_NAME = "user.json";

    public static void load() {
        Gson gson = Emerald.getGson();
        Path userFile = Emerald.getApplicationPath().resolve(FILE_NAME);
        User user = null;
        if (Files.isRegularFile(userFile)) {
            Logger.debug("Loading user data");
            try (BufferedReader reader = Files.newBufferedReader(userFile)) {
                user = gson.fromJson(reader, User.class);
            } catch (IOException e) {
                Logger.error(e, "Unable to load user data - removing file");
                try {
                    Files.delete(userFile);
                } catch (IOException e1) {
                    Logger.error(e1, "Unable to remove user data file");
                }
            }
        }
        Emerald.getUser().set(user);
        if (UserManager.isUserLoggedIn()) {
            try {
                validate();
            } catch (MojangAuthException e) {
                Logger.warn(e.getMessage() + " - will ask for credentials when launching an instance");
            } catch (IOException e) {
                Logger.error(e, "Error while refreshing user token");
            }
        } else {
            Logger.debug("No logged in user found");
        }
    }

    public static void save() {
        User user = Emerald.getUser().get();
        Path userFile = Emerald.getApplicationPath().resolve(FILE_NAME);
        if (user != null) {
            Logger.debug("Saving user data");
            Gson gson = Emerald.getGson();
            try (BufferedWriter writer = Files.newBufferedWriter(userFile)) {
                gson.toJson(user, writer);
            } catch (IOException e) {
                Logger.error(e, "Unable to save user data file");
            }
        } else {
            Logger.debug("Deleting user data");
            try {
                Files.deleteIfExists(userFile);
            } catch (IOException e) {
                Logger.error(e, "Unable to delete user data file");
            }
        }
    }

    public static void validate() throws IOException {
        User user = Emerald.getUser().get();
        Call<Void> call = WebServiceManager.getMojangAuthService().validate(new ValidateRequest(user.getAccessToken(), user.getClientToken()));
        try {
            call.execute();
            Logger.debug("User \"{}\" is logged in", user.getUserName());
        } catch (MojangAuthException e) {
            Logger.info(e.getMessage() + " - refreshing user");
            Call<RefreshResponse> call1 = WebServiceManager.getMojangAuthService().refresh(new RefreshRequest(user.getAccessToken(), user.getClientToken(), false));
            Response<RefreshResponse> response1 = call1.execute();
            if (response1.isSuccessful()) {
                RefreshResponse body = response1.body();
                user.setAccessToken(body.getAccessToken());
                user.setClientToken(body.getClientToken());
                UserManager.save();
            }
        }
    }

    public static void invalidate() {
        User user = Emerald.getUser().get();
        CallHandler.builder(WebServiceManager.getMojangAuthService().invalidate(new InvalidateRequest(user.getAccessToken(), user.getClientToken())))
                .onResponse(v -> {
                    User newUser = new User();
                    newUser.setUserName(user.getUserName());
                    newUser.setClientToken(user.getClientToken());
                    Emerald.getUser().set(newUser);
                    UserManager.save();
                    Logger.info("Logout succeeded");
                })
                .onFailure(throwable -> Logger.error(throwable, "Logout failed"))
                .build().runAsync();
    }

    public static boolean isUserLoggedIn() {
        User user = Emerald.getUser().get();
        return user != null && user.getAccessToken() != null;
    }
}
