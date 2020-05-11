package com.github.franckyi.emerald.service.web;

import com.github.franckyi.emerald.service.web.resource.twitch.MinecraftVersion;
import com.github.franckyi.emerald.service.web.resource.twitch.Modloader;
import com.github.franckyi.emerald.service.web.resource.twitch.ModloaderInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface TwitchAppService {

    @GET("minecraft/modloader")
    Call<List<Modloader>> getModloaderList();

    @GET("minecraft/modloader/{versionName}")
    Call<ModloaderInfo> getModloaderInfo(@Path("versionName") String versionName);

    @GET("minecraft/version")
    Call<List<MinecraftVersion>> getMinecraftVersionList();

    @GET("minecraft/version/{versionString}")
    Call<MinecraftVersion> getMinecraftVersion(@Path("versionString") String versionString);

}
