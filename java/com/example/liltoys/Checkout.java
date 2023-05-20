package com.example.liltoys;

public class Checkout {
    private String name, address, paymentmethod, addedby, userid, paymentid, date;

    public Checkout()
    {

    }

    public Checkout(String name, String address, String paymentmethod, String addedby, String userid, String paymentid, String date) {
        this.name = name;
        this.address = address;
        this.paymentmethod = paymentmethod;
        this.addedby = addedby;// siapa yang checkout, sbb nnti dia akan dapat receipt // loop cart, ambik addedby compare dgn getcurrentuser
        this.userid = userid; // guna untuk panggil item dalam cart yang addded by the same person,
        this.paymentid = paymentid;
        this.date = date;
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

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}