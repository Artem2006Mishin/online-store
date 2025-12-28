package com.example.backend.dto;

public class NewsDto {
    private String title;
    private String text;
    private String imageURL;

    public NewsDto() {}

    public NewsDto(String title, String text, String imageURL) {
        this.title = title;
        this.text = text;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}