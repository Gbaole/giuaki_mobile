package com.example.giuaki_mobile.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

//19110167-Lê Trần Gia Bảo

public class ProductPagingResponse {


    @SerializedName("data")
    private List<Product> products;


    private Meta meta;

    public List<Product> getProducts() {
        return products;
    }


    public int getTotalPages() {
        return meta != null ? meta.totalPages : 0;
    }

    public int getCurrentPage() {
        return meta != null ? meta.page : 1;
    }


    private static class Meta {
        private int page;
        private int limit;
        private int total;
        private int totalPages;
    }

}