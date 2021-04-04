package com.example.conelshop;

public class Product {

    private int mProduct_id;
    private String mName, mDescription, mPhoto;
    private double mPrice;

    public Product(int productID, String name, String description, String photo, double price) {
        mProduct_id = productID;
        mName = name;
        mDescription = description;
        mPhoto = photo;
        mPrice = price;
    }

    public int getProductID() {
        return mProduct_id;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public double getPrice() {
        return mPrice;
    }
}
