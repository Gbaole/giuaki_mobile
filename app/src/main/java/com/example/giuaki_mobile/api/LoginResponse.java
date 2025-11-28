package com.example.giuaki_mobile.api;
//19110242-Lê Bá Minh
public class LoginResponse {
    private boolean success;
    private LoginData data;
    private String message;

    public boolean isSuccess() { return success; }
    public LoginData getData() { return data; }

    public static class LoginData {
        private String token;
        // private User user; // Có thể thêm User object

        public String getToken() { return token; }
    }
}