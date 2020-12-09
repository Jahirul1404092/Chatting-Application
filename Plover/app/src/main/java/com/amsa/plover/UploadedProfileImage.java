package com.amsa.plover;

public class UploadedProfileImage {     ///////////////ei class ekhono use kora hoyni
    private String imageName;
    private String imageUrl;
    public UploadedProfileImage(){

    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UploadedProfileImage(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }
}
