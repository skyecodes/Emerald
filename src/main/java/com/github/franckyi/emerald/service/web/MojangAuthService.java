package com.github.franckyi.emerald.service.web;

import com.github.franckyi.emerald.service.web.resource.mojang.auth.AuthRequest;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MojangAuthService {
    @POST("authenticate")
    Call<AuthResponse> authenticate(@Body AuthRequest authRequest);
}
