package com.example.giuaki_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giuaki_mobile.adapters.CategoryAdapter;
import com.example.giuaki_mobile.adapters.ProductAdapter;
import com.example.giuaki_mobile.api.ApiService;
import com.example.giuaki_mobile.api.AuthService;
import com.example.giuaki_mobile.api.BaseResponse;
import com.example.giuaki_mobile.api.ProfileData;
import com.example.giuaki_mobile.api.RetrofitClient;
import com.example.giuaki_mobile.api.models.Category;
import com.example.giuaki_mobile.api.models.Product;
import com.example.giuaki_mobile.api.models.ProductPagingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

// Triển khai CategoryAdapter.OnCategoryClickListener để xử lý sự kiện click
public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    // Service & Constants
    private ApiService apiService;
    private AuthService authService;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_AUTH_TOKEN = "authToken";

    // UI Components
    private TextView tvUserName;
    private TextView tvProductsTitle;

    // Category Components
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();

    // Product Components (Lazy Loading)
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private String currentCategoryId = null; // ID của danh mục đang hiển thị sản phẩm
    private int currentPage = 1;
    private int totalPages = 1;
    private final int LIMIT = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Service
        authService = RetrofitClient.getAuthService();
        apiService = RetrofitClient.getApiService();

        // 1. Ánh xạ View
        tvUserName = findViewById(R.id.tv_user_name);
        tvProductsTitle = findViewById(R.id.tv_products_title);

        // 2. Thiết lập Category RecyclerView (Horizontal)
        categoryRecyclerView = findViewById(R.id.rv_categories);
        categoryAdapter = new CategoryAdapter(this, categoryList, this);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        // 3. Thiết lập Product RecyclerView (Grid)
        productRecyclerView = findViewById(R.id.rv_products);
        productAdapter = new ProductAdapter(this, productList);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productRecyclerView.setAdapter(productAdapter);

        // 4. Thiết lập Lazy Loading cho Product
        setupProductLazyLoading();

        // 5. Tải dữ liệu ban đầu
        loadUserProfile();
        loadAllCategories(); // Bắt đầu tải Category, Product sẽ tải sau đó

        // 6. Thiết lập sự kiện cho Bottom Navigation
        findViewById(R.id.homeBtn).setOnClickListener(v -> Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show());
        findViewById(R.id.profileBtn).setOnClickListener(v -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show());
        // Thêm các nút khác...
    }

    // --- Logic Tải Profile (Giữ nguyên) ---
    private void loadUserProfile() {
        // 1. Lấy Auth Token
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_AUTH_TOKEN, null);

        if (token == null) {
            tvUserName.setText("Khách (Chưa đăng nhập)");
            return;
        }

        String authTokenHeader = "Bearer " + token;

        // 2. Gọi API Profile
        authService.getProfile(authTokenHeader).enqueue(new Callback<BaseResponse<ProfileData>>() {
            @Override
            public void onResponse(Call<BaseResponse<ProfileData>> call, Response<BaseResponse<ProfileData>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    String username = response.body().getData().getUsername();
                    if (username != null) {
                        tvUserName.setText(username);
                    } else {
                        tvUserName.setText("Không tìm thấy tên người dùng");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Không thể tải Profile.", Toast.LENGTH_LONG).show();
                    tvUserName.setText("Lỗi tải thông tin");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ProfileData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối mạng Profile", Toast.LENGTH_LONG).show();
                tvUserName.setText("Lỗi mạng");
            }
        });
    }

    // --- Logic Tải Category (Mới) ---
    private void loadAllCategories() {
        apiService.getAllCategories().enqueue(new Callback<BaseResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Category>>> call, Response<BaseResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Category> newCategories = response.body().getData();
                    categoryList.clear();
                    categoryList.addAll(newCategories);
                    categoryAdapter.setCategories(newCategories);

                    // Tải sản phẩm của Category đầu tiên sau khi Categories được tải
                    if (!newCategories.isEmpty()) {
                        currentCategoryId = newCategories.get(0).get_id();
                        loadProducts(true);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi tải Danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Category>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối API Danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Logic Tải Product (Pagination & Sort - Mới) ---
    private void loadProducts(boolean isFirstLoad) {
        if (currentCategoryId == null) return;
        if (!isFirstLoad && currentPage > totalPages) return;

        // Nếu là lần tải đầu tiên (hoặc khi đổi Category)
        if (isFirstLoad) {
            currentPage = 1;
            productAdapter.clearProducts();
            productAdapter.setLoading(false);
            tvProductsTitle.setText("Sản Phẩm Nổi Bật (Đang tải...)");
        }

        productAdapter.setLoading(true);

        apiService.getProductsByCategory(
                currentCategoryId,
                currentPage,
                LIMIT,
                "price", // sắp xếp theo giá
                "asc"    // thứ tự tăng dần
        ).enqueue(new Callback<BaseResponse<ProductPagingResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ProductPagingResponse>> call, Response<BaseResponse<ProductPagingResponse>> response) {
                productAdapter.setLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ProductPagingResponse pagingResponse = response.body().getData();

                    if (pagingResponse != null && pagingResponse.getProducts() != null) {
                        productAdapter.addProducts(pagingResponse.getProducts());
                        currentPage++;
                        totalPages = pagingResponse.getTotalPages();
                        tvProductsTitle.setText("Sản Phẩm Nổi Bật (Page " + (currentPage - 1) + "/" + totalPages + ")");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi tải Sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ProductPagingResponse>> call, Throwable t) {
                productAdapter.setLoading(false);
                Toast.makeText(MainActivity.this, "Lỗi kết nối API Sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Triển khai Interface OnCategoryClickListener ---
    @Override
    public void onCategoryClick(String categoryId) {
        if (!categoryId.equals(currentCategoryId)) {
            currentCategoryId = categoryId;
            loadProducts(true); // Reset và tải lại sản phẩm khi chọn Category mới
        }
    }

    // --- Thiết lập Lazy Loading ---
    private void setupProductLazyLoading() {
        productRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                // Điều kiện Lazy Loading: Không đang tải, chưa hết trang, và gần cuối
                if (!productAdapter.isLoading() && currentPage <= totalPages) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2
                            && firstVisibleItemPosition >= 0) {

                        loadProducts(false); // Tải trang tiếp theo
                    }
                }
            }
        });
    }
}