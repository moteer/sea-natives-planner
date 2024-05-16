package com.seanatives.SurfCoursePlanner.repository;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
}
