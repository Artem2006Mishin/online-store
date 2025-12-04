package com.example.backend.dto;

public class AuthResponceDto {

    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;
    private String profileImage;
    private Integer visitCount;

    public AuthResponceDto(String email, String role, String accessToken, String refreshToken, String profileImage, Integer visitCount) {
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.profileImage = profileImage;
        this.visitCount = visitCount;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public Integer getVisitCount() {
        return visitCount;
    }
}
