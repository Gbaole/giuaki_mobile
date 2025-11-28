package com.example.giuaki_mobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;




import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Màn hình Chính (Home).
 * Chứa phần thông tin người dùng, danh mục ngang, danh sách sản phẩm dạng lưới và Bottom Navigation.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private RecyclerView productRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần giao diện (để chuẩn bị cho logic sau này)
        categoryRecyclerView = findViewById(R.id.rv_categories);
        productRecyclerView = findViewById(R.id.rv_products);

        // Thiết lập sự kiện cho Bottom Navigation (Dùng ID từ XML layout)
        findViewById(R.id.homeBtn).setOnClickListener(v -> Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show());
        // Các nút khác trong Bottom Bar (Cần thêm ID cho các LinearLayout khác trong XML)
        // Hiện tại chỉ có homeBtn có ID. Bạn có thể thêm ID cho các nút khác như profileBtn, supportBtn, settingsBtn.
    }
}