package com.seanatives.SurfCoursePlanner.services;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.CsvBooking;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingCopyService {

    public List<Booking> convertCsvsToEntities(List<CsvBooking> csvBookings) {
        List<Booking> bookings = new ArrayList<>();
        csvBookings.forEach(csvBooking -> bookings.add(convertCsvToEntity(csvBooking)));
        return bookings;
    }

    public Booking convertCsvToEntity(CsvBooking csvBooking) {
        if (csvBooking == null) {
            return null;
        }

        Booking booking = new Booking();

        booking.setBookingId(csvBooking.getId());
        booking.setCreated(csvBooking.getCreated());
        booking.setChannel(csvBooking.getChannel());
        booking.setBooking(csvBooking.getBooking());
        booking.setStatus(csvBooking.getStatus());
        booking.setCheckInAt(csvBooking.getCheckInAt());
        booking.setCheckOutAt(csvBooking.getCheckOutAt());
        booking.setNights(csvBooking.getNights());
        booking.setComments(csvBooking.getComments());
        booking.setLocation(csvBooking.getLocation());
        booking.setBookerFirstName(csvBooking.getBookerFirstName());
        booking.setBookerLastName(csvBooking.getBookerLastName());
        booking.setBookerGender(csvBooking.getBookerGender());
        booking.setEmail(csvBooking.getEmail());
        booking.setPhone(csvBooking.getPhone());
        booking.setAddress1(csvBooking.getAddress1());
        booking.setAddress2(csvBooking.getAddress2());
        booking.setAddress3(csvBooking.getAddress3());
        booking.setZipCode(csvBooking.getZipCode());
        booking.setCity(csvBooking.getCity());
        booking.setState(csvBooking.getState());
        booking.setCountry(csvBooking.getCountry());
        booking.setCurrency(csvBooking.getCurrency());
        booking.setSubtotal(csvBooking.getSubtotal());
        booking.setTotal(csvBooking.getTotal());
        booking.setDiscount(csvBooking.getDiscount());
        booking.setDue(csvBooking.getDue());
        booking.setPartnerCommission(csvBooking.getPartnerCommission());
        booking.setPartnerCommissionPercent(csvBooking.getPartnerCommissionPercent());
        booking.setPax(csvBooking.getPax());
        booking.setCouples(csvBooking.getCouples());

        // Hier muss ggf. die Logik zum Kopieren der Gästeliste implementiert werden
        // Dies hängt von der Struktur und den Beziehungen der Guest-Entität ab

        return booking;
    }
}
