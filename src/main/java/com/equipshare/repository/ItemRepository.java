package com.equipshare.repository;

import com.equipshare.model.Item;
import com.equipshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    List<Item>findByTitle(String title);
    List<Item>findByOwner(User owner);
    List<Item>findByOwnerAndIsAvailable(User owner, boolean isAvailable);
}
