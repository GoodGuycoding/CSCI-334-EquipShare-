package com.equipshare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Item {
    private String ownerID;
    private String title;
    private String description;
    private String category;
    private float rentalfee;
    private Boolean avalability;

    @Id
    private String itemId;

    public Item() {}

    public Item(String title, String description, String category, String itemId, float rentalfee, Boolean avalability) {
        this.itemId = itemId;
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.rentalfee = rentalfee;
        this.avalability = avalability;
    }

}