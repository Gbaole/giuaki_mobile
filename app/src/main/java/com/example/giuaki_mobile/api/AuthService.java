package com.example.giuaki_mobile.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthService {
    // 19110242-Lê Bá Minh
    String BASE_URL = "http://10.0.2.2:8000/";

    @POST("api/v1/auth/register")
    Call<BaseResponse<Object>> register(@Body AuthRequest request);

    @POST("api/v1/auth/verify-otp")
    Call<BaseResponse<VerifyData>> verifyOtp(@Body AuthRequest request);

    @POST("api/v1/auth/login")
    Call<LoginResponse> login(@Body AuthRequest request);

    @GET("api/v1/auth/profile")
    Call<BaseResponse<ProfileData>> getProfile(@Header("Authorization") String authToken);
}