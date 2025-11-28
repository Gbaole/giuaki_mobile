package com.example.giuaki_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Màn hình giới thiệu (Intro).
 * Luôn chuyển đến LoginActivity khi nhấn nút.
 */
public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button startButton = findViewById(R.id.btn_start);

        // 🟢 ĐÃ SỬA: Loại bỏ logic kiểm tra đăng nhập.
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Luôn chuyển thẳng đến LoginActivity
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));

                finish(); // Đóng IntroActivity
            }
        });
    }
}