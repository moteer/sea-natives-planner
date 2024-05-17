package com.seanatives.SurfCoursePlanner.services;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    public List<Booking> saveOrUpdateBookings(List<Booking> bookings) {
        List<Booking> savedBookings = new ArrayList<>();
        bookings.forEach(booking -> {
            Booking savedBooking = saveOrUpdateBooking(booking);
            savedBookings.add(savedBooking);
        });
        return savedBookings;
    }

    @Transactional
    public Booking saveOrUpdateBooking(Booking booking) {
        Optional<Booking> existingBookingOpt = bookingRepository.findByBookingId(booking.getBookingId());
        if (existingBookingOpt.isPresent()) {
            Booking existingBooking = existingBookingOpt.get();
            existingBooking.setCreated(booking.getCreated());
            existingBooking.setChannel(booking.getChannel());
            existingBooking.setBooking(booking.getBooking());
            existingBooking.setStatus(booking.getStatus());
            existingBooking.setCheckInAt(booking.getCheckInAt());
            existingBooking.setCheckOutAt(booking.getCheckOutAt());
            existingBooking.setNights(booking.getNights());
            existingBooking.setComments(booking.getComments());
            existingBooking.setLocation(booking.getLocation());
            existingBooking.setBookerFirstName(booking.getBookerFirstName());
            existingBooking.setBookerLastName(booking.getBookerLastName());
            existingBooking.setBookerGender(booking.getBookerGender());
            existingBooking.setEmail(booking.getEmail());
            existingBooking.setPhone(booking.getPhone());
            existingBooking.setAddress1(booking.getAddress1());
            existingBooking.setAddress2(booking.getAddress2());
            existingBooking.setAddress3(booking.getAddress3());
            existingBooking.setZipCode(booking.getZipCode());
            existingBooking.setCity(booking.getCity());
            existingBooking.setState(booking.getState());
            existingBooking.setCountry(booking.getCountry());
            existingBooking.setCurrency(booking.getCurrency());
            existingBooking.setSubtotal(booking.getSubtotal());
            existingBooking.setTotal(booking.getTotal());
            existingBooking.setDiscount(booking.getDiscount());
            existingBooking.setDue(booking.getDue());
            existingBooking.setPartnerCommission(booking.getPartnerCommission());
            existingBooking.setPartnerCommissionPercent(booking.getPartnerCommissionPercent());
            existingBooking.setPax(booking.getPax());
            existingBooking.setCouples(booking.getCouples());

            // Gäste aktualisieren
            existingBooking.getGuests().clear();
            if (booking.getGuests() != null) {
                booking.getGuests().forEach(guest -> guest.setBooking(existingBooking));
                existingBooking.getGuests().addAll(booking.getGuests());
            }

            return bookingRepository.save(existingBooking);
        } else {
            // Neue Buchung
            if (booking.getGuests() != null) {
                booking.getGuests().forEach(guest -> guest.setBooking(booking));
            }
            return bookingRepository.save(booking);
        }
    }
}
