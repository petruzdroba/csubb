package com.domain;

import java.time.LocalDateTime;

public class ProfilePicture {

    private final long userId;
    private byte[] image;
    private String contentType;
    private LocalDateTime uploadedAt;

    public ProfilePicture(long userId, byte[] image, String contentType, LocalDateTime uploadedAt) {
        this.userId = userId;
        this.image = image;
        this.contentType = contentType;
        this.uploadedAt = uploadedAt;
    }

    public long getUserId() {
        return userId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
