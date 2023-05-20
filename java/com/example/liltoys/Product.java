package com.example.liltoys;


import android.net.Uri;

import java.util.Date;

public class Product {
    private String userid;
    private String productid;
    private String name;
    private String category;
    private String description;
    private double price;
    private String location;
    private String date;
    private boolean mSelected;

    public Product()
    {

    }

    public Product(String userid, String productid, String name, String category, String description, double price, String location, String date) {
        this.userid = userid;
        this.productid = productid;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
    }
}
