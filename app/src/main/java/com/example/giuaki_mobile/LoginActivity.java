package com.example.giuaki_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import com.example.giuaki_mobile.api.AuthRequest;
import com.example.giuaki_mobile.api.AuthService;
import com.example.giuaki_mobile.api.LoginResponse;
import com.example.giuaki_mobile.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_AUTH_TOKEN = "authToken";

    private TextInputEditText etUsername, etPassword;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authService = RetrofitClient.getAuthService();

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        Button loginButton = findViewById(R.id.btn_login);
        TextView registerLink = findViewById(R.id.tv_register_link);

        // Xử lý sự kiện Đăng nhập
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Chuyển đến RegisterActivity
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin đăng nhập.", Toast.LENGTH_SHORT).show();
            return;
        }

        // SỬA LỖI CONSTRUCTOR: Dùng constructor mặc định và Setters
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        authService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Đăng nhập thành công
                    String token = response.body().getData().getToken();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Lưu trạng thái đăng nhập và token
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(KEY_IS_LOGGED_IN, true);
                    editor.putString(KEY_AUTH_TOKEN, token);
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    // Xử lý lỗi từ server (sai user/pass, chưa active, ...)
                    String errorMsg = "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin hoặc tài khoản chưa kích hoạt.";
                    // Có thể thêm logic parse response.errorBody() để lấy thông báo lỗi cụ thể
                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}