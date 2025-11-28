package com.example.giuaki_mobile.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

//19110167-Lê Trần Gia Bảo

public class Product {
    private String _id;
    private String title;

    private Category category;
    private double price;


    @SerializedName("images")
    private List<String> images;

    public String get_id() {
        return _id;
    }


    public String getName() {
        return title;
    }

    public double getPrice() {
        return price;
    }


    public String getThumbnail() {
        return images != null && !images.isEmpty() ? images.get(0) : null;
    }

    public Category getCategory() {
        return category;
    }
}