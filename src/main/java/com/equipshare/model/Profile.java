package com.equipshare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Profile {

    private Float UserRating;
    private ArrayList<TransactionHistory> rentals;
    @Id
    private Long id;

    public Profile() {}

    <Public> Public Profile() {
        this.UserRating = 0.0f;
        this.rentals = new ArrayList<>();
        this.id = null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}