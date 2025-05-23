package com.equipshare.repository;

import com.equipshare.model.Rating;
import com.equipshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
    List<Rating> findByBorrower(User borrower);
    List<Rating> findByItemOwner(User owner);
}