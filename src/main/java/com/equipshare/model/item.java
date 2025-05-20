package com.equipshare.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.sql.Timestamp;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "title")
    private String title;

    private String description;
    private String category;
    private double pricePerDay;
    private String location;


    @Column(name = "item_photo_url")
    private String itemPhotoUrl;

    @Column(name = "is_active")
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "created_at")
    private Timestamp createdAt;

    // Required no-arg constructor
    public Item() {
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getItemPhotoUrl() {
        return itemPhotoUrl;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setItemPhotoUrl(String itemPhotoUrl) {
        this.itemPhotoUrl = itemPhotoUrl;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}