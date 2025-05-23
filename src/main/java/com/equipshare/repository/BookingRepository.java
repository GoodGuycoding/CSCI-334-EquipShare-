package com.equipshare.repository;
import com.equipshare.model.Booking;
import com.equipshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByBorrower(User borrower);
    List<Booking> findByBorrowerAndStatus(User borrower, String status);
    List<Booking> findByStatus(String status);

    List<Booking> findByItemOwner(User user);
}