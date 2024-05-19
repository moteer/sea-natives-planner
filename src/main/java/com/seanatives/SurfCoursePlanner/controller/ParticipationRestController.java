package com.seanatives.SurfCoursePlanner.controller;

import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.domain.Participation;
import com.seanatives.SurfCoursePlanner.repository.GuestRepository;
import com.seanatives.SurfCoursePlanner.repository.ParticipationRepository;
import com.seanatives.SurfCoursePlanner.services.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/participation")
public class ParticipationRestController {

    @Autowired
    private ParticipationService participationService;
    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private ParticipationRepository participationRepository;

    @PostMapping("/save")
    public List<Guest> saveParticipations(@RequestBody List<GuestDto> guestsDto) throws Exception {
        List<Guest> effectedGuests = new ArrayList<>();
        for (GuestDto guestDto : guestsDto) {
            Guest existingGuest = guestRepository.findById(guestDto.id).orElse(null);

            if (existingGuest == null)
                throw new Exception(format("cannot find guest: %s", existingGuest.toString()));

            effectedGuests.add(existingGuest);
            for (ParticipationDto participationDto : guestDto.participations) {
                if (!participationDto.attended) {
                    findAndDeleteIfExists(existingGuest, participationDto);
                } else {
                    Participation newParticipation = new Participation();
                    newParticipation.setAttended(true);
                    newParticipation.setDate(participationDto.date);
                    newParticipation.setCourseType(participationDto.courseType);
                    newParticipation.setGuest(existingGuest);
                    participationRepository.save(newParticipation);
                }
            }
        }
        return effectedGuests;
    }

    private void findAndDeleteIfExists(Guest existingGuest, ParticipationDto participationDto) {
        List<Participation> toBeDeleted = new ArrayList<>();
        for (Participation participation : existingGuest.getParticipations()) {
            if (participation.getId().equals(participationDto.id)) {
                toBeDeleted.add(participation);
            }
        }
        participationRepository.deleteAll(toBeDeleted);
    }

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
        guestDto.id = guest.getId();
        guestDto.name = guest.getName();
        guestDto.age = guest.getAge();
        guestDto.bookingDetails = guest.getBookingDetails();
        guestDto.checkInAt = guest.getBooking().getCheckInAt();
        guestDto.checkOutAt = guest.getBooking().getCheckOutAt();
        guestDto.bookingId = guest.getBooking().getBookingId();
        guestDto.bookerFirstName = guest.getBooking().getBookerFirstName();
        guestDto.bookerLastName = guest.getBooking().getBookerLastName();
        guestDto.bookedYogaHours = guest.getBookedYogaHours();
        guestDto.bookedSkateHours = guest.getBookedSkateHours();
        guestDto.bookedSurfHours = guest.getNumberOfSurfClassesBooked();
        List<Participation> participations = guest.getParticipations();
        guestDto.participations = participations == null ? Collections.emptyList() : toParticipationsDto(participations);
        return guestDto;
    }

    private List<ParticipationDto> toParticipationsDto(List<Participation> participations) {
        return participations.stream()
                .map(p ->
                        new ParticipationDto(p.getId(), p.getCourseType(), p.getDate(), p.isAttended()))
                .collect(Collectors.toList());
    }

}
