package com.example.giuaki_mobile.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.giuaki_mobile.R;
import com.example.giuaki_mobile.api.models.Category;
import java.util.List;

//19110167-Lê Trần Gia Bảo

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private String selectedCategoryId = null;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryId);
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
        if (!categoryList.isEmpty()) {
            selectedCategoryId = categoryList.get(0).get_id(); // Chọn category đầu tiên
        }
    }

    // Cập nhật danh sách Category mới
    public void setCategories(List<Category> newCategories) {
        this.categoryList = newCategories;
        if (!newCategories.isEmpty() && selectedCategoryId == null) {
            selectedCategoryId = newCategories.get(0).get_id();
        }
        notifyDataSetChanged();
    }

    // Đặt lại Category đang chọn
    public void setSelectedCategory(String categoryId) {
        this.selectedCategoryId = categoryId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_placeholder, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());


        // Xử lý trạng thái Selected
        boolean isSelected = category.get_id().equals(selectedCategoryId);
        if (isSelected) {
            holder.tvCategoryName.setTextColor(context.getResources().getColor(R.color.purple_700));
            holder.ivCategoryIcon.setBackgroundColor(context.getResources().getColor(R.color.purple_100));
        } else {
            holder.tvCategoryName.setTextColor(Color.parseColor("#333333"));
            holder.ivCategoryIcon.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                setSelectedCategory(category.get_id());
                listener.onCategoryClick(category.get_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView ivCategoryIcon;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            ivCategoryIcon = itemView.findViewById(R.id.iv_category_icon);

        }
    }
}