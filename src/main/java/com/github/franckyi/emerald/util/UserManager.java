package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.data.User;
import com.google.gson.Gson;
import org.tinylog.Logger;

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

}
