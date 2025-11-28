package com.example.giuaki_mobile.api;

import com.example.giuaki_mobile.api.models.Category;
import com.example.giuaki_mobile.api.models.ProductPagingResponse;
import com.example.giuaki_mobile.api.models.Product;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//19110167-Lê Trần Gia Bảo
public interface ApiService {
    String BASE_URL = "http://10.0.2.2:8000/";

    // Category API (Endpoint: GET /api/categories)
    @GET("api/v1/categories")
    Call<BaseResponse<List<Category>>> getAllCategories();

    // Product API (Endpoint: GET /api/products/category/:categoryId?...)
    @GET("api/v1/products/category/{categoryId}")
    Call<BaseResponse<ProductPagingResponse>> getProductsByCategory(
            @Path("categoryId") String categoryId,
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortBy") String sortBy,
            @Query("order") String order
    );
}