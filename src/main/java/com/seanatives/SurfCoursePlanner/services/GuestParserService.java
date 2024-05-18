package com.seanatives.SurfCoursePlanner.services;

import com.seanatives.SurfCoursePlanner.domain.Guest;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GuestParserService {

    public boolean hasYogaBooked(Guest guest) {
        return guest.getBookingDetails().toLowerCase().contains("yoga");
    }

    public Guest parseGuest(Guest guest) {
        guest.setBookedYogaHours(parseBookedYogaHours(guest.getBookingDetails()));
        return guest;
    }

    /*
     ** Todo: need to state all the yoga related product combinations
     */
    private int parseBookedYogaHours(String bookingDetails) {
        String combinedInput = bookingDetails.replaceAll("\n", " ");

        // Define patterns to match
        Pattern pattern = Pattern.compile("(\\d+)[-\\s]?day yoga course|(\\d+)x\\s+Yoga lesson|Unscheduled\\s+Yoga lesson");
        Matcher matcher = pattern.matcher(combinedInput);

        int totalHours = 0;
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                totalHours += Integer.parseInt(matcher.group(1));
            } else if (matcher.group(2) != null) {
                totalHours += Integer.parseInt(matcher.group(2));
            } else {
                totalHours += 1; // For "Unscheduled Yoga lesson"
            }
        }

        return totalHours;
    }
}
