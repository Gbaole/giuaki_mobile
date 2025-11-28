package com.example.giuaki_mobile;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import com.example.giuaki_mobile.api.AuthRequest;
import com.example.giuaki_mobile.api.AuthService;
import com.example.giuaki_mobile.api.BaseResponse;
import com.example.giuaki_mobile.api.RetrofitClient;
import com.example.giuaki_mobile.api.VerifyData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout registerFormLayout;
    private LinearLayout otpVerifyLayout;

    // Fields cho Register
    private TextInputEditText etRegisterUsername, etRegisterEmail, etRegisterPassword;
    // Fields cho Verify
    private TextInputEditText etOtp;

    private String currentUsername;

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authService = RetrofitClient.getAuthService();
        //19110242-Lê Bá Minh

        registerFormLayout = findViewById(R.id.layout_register_form);
        otpVerifyLayout = findViewById(R.id.layout_otp_verify);
        Button registerButton = findViewById(R.id.btn_register);
        Button verifyOtpButton = findViewById(R.id.btn_verify_otp);

        // Ánh xạ các EditText cho Register
        etRegisterUsername = findViewById(R.id.et_register_username);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);

        // Ánh xạ EditText cho OTP
        etOtp = findViewById(R.id.et_otp_input);

        //hiển thị form đăng ký
        registerFormLayout.setVisibility(View.VISIBLE);
        otpVerifyLayout.setVisibility(View.GONE);

        // gọi API Register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        // gọi API Verify OTP
        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleVerifyOtp();
            }
        });
    }

    private void handleRegister() {
        final String username = etRegisterUsername.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUsername = username;
        // Constructor Register
        AuthRequest registerRequest = new AuthRequest(username, email, password);

        authService.register(registerRequest).enqueue(new Callback<BaseResponse<Object>>() {
            @Override
            public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công. Vui lòng kiểm tra console BE để lấy OTP.", Toast.LENGTH_LONG).show();
                    // Chuyển sang màn hình xác thực OTP
                    registerFormLayout.setVisibility(View.GONE);
                    otpVerifyLayout.setVisibility(View.VISIBLE);
                } else {
                    String errorMsg = "Đăng ký thất bại.";

                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleVerifyOtp() {
        String otp = etOtp.getText().toString().trim();

        if (currentUsername == null || currentUsername.isEmpty() || otp.isEmpty()) {
            Toast.makeText(this, "Lỗi: Vui lòng nhập OTP và đảm bảo đã đăng ký.", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthRequest verifyRequest = new AuthRequest();
        verifyRequest.setUsername(currentUsername);
        verifyRequest.setOtp(otp);

        authService.verifyOtp(verifyRequest).enqueue(new Callback<BaseResponse<VerifyData>>() {
            @Override
            public void onResponse(Call<BaseResponse<VerifyData>> call, Response<BaseResponse<VerifyData>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(RegisterActivity.this, "Xác thực OTP thành công. Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
                    // Chuyển về LoginActivity
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Xác thực OTP thất bại. Vui lòng kiểm tra lại mã hoặc đã hết hạn.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<VerifyData>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}