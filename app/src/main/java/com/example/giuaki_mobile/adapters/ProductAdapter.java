package com.example.giuaki_mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.giuaki_mobile.R;
import com.example.giuaki_mobile.api.models.Product;
import android.widget.Toast;
// 19110167-Lê Trần Gia Bảo

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private boolean isLoading = false;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    // Thêm danh sách sản phẩm mới
    public void addProducts(List<Product> newProducts) {
        int startPosition = productList.size();
        productList.addAll(newProducts);
        notifyItemRangeInserted(startPosition, newProducts.size());
    }

    // Xóa tất cả sản phẩm
    public void clearProducts() {
        productList.clear();
        notifyDataSetChanged();
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }
    public boolean isLoading() {
        return isLoading;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_placeholder, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.format("%,.0f VNĐ", product.getPrice())); // Định dạng giá


        /*
        Glide.with(context)
             .load(product.getThumbnail())
             .placeholder(R.drawable.ic_product_placeholder)
             .into(holder.ivProductImage);
        */

        // Hiện tại chỉ dùng placeholder image
        // holder.ivProductImage.setImageResource(R.drawable.ic_product_placeholder);

        holder.itemView.setOnClickListener(v -> {
            // click vào sản phẩm
            Toast.makeText(context, "Clicked: " + product.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvProductPrice;
        ImageView ivProductImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
        }
    }
}