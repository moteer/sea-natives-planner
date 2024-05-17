package com.seanatives.SurfCoursePlanner.repository;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends CrudRepository<Guest, Long> {
    Optional<Guest> findByName(String name);
}
