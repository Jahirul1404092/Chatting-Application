package com.amsa.plover;

public class Profile {
    private String Id;
    private String Name;
    private String Password;
    private String Phone;
    private String Gender;
    private String ImageName;
    private String ImageDownloadUrl;

    public Profile(){

    }

    public Profile(String id, String name, String password, String phone, String gender, String imageName, String imageDownloadUrl) {
        Id = id;
        Name = name;
        Password = password;
        Phone = phone;
        Gender = gender;
        ImageName = imageName;
        ImageDownloadUrl = imageDownloadUrl;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getImageDownloadUrl() {
        return ImageDownloadUrl;
    }

    public void setImageDownloadUrl(String imageDownloadUrl) {
        ImageDownloadUrl = imageDownloadUrl;
    }
}
