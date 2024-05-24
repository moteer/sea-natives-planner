package com.seanatives.SurfCoursePlanner.controller;

import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.domain.Participation;
import com.seanatives.SurfCoursePlanner.repository.GuestRepository;
import com.seanatives.SurfCoursePlanner.repository.ParticipationRepository;
import com.seanatives.SurfCoursePlanner.services.ParticipationService;
import com.seanatives.SurfCoursePlanner.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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


    @PostMapping("/save2")
    public List<Participation> saveParticipations2(@RequestBody List<GuestDto> guestDtos) {
        List<Participation> newSavedParticipations = new ArrayList<>();
        List<ParticipationDto> participationDtos = guestDtos.stream()
                .flatMap(guestDto -> guestDto.participations.stream())
                .collect(Collectors.toList());

        // remove all false participations
        removeParticipations(participationDtos);

        List<String> participationFrontEndIdsToKeep = participationDtos.stream()
                .filter(participationDto -> participationDto.isAttended())
                .map(participationDto -> participationDto.frontendId)
                .collect(Collectors.toList());
        List<Participation> participationsAlreadySaved = participationRepository.findByFrontendIdIn(participationFrontEndIdsToKeep);
        List<String> frontendIdsOfAlreadySavedParticipations = participationsAlreadySaved.stream().map(participation -> participation.getFrontendId())
                .collect(Collectors.toList());

        System.out.printf("save Participations: %d already in db from %d new participations sent from frontend%n",
                frontendIdsOfAlreadySavedParticipations.size(),
                participationFrontEndIdsToKeep.size());
        participationFrontEndIdsToKeep.removeAll(frontendIdsOfAlreadySavedParticipations);

        participationDtos.stream()
                .filter(participationDto -> participationFrontEndIdsToKeep.contains(participationDto.frontendId))
                .forEach(participationDto -> {
                    Participation newParticipation = new Participation();
                    newParticipation.setAttended(true);
                    newParticipation.setDate(participationDto.date);
                    newParticipation.setCourseType(participationDto.courseType);
                    newParticipation.setFrontendId(participationDto.frontendId);
                    // Todo: not very performant here!!!
                    Optional<Guest> guestForParticipation = guestRepository.findById(participationDto.getGuestId());
                    newParticipation.setGuest(guestForParticipation.get());
                    newSavedParticipations.add(newParticipation);

                });
        participationRepository.saveAll(newSavedParticipations);

        return newSavedParticipations;
    }


    private void removeParticipations(List<ParticipationDto> participationDtos) {
        List<String> participationFrontendIdsToDelete = participationDtos.stream()
                .filter(participationDto -> !participationDto.isAttended())
                .map(participationDto -> participationDto.frontendId).collect(Collectors.toList());

        List<Participation> participationsToDelete = participationRepository.findByFrontendIdIn(participationFrontendIdsToDelete);
        participationsToDelete.forEach(p -> p.setGuest(null));
        participationRepository.deleteAll(participationsToDelete);
    }

    @PostMapping("/save")
    public List<Guest> saveParticipations(@RequestBody List<GuestDto> guestsDto) throws Exception {
        List<Guest> effectedGuests = new ArrayList<>();
        for (GuestDto guestDto : guestsDto) {
            Guest existingGuest = guestRepository.findById(guestDto.id).orElse(null);

            if (existingGuest == null)
                throw new Exception(format("cannot find guest: %s", existingGuest.toString()));

            effectedGuests.add(existingGuest);

            // to be newly created
            guestDto.participations.stream()
                    .filter(participationDto -> doesNotExistYet(existingGuest, participationDto, guestDto))
                    .forEach(participationDto -> {
                        Participation newParticipation = new Participation();
                        newParticipation.setAttended(true);
                        newParticipation.setDate(participationDto.date);
                        newParticipation.setCourseType(participationDto.courseType);
                        newParticipation.setGuest(existingGuest);
                        participationRepository.save(newParticipation);
                    });

            // to be deleted participations
            guestDto.participations.stream()
                    .filter(participationDto -> !participationDto.attended)
                    .forEach(participationDto -> findAndDeleteIfExists(existingGuest, participationDto));
        }
        return effectedGuests;
    }

    private boolean doesNotExistYet(Guest existingGuest, ParticipationDto participationDto, GuestDto guestDto) {
        for (Participation participation : existingGuest.getParticipations()) {
            if (DateUtils.isSameDay(participation.getDate(), participationDto.getDate())
                    && participation.getCourseType().equals(participationDto.getCourseType())
                    && participation.getGuest().getId().equals(guestDto.id))
                return false;
        }
        return true;
    }

    private void findAndDeleteIfExists(Guest existingGuest, ParticipationDto participationDto) {
        List<Participation> toBeDeleted = new ArrayList<>();
        for (Participation participation : existingGuest.getParticipations()) {
            if (DateUtils.isSameDay(participation.getDate(), participationDto.getDate())
                    && participation.getCourseType().equals(participationDto.getCourseType())) {
                toBeDeleted.add(participation);
            }
        }

        existingGuest.getParticipations().removeAll(toBeDeleted);
        toBeDeleted.forEach(p -> p.setGuest(null));
        participationRepository.deleteAll(toBeDeleted);
        guestRepository.save(existingGuest);
        // participationRepository.deleteAll(toBeDeleted);
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
        guestDto.bookedYogaHours = guest.getBookedYogaLessons();
        guestDto.bookedSkateHours = guest.getBookedSkateLessons();
        guestDto.bookedSurfHours = guest.getBookedSurfLessons();
        List<Participation> participations = guest.getParticipations();
        guestDto.tent = guest.getTent();
        guestDto.participations = participations == null ? Collections.emptyList() : toParticipationsDto(participations);
        return guestDto;
    }

    private List<ParticipationDto> toParticipationsDto(List<Participation> participations) {
        return participations.stream()
                .map(p ->
                        new ParticipationDto(p.getId(), p.getFrontendId(), p.getGuest().getId(), p.getCourseType(), p.getDate(), p.isAttended()))
                .collect(Collectors.toList());
    }

}
