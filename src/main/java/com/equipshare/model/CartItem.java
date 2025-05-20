package com.equipshare.model;

public class CartItem {

    private String itemId;
    private String title;
    private double pricePerDay;
    private int rentalDays;

    public double getTotalPrice() {
        return pricePerDay * rentalDays;
    }

    // Getters and setters

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }

    public int getRentalDays() { return rentalDays; }
    public void setRentalDays(int rentalDays) { this.rentalDays = rentalDays; }
}
