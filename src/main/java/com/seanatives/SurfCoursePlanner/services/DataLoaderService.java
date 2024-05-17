package com.seanatives.SurfCoursePlanner.services;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.CsvBooking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.repository.BookingRepository;
import com.seanatives.SurfCoursePlanner.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DataLoaderService {
    @Autowired
    private SeleniumScraperService seleniumScraperService;
    @Autowired
    private BookingCopyService bookingCopyService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private GuestRepository guestRepository;

    public void loadData() throws Exception {
        List<CsvBooking> csvBookings = seleniumScraperService.scrapeAllBookings();
        List<Booking> bookings = persistBookings(csvBookings);

        Date startDate = createDate(2024, Month.MAY, 1);
        Date endDate = createDate(2024, Month.JUNE, 1);

        List<Guest> guests = scrapeGuestsFor(bookings, startDate, endDate);
        persistGuests(guests);
        guests.forEach(System.out::println);
    }

    private void persistGuests(List<Guest> guests) {
        guestRepository.saveAll(guests);
    }

    private List<Booking> persistBookings(List<CsvBooking> csvBookings) {
        List<Booking> bookings = bookingCopyService.convertCsvsToEntities(csvBookings);
        bookingRepository.saveAll(bookings);
        return bookings;
    }

    private List<Guest> scrapeGuestsFor(List<Booking> bookings, Date startDate, Date endDate) {

        List<Booking> bookingsForDateRange = bookings.stream()
                .filter(booking -> isInTimeRange(startDate, endDate, booking))
                .collect(Collectors.toList());
        printBookings(bookingsForDateRange);
        return seleniumScraperService.scrapeGuestsFor(bookingsForDateRange);

    }

    private static boolean isInTimeRange(Date start, Date end, Booking booking) {
        LocalDate checkIn = toLocalDate(booking.getCheckInAt());
        LocalDate checkOut = toLocalDate(booking.getCheckOutAt());

        LocalDate startDate = toLocalDate(start);
        LocalDate endDate = toLocalDate(end);

        // Überprüft, ob das Check-in-Datum im Bereich [Startdatum, Enddatum] liegt
        boolean isCheckInInRange = !checkIn.isBefore(startDate) && !checkIn.isAfter(endDate);
        // Überprüft, ob das Check-out-Datum im Bereich [Startdatum, Enddatum] liegt
        boolean isCheckOutInRange = !checkOut.isBefore(startDate) && !checkOut.isAfter(endDate);

        return isCheckInInRange || isCheckOutInRange;
    }

    private static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    private void printBookings(List<Booking> bookingsForDateRange) {
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

    private Date createDatePlusOneDay(int year, Month month, int dayOfMonth) {
        // Erstelle ein LocalDate-Objekt
        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        // Füge einen Tag hinzu
        LocalDate nextDay = localDate.plusDays(1);
        // Konvertiere LocalDate in Date
        return Date.from(nextDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date createDate(int year, Month month, int dayOfMonth) {
        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

