package com.seanatives.SurfCoursePlanner.services;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> findAll() {
        Iterable<Booking> iterable = bookingRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Booking> findBookingsInRange(LocalDate startDate, LocalDate endDate) {
        return findAll().stream()
                .filter(booking -> {
                    LocalDate checkIn = convertToLocalDate(booking.getCheckInAt());
                    LocalDate checkOut = convertToLocalDate(booking.getCheckOutAt());
                    return !checkIn.isAfter(endDate) && !checkOut.isBefore(startDate);
                })
                .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
