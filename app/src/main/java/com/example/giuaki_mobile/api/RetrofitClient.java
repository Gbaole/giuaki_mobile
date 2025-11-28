package com.example.giuaki_mobile.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//19110242-Lê Bá Minh
//19110167-Lê Trần Gia Bảo

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static AuthService authService;
    private static ApiService apiService;

    // Hàm khởi tạo Retrofit
    private static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            //  log request/response
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // AuthService
    public static AuthService getAuthService() {
        if (authService == null) {
            // Đảm bảo BASE_URL được lấy từ AuthService (hoặc ApiService nếu chúng dùng chung)
            authService = getClient(AuthService.BASE_URL).create(AuthService.class);
        }
        return authService;
    }

    //  ApiService
    public static ApiService getApiService() {
        if (apiService == null) {
            // Giả định ApiService và AuthService dùng chung Base URL
            apiService = getClient(ApiService.BASE_URL).create(ApiService.class);
        }
        return apiService;
    }
}