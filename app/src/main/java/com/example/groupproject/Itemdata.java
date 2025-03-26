package com.example.groupproject;

public class Itemdata {
    public String name, price, image;

    public Itemdata() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setPic(String pic) {
        this.image = image;
    }

    public Itemdata(String name, String price, String pic) {
        this.name = name;
        this.image = image;
        this.price = price;
    }
}