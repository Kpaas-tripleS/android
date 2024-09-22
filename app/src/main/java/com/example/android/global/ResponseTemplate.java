package com.example.android.global;

import com.google.gson.annotations.SerializedName;

public class ResponseTemplate<T> {
    @SerializedName("isSuccess")
    private Boolean isSuccess;

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("results")
    private T results;

    // 기본 생성자
    public ResponseTemplate() {
    }

    // 모든 필드를 초기화하는 생성자
    public ResponseTemplate(Boolean isSuccess, String code, String message, T results) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.results = results;
    }

    // Getter 및 Setter 메서드
    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
