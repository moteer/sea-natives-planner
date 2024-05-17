package com.seanatives.SurfCoursePlanner.services;

import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.repository.GuestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    public List<Guest> saveOrUpdateGuest(List<Guest> guests) {
        List<Guest> savedGuests = new ArrayList<>();
        guests.forEach(guest -> {
            Guest savedGuest = saveOrUpdateGuest(guest);
            savedGuests.add(savedGuest);
        });
        return savedGuests;
    }

    @Transactional
    public Guest saveOrUpdateGuest(Guest guest) {
        Optional<Guest> existingGuestOpt = guestRepository.findByName(guest.getName());
        if (existingGuestOpt.isPresent()
                && guest.getBooking().getBookingId().equals(existingGuestOpt.get().getBooking().getBookingId())) {
            Guest existingGuest = existingGuestOpt.get();

            // Aktualisiere die notwendigen Felder des bestehenden Gastes
            if (guest.getAge()!= null) existingGuest.setAge(guest.getAge());
            if (guest.getBookingDetails()!= null) existingGuest.setBookingDetails(guest.getBookingDetails());
            existingGuest.setNumberOfSurfClassesBooked(guest.getNumberOfSurfClassesBooked());

            // Weitere Felder hier aktualisieren
            return guestRepository.save(existingGuest);
        } else {
            // Neuer Gast
            return guestRepository.save(guest);
        }
    }
}
