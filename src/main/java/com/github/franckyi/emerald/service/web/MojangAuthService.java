package com.github.franckyi.emerald.service.web;

import com.github.franckyi.emerald.service.web.resource.mojang.auth.authenticate.AuthenticateRequest;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.authenticate.AuthenticateResponse;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.invalidate.InvalidateRequest;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.refresh.RefreshRequest;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.refresh.RefreshResponse;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.signout.SignoutRequest;
import com.github.franckyi.emerald.service.web.resource.mojang.auth.validate.ValidateRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MojangAuthService {
    @POST("authenticate")
    Call<AuthenticateResponse> authenticate(@Body AuthenticateRequest request);

    @POST("refresh")
    Call<RefreshResponse> refresh(@Body RefreshRequest request);

    @POST("validate")
    Call<Void> validate(@Body ValidateRequest request);

    @POST("signout")
    Call<Void> signout(@Body SignoutRequest request);

    @POST("invalidate")
    Call<Void> invalidate(@Body InvalidateRequest request);
}
