package com.seanatives.SurfCoursePlanner.controller;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.services.BookingService;
import com.seanatives.SurfCoursePlanner.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private GuestService guestService;

    @GetMapping("/debug")
    public String debug() {
        return "debug";
    }

    @GetMapping("/bookings")
    public String getBookings(Model model,
                              @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                              @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Booking> bookings;
        if (startDate != null && endDate != null) {
            bookings = bookingService.findBookingsInRange(startDate, endDate);
        } else {
            bookings = bookingService.findAll();
        }
        model.addAttribute("bookings", bookings);
        return "bookings";
    }

    @GetMapping("/guests")
    public String getGuests(Model model,
                            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Guest> guests;
        if (startDate != null && endDate != null) {
            guests = guestService.findGuestsInRange(startDate, endDate);
        } else {
            guests = guestService.findAll();
        }
        model.addAttribute("guests", guests);
        return "guests";
    }

    @GetMapping("/yoga")
    public String getGuestsWithYogaBooked(Model model,
                            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Guest> guests;
        if (startDate != null && endDate != null) {
            guests = guestService.findGuestsInRangeWithYoga(startDate, endDate);
        } else {
            guests = guestService.findAllGuestsWithYoga();
        }
        model.addAttribute("guests", guests);
        return "yoga";
    }
}
