package com.example.giuaki_mobile.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthService {
    // THAY ĐỔI URL BE MẶC ĐỊNH CỦA BẠN TẠI ĐÂY
    String BASE_URL = "http://10.0.2.2:8000/api/v1/"; // Dùng 10.0.2.2 cho Android Emulator

    @POST("api/auth/register")
    Call<BaseResponse<Object>> register(@Body AuthRequest request);

    @POST("api/auth/verify-otp")
    Call<BaseResponse<VerifyData>> verifyOtp(@Body AuthRequest request);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body AuthRequest request);

    @GET("api/auth/profile")
    Call<BaseResponse<Object>> getProfile(@Header("Authorization") String authToken);
}