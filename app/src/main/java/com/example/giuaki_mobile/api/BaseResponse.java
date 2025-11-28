package com.example.giuaki_mobile.api;
//19110242-Lê Bá Minh
//19110167-Lê Trần Gia Bảo
public class BaseResponse<T> {
    private boolean success;
    private T data;
    private String message;

    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getMessage() { return message; }
}