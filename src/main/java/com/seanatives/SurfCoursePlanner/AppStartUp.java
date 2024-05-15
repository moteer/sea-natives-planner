package com.seanatives.SurfCoursePlanner;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.scraper.SeleniumScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class AppStartUp implements CommandLineRunner {

    @Autowired
    private SeleniumScraper seleniumScraper;

    @Override
    public void run(String... args) throws Exception {

        List<Booking> bookings = seleniumScraper.scrapeAllBookings();

        Instant startDate = getDate(2024, Month.JUNE, 1);
        Instant endDate = getDate(2024, Month.JULY, 30);

        List<Guest> guests = scrapeGuestsFor(bookings, startDate, endDate);
        guests.forEach(System.out::println);

        System.out.println("shut down");
        System.exit(0);
    }

    private List<Guest> scrapeGuestsFor(List<Booking> bookings, Instant startDate, Instant endDate) {
        Instant exclusiveEndDate = endDate.plus(Duration.ofDays(1));
        List<Booking> bookingsForDateRange = bookings.stream()
                .filter(booking -> booking.getCheckInAt().after(Date.from(startDate))
                        && booking.getCheckOutAt().before(Date.from(exclusiveEndDate)))
                .collect(Collectors.toList());
        printBookings(bookingsForDateRange);
        return seleniumScraper.scrapeGuestsFor(bookingsForDateRange);

    }

    private void printBookings(List<Booking> bookingsForDateRange) {
        SimpleDateFormat deFormatter = new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.GERMANY);
        System.out.printf("%-20s %-30s %-30s%n", "Name", "Check-In", "Check-Out");
        bookingsForDateRange
                .forEach(booking -> {
                            System.out.printf("%-20s %-30s %-30s%n", booking.getBookerFirstName(),
                                    deFormatter.format(booking.getCheckInAt()),
                                    deFormatter.format(booking.getCheckOutAt()));
                        }
                );
    }

    private static Instant getDate(int year, Month month, int day) {
        ZonedDateTime zonedDateTime = LocalDate.of(year, month, day)
                .atStartOfDay(ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }
}

