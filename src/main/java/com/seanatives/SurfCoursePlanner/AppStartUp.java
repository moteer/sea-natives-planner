package com.seanatives.SurfCoursePlanner;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.scraper.SeleniumScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppStartUp implements CommandLineRunner {

    @Autowired
    private SeleniumScraper seleniumScraper;

    @Override
    public void run(String... args) throws Exception {

        List<Booking> bookings = seleniumScraper.scrapeAllBookings();
        System.out.println(bookings);

        List<Guest> guests = new ArrayList<>();
        guests.addAll(seleniumScraper.scrapeParticipantsForBooking("7c2d1392-22fe-442e-a121-28d19d185e65"));
        guests.addAll(seleniumScraper.scrapeParticipantsForBooking("d8dd0dc2-f614-4d65-b6bd-1d3f87e3d9e6"));
        System.out.println(guests);

        System.out.println("shut down");
        System.exit(0);
    }
}

