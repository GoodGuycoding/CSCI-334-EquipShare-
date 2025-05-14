package com.equipshare.repository;

import com.equipshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProfileRepository extends JpaRepository<User, String> {

}