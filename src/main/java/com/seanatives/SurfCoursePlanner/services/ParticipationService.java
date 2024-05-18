package com.seanatives.SurfCoursePlanner.services;

import com.google.common.collect.Lists;
import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.domain.Participation;
import com.seanatives.SurfCoursePlanner.repository.BookingRepository;
import com.seanatives.SurfCoursePlanner.repository.CourseRepository;
import com.seanatives.SurfCoursePlanner.repository.GuestRepository;
import com.seanatives.SurfCoursePlanner.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipationService {
    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private BookingRepository bookingRepository;
    private CourseRepository courseRepository;

    public void markAttendance(Long guestId, Long courseId, Date date, boolean attended) {
        Participation participation = participationRepository.findByGuestIdAndCourseIdAndDate(guestId, courseId, date)
                .orElse(new Participation());
        participation.setGuest(guestRepository.findById(guestId).get());
        participation.setCourse(courseRepository.findById(courseId).get());
        participation.setDate(date);
        participation.setAttended(attended);
        participationRepository.save(participation);
    }

    public List<Guest> getGuestsCheckedInBetween(Date startDate, Date endDate) {
        LocalDate start = convertToLocalDate(startDate);
        LocalDate end = convertToLocalDate(endDate);

        List<Booking> bookings = Lists.newArrayList(bookingRepository.findAll());
        return bookings.stream()
                .filter(booking -> {
                    LocalDate checkIn = convertToLocalDate(booking.getCheckInAt());
                    LocalDate checkOut = convertToLocalDate(booking.getCheckOutAt());
                    return !checkIn.isAfter(end) && !checkOut.isBefore(start);
                })
                .flatMap(booking -> booking.getGuests().stream())
                .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public List<Participation> getParticipationsForGuest(Long guestId) {
        return participationRepository.findByGuestId(guestId);
    }
}
