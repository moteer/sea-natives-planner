package com.seanatives.SurfCoursePlanner.controller;

import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.services.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/participation")
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    @PostMapping("/mark")
    @ResponseBody
    public ResponseEntity<Void> markAttendance(@RequestParam Long guestId, @RequestParam String courseType, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam boolean attended) {
        participationService.markAttendance(guestId, courseType, date, attended);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/weeklyAttendance")
    public String getWeeklyAttendance(@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                      @RequestParam(name = "direction", required = false, defaultValue = "0") int direction,
                                      Model model) {
        LocalDate today = LocalDate.now();

        LocalDate startOfWeek;
        if (startDate == null) {
            startOfWeek = today.with(DayOfWeek.MONDAY);
        } else {
            startOfWeek = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusWeeks(direction);
        }

        LocalDate endOfWeek = startOfWeek.with(DayOfWeek.SUNDAY);

        List<Date> weekDates = IntStream.rangeClosed(0, 6)
                .mapToObj(startOfWeek::plusDays)
                .map(date -> Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .collect(Collectors.toList());

        Date start = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Guest> guests = participationService.getGuestsCheckedInBetween(start, end);
        model.addAttribute("guests", guests);
        model.addAttribute("weekDates", weekDates);
        model.addAttribute("today", Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        model.addAttribute("startDate", start);

        return "weeklyAttendance";
    }


}
