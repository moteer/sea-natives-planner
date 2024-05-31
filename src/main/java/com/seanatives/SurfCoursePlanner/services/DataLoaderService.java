package com.seanatives.SurfCoursePlanner.services;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.CsvBooking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class DataLoaderService {
    @Autowired
    private SeleniumScraperService seleniumScraperService;
    @Autowired
    private BookingCopyService bookingCopyService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private GuestService guestService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Downloads the CSV of bookings from bookinglayer.
     * This way all new bookings will be scraped.
     *
     * Alle guest information of those bookings within the given period will be scraped.
     *
     * @param start
     * @param end
     * @throws Exception
     */
    public void loadDataIncludingAllBookingsFromCSV(Date start, Date end) throws Exception {
        messagingTemplate.convertAndSend("/topic/logs", "Scrape all bookings");
        List<CsvBooking> csvBookings = seleniumScraperService.scrapeAllBookings();
        List<Booking> bookings = persistBookings(csvBookings);
        messagingTemplate.convertAndSend("/topic/logs", format("%d bookings will be saved", bookings.size()));
        List<Guest> guests = scrapeGuestsFor(bookings, start, end);
        guests.forEach(System.out::println);
    }

    /**
     * Only updates the existing guests for bookings that are already persisted before
     * that have checkin dates within the given range.
     *
     * @param start
     * @param end
     * @throws Exception
     */
    public void rescrapeExistingBookings(Date start, Date end) throws Exception {
        messagingTemplate.convertAndSend("/topic/logs", "Update Bookings and scrape guest details again");
        List<Booking> bookings = bookingService.findBookingsInRange2(getStartOfDay(start), getEndOfDay(end));
        messagingTemplate.convertAndSend("/topic/logs", format("%d bookings will be saved", bookings.size()));
        List<Guest> guests = seleniumScraperService.scrapeGuestsFor(bookings);
        guests.forEach(System.out::println);
    }

    public static Date getStartOfDay(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime startOfDay = localDate.atStartOfDay();
        ZonedDateTime zonedDateTime = startOfDay.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date getEndOfDay(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59, 999999999);
        ZonedDateTime zonedDateTime = endOfDay.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    private void persistGuests(List<Guest> guests) {
        guestService.saveOrUpdateGuest(guests);
    }

    private List<Booking> persistBookings(List<CsvBooking> csvBookings) {
        List<Booking> bookings = bookingCopyService.convertCsvsToEntities(csvBookings);
        return bookingService.saveOrUpdateBookings(bookings);
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

