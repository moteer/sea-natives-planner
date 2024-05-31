package com.seanatives.SurfCoursePlanner.repository;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    Optional<Booking> findByBookingId(String bookingId);

    @Query("SELECT b FROM Booking b WHERE b.checkInAt BETWEEN :startDate AND :endDate OR b.checkOutAt BETWEEN :startDate AND :endDate ")
    List<Booking> findAllGuestsBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
