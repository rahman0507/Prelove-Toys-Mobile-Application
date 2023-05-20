package com.example.liltoys;

public class Payment {
    private String name, address, paymentmethod, addedby, userid;

    public Payment()
    {

    }

    public Payment(String name, String address, String paymentmethod, String addedby, String userid) {
        this.name = name;
        this.address = address;
        this.paymentmethod = paymentmethod;
        this.addedby = addedby;
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getAddedby() {
        return addedby;
    }

    public void setAddedby(String addedby) {
        this.addedby = addedby;
    }

    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
}
