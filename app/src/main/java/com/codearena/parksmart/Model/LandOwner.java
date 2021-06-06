package com.codearena.parksmart.Model;

public class LandOwner
{
    private String Adhar_no, name, password, mobile_no, address, email_id, land_length, land_breadth, image_url;

    public LandOwner(){}

    public LandOwner(String adhar_no, String name, String password, String mobile_no, String address, String email_id, String land_length, String land_breadth, String image_url) {
        Adhar_no = adhar_no;
        this.name = name;
        this.password = password;
        this.mobile_no = mobile_no;
        this.address = address;
        this.email_id = email_id;
        this.land_length = land_length;
        this.land_breadth = land_breadth;
        this.image_url = image_url;
    }

    public String getAdhar_no() {
        return Adhar_no;
    }

    public void setAdhar_no(String adhar_no) {
        Adhar_no = adhar_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getLand_length() {
        return land_length;
    }

    public void setLand_length(String land_length) {
        this.land_length = land_length;
    }

    public String getLand_breadth() {
        return land_breadth;
    }

    public void setLand_breadth(String land_breadth) {
        this.land_breadth = land_breadth;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
