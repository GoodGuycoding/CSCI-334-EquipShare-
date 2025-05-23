package com.equipshare.repository;

import com.equipshare.model.Item;
import com.equipshare.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    List<Item>findByTitle(String title);
    List<Item>findByOwner(User owner);
    List<Item>findByOwnerAndIsAvailable(User owner, boolean isAvailable);
    // 🔍 Search by keyword in title or description
    @Query("SELECT i FROM Item i WHERE LOWER(i.title) LIKE %:keyword% OR LOWER(i.description) LIKE %:keyword%")
    Page<Item> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 🔃 Filters
    Page<Item> findAllByOrderByPricePerDayAsc(Pageable pageable);
    Page<Item> findAllByOrderByLocationAsc(Pageable pageable);
}

