package com.codearena.parksmart.Model;

public class Users
{
    private String name, mobile_no, email_id, password, image;

    public Users() {
    }

    public Users(String name, String mobile_no, String email_id, String password, String image) {
        this.name = name;
        this.mobile_no = mobile_no;
        this.email_id = email_id;
        this.password = password;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
