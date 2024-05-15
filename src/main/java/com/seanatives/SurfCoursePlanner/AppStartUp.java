package com.seanatives.SurfCoursePlanner;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.scraper.SeleniumScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AppStartUp implements CommandLineRunner {

    @Autowired
    private SeleniumScraper seleniumScraper;

    @Override
    public void run(String... args) throws Exception {

        List<Booking> bookings = seleniumScraper.scrapeAllBookings();
        // System.out.println(bookings);

        List<Guest> guests = new ArrayList<>();



        Date startDate = getDate(2024, Month.JUNE, 1);
        // todo end of day
        Date endDate = getDate(2024, Month.JUNE, 30);

        // todo make this a service
        bookings.stream()
                .filter(booking -> booking.getCheckInAt().after(startDate)
                        && booking.getCheckOutAt().before(endDate))
                .forEach(booking ->
                        System.out.printf("%s am %s bis %s%n", booking.getBookerFirstName(), booking.getCheckInAt(), booking.getCheckOutAt()));

//        guests.addAll(seleniumScraper.scrapeParticipantsForBooking("7c2d1392-22fe-442e-a121-28d19d185e65"));
//        guests.addAll(seleniumScraper.scrapeParticipantsForBooking("d8dd0dc2-f614-4d65-b6bd-1d3f87e3d9e6"));


        System.out.println("shut down");
        System.exit(0);
    }

    private static Date getDate(int year, Month month, int day) {
        ZonedDateTime zonedDateTime = LocalDate.of(year, month, day)
                .atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
}

