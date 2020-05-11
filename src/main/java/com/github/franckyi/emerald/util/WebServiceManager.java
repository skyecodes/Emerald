package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.service.web.MojangAuthService;
import com.github.franckyi.emerald.service.web.TwitchAppService;
import com.google.gson.Gson;
import org.tinylog.Logger;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class WebServiceManager {

    private static final String TWITCH_APP_SERVICE_BASE_URL = "https://addons-ecs.forgesvc.net/api/v2/";
    private static final String MOJANG_AUTH_SERVICE_BASE_URL = "https://authserver.mojang.com";
    private static TwitchAppService twitchAppService;
    private static MojangAuthService mojangAuthService;

    public static TwitchAppService getTwitchAppService() {
        return twitchAppService;
    }

    public static MojangAuthService getMojangAuthService() {
        return mojangAuthService;
    }

    public static void init(Gson gson) {
        Logger.debug("Initializing WebService clients");
        twitchAppService = buildWebService(gson, TWITCH_APP_SERVICE_BASE_URL, TwitchAppService.class);
        mojangAuthService = buildWebService(gson, MOJANG_AUTH_SERVICE_BASE_URL, MojangAuthService.class);
    }

    private static <T> T buildWebService(Gson gson, String baseUrl, Class<T> type) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(type);
    }

}
