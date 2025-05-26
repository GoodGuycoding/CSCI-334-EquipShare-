package com.equipshare.repository;
import com.equipshare.model.Booking;
import com.equipshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByBorrower(User borrower);
    List<Booking> findByBorrowerAndStatus(User borrower, String status);
    List<Booking> findByStatus(String status);

    List<Booking> findByItemOwner(User user);

    @Query("SELECT b FROM Booking b WHERE b.borrower.id = :userId AND b.item.id = :itemId")
    List<Booking> findAllByUserAndItem(@Param("userId") String userId, @Param("itemId") String itemId);

    // calculating bookings per day
    @Query(
            value = "SELECT DATE(start_date) AS bookingDate, COUNT(*) AS totalBookings " +
                    "FROM booking " +
                    "GROUP BY bookingDate " +
                    "ORDER BY bookingDate DESC",
            nativeQuery = true)
    List<Object[]> findTotalBookingsGroupedByDay();

}
