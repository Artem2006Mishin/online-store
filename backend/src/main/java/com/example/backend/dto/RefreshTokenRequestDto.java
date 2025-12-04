package com.example.backend.dto;

public class RefreshTokenRequestDto {

    private String refreshToken;

    public RefreshTokenRequestDto() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
