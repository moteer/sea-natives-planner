package com.seanatives.SurfCoursePlanner;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.CsvBooking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.scraper.SeleniumScraper;
import com.seanatives.SurfCoursePlanner.services.BookingCopyService;
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

    @Autowired
    private BookingCopyService bookingCopyService;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void run(String... args) throws Exception {

        List<CsvBooking> csvBookings = seleniumScraper.scrapeAllBookings();
        persistBookings(csvBookings);

        Instant startDate = getDate(2024, Month.JUNE, 1);
        Instant endDate = getDate(2024, Month.JULY, 30);

        List<Guest> guests = scrapeGuestsFor(csvBookings, startDate, endDate);
        guests.forEach(System.out::println);

        //System.out.println("shut down");
        //System.exit(0);
    }

    private void persistBookings(List<CsvBooking> csvBookings) {
        List<Booking> bookings = bookingCopyService.convertCsvsToEntities(csvBookings);
        bookingRepository.saveAll(bookings);
    }

    private List<Guest> scrapeGuestsFor(List<CsvBooking> csvBookings, Instant startDate, Instant endDate) {
        Instant exclusiveEndDate = endDate.plus(Duration.ofDays(1));
        List<CsvBooking> bookingsForDateRange = csvBookings.stream()
                .filter(csvBooking -> csvBooking.getCheckInAt().after(Date.from(startDate))
                        && csvBooking.getCheckOutAt().before(Date.from(exclusiveEndDate)))
                .collect(Collectors.toList());
        printBookings(bookingsForDateRange);
        return seleniumScraper.scrapeGuestsFor(bookingsForDateRange);

    }

    private void printBookings(List<CsvBooking> bookingsForDateRange) {
        SimpleDateFormat deFormatter = new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.GERMANY);
        System.out.printf("%-20s %-30s %-30s%n", "Name", "Check-In", "Check-Out");
        bookingsForDateRange
                .forEach(csvBooking -> {
                            System.out.printf("%-20s %-30s %-30s%n", csvBooking.getBookerFirstName(),
                                    deFormatter.format(csvBooking.getCheckInAt()),
                                    deFormatter.format(csvBooking.getCheckOutAt()));
                        }
                );
    }

    private static Instant getDate(int year, Month month, int day) {
        ZonedDateTime zonedDateTime = LocalDate.of(year, month, day)
                .atStartOfDay(ZoneId.systemDefault());
        return zonedDateTime.toInstant();
    }
}

