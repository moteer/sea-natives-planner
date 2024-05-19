package com.seanatives.SurfCoursePlanner.controller;

import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.domain.Participation;
import com.seanatives.SurfCoursePlanner.services.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/participation")
public class ParticipationRestController {

    @Autowired
    private ParticipationService participationService;

    @GetMapping("/weeklyAttendance")
    public List<GuestDto> getWeeklyAttendance(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        List<Guest> guests = participationService.getGuestsCheckedInBetween(
                Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        return guests.stream()
                .map(guest -> toGuestDto(guest))
                .collect(Collectors.toList());
    }

    private GuestDto toGuestDto(Guest guest) {
        GuestDto guestDto = new GuestDto();
        guestDto.name = guest.getName();
        guestDto.age = guest.getAge();
        guestDto.bookedYogaHours = guest.getBookedYogaHours();
        guestDto.bookingDetails = guest.getBookingDetails();
        guestDto.checkInAt = guest.getBooking().getCheckInAt();
        guestDto.checkOutAt = guest.getBooking().getCheckOutAt();
        guestDto.bookingId = guest.getBooking().getBookingId();
        guestDto.bookerFirstName = guest.getBooking().getBookerFirstName();
        guestDto.bookerLastName = guest.getBooking().getBookerLastName();
        List<Participation> participations = guest.getParticipations();
        guestDto.participations = participations == null ? Collections.emptyList() : participations;
        return guestDto;
    }

    private class GuestDto {
        public String name;
        public String age;
        public Integer bookedYogaHours;
        public String bookingDetails;
        public Date checkInAt;
        public Date checkOutAt;
        public String bookingId;
        public String bookerFirstName;
        public String bookerLastName;
        public List<Participation> participations;
    }
}
