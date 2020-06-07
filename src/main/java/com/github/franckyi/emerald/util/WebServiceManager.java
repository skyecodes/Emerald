package com.github.franckyi.emerald.util;

import com.github.franckyi.emerald.Emerald;
import com.github.franckyi.emerald.service.web.MojangAuthException;
import com.github.franckyi.emerald.service.web.MojangAuthService;
import com.github.franckyi.emerald.service.web.TwitchAppService;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.AuthError;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class WebServiceManager {
    private static final String TWITCH_APP_SERVICE_BASE_URL = "https://addons-ecs.forgesvc.net/api/v2/";
    private static final String MOJANG_AUTH_SERVICE_BASE_URL = "https://authserver.mojang.com";
    private static TwitchAppService twitchAppService;
    private static MojangAuthService mojangAuthService;

    public static TwitchAppService getTwitchAppService() {
        if (twitchAppService == null) {
            twitchAppService = new Retrofit.Builder()
                    .baseUrl(TWITCH_APP_SERVICE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(Emerald.getGson()))
                    .build()
                    .create(TwitchAppService.class);
        }
        return twitchAppService;
    }

    public static MojangAuthService getMojangAuthService() {
        if (mojangAuthService == null) {
            mojangAuthService = new Retrofit.Builder()
                    .baseUrl(MOJANG_AUTH_SERVICE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(Emerald.getGson()))
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(chain -> {
                                Request request = chain.request();
                                Response response = chain.proceed(request);
                                if (!response.isSuccessful()) {
                                    AuthError error = Emerald.getGson().fromJson(response.body().charStream(), AuthError.class);
                                    throw new MojangAuthException(error);
                                }
                                return response;
                            })
                            .build())
                    .build()
                    .create(MojangAuthService.class);
        }
        return mojangAuthService;
    }

}
