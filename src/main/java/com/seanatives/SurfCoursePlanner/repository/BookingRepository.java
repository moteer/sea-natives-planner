package com.seanatives.SurfCoursePlanner.repository;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    Optional<Booking> findByBookingId(String bookingId);
}
