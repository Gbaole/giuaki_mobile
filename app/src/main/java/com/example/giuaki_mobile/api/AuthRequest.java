package com.example.giuaki_mobile.api;

public class AuthRequest {
    private String username;
    private String email;
    private String password;
    private String otp;

    // Constructor mặc định (cần thiết nếu bạn muốn dùng setter)
    public AuthRequest() {}

    // Constructor cho Register (3 tham số - unique signature)
    public AuthRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Constructor cho Login (2 tham số - ĐÃ BỊ LỖI, CẦN LOẠI BỎ)
    // Constructor cho Verify OTP (2 tham số - ĐÃ BỊ LỖI, CẦN LOẠI BỎ)

    // Getters và Setters (Dùng Setter cho Login/Verify để tránh lỗi chữ ký)

    public String getUsername() { return username; }
    public String getOtp() { return otp; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setOtp(String otp) { this.otp = otp; }
}