package com.seanatives.SurfCoursePlanner.services;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GuestService {

    @Autowired
    private GuestRepository guestRepository;

    public List<Guest> findAll() {
        Iterable<Guest> iterable = guestRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Guest> findGuestsInRange(LocalDate startDate, LocalDate endDate) {
        Date start = convertToDate(startDate);
        Date end = convertToDate(endDate);
        return findAll().stream()
                .filter(guest -> {
                    Date checkIn = guest.getBooking().getCheckInAt();
                    Date checkOut = guest.getBooking().getCheckOutAt();
                    return !checkIn.after(end) && !checkOut.before(start);
                })
                .collect(Collectors.toList());
    }

    private Date convertToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }
}
