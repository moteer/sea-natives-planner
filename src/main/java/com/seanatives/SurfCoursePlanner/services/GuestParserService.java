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

    private String combinedInput;

    public Guest parseGuest(Guest guest) {
        String bookingDetails = guest.getBookingDetails();
        combinedInput = bookingDetails.replaceAll("\n", " ");

        Integer bookedYogaLessons = parseBookedYogaHours();
        guest.setBookedYogaLessons(bookedYogaLessons);
        guest.setBookedYogaLessonsSuccessfullyScraped(bookedYogaLessons != null);

        Integer bookedSurfLessons = parseBookedSurfHours();
        guest.setBookedSurfLessons(bookedSurfLessons);
        guest.setBookedSurfLessonsSuccessfullyScraped(bookedSurfLessons != null);

        Integer bookedSkateLessons = parseBookedSkateHours();
        guest.setBookedSkateLessonsSuccessfullyScraped(bookedSkateLessons != null);

        guest.setBookedSkateLessons(bookedSkateLessons);
        guest.setTent(parseTent());
        return guest;
    }

    /**
     * Todo:
     *   Own accommodation 3
     *   Tent #19
     */
    private String parseTent() {
        String patternString = "Own accommodation \\d+|Tent #\\d+|Mini tent #\\d+";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(combinedInput);

        String tent = null;
        if (matcher.find()) {
            tent = matcher.group();
        }

        return tent;
    }

    /*
     ** Todo:
     *   Unscheduled Skate lessons € 50.00 -> 3 Tage
     *   Skate lessons -> 1 Tag (17,50 €)
     *   3x Skate lessons -> 3 Tage
Skate lessons
     *
     * */
    private Integer parseBookedSkateHours() {
        String threeDayPattern = "Unscheduled Skate lessons € 50\\.00";
        Pattern pattern = Pattern.compile(threeDayPattern);
        Matcher matcher = pattern.matcher(combinedInput);
        if (matcher.find()) {
            return 3;
        }

        String oneDayPattern = "Unscheduled Skate lessons € 17\\.50";
        pattern = Pattern.compile(oneDayPattern);
        matcher = pattern.matcher(combinedInput);
        if (matcher.find()) {
            return 1;
        }

        String amountOfLessonsPattern = "(\\d+)x Skate lessons €";
        pattern = Pattern.compile(amountOfLessonsPattern);
        matcher = pattern.matcher(combinedInput);
        if (matcher.find() && matcher.group(1) != null) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0;
    }

    /*
     *   Unscheduled Surf course adults € 125.00 -> 5 Tage
     *   Unscheduled Surf course kids € 175.00 -> 5 Tage
     *   Surf lesson kids € 35.00 -> 1 Tag
     *   Surf course kids € 175.00 -> 5 Tage
     *   Surf course adults € 125.00 -> 5 Tage
     *   Multiple dates 5x Surf lesson adults € 0.00 -> 5 Tage
     * */
    private Integer parseBookedSurfHours() {
        String multipleDatesSurfLessonPattern = "Multiple dates (\\d+)x Surf lesson adults €";
        Pattern pattern = Pattern.compile(multipleDatesSurfLessonPattern);
        Matcher matcher = pattern.matcher(combinedInput);
        if (matcher.find() && matcher.group(1) != null) {
            return Integer.parseInt(matcher.group(1));
        }

        String multipleDatesSurfLessonKidsPattern = "Multiple dates (\\d+)x Surf lesson kids € 0\\.00";
        pattern = Pattern.compile(multipleDatesSurfLessonKidsPattern);
        matcher = pattern.matcher(combinedInput);
        if (matcher.find() && matcher.group(1) != null) {
            return Integer.parseInt(matcher.group(1));
        }

        String surfCourseAdults = "Surf course adults € 125\\.00";
        pattern = Pattern.compile(surfCourseAdults);
        matcher = pattern.matcher(combinedInput);
        if (matcher.find()) {
            return 5;
        }

        String surfCourseKids = "Surf course kids € 175\\.00";
        pattern = Pattern.compile(surfCourseKids);
        matcher = pattern.matcher(combinedInput);
        if (matcher.find()) {
            return 5;
        }

        String surfLessonKids = "Surf lesson kids € 35\\.00";
        pattern = Pattern.compile(surfLessonKids);
        matcher = pattern.matcher(combinedInput);
        if (matcher.find()) {
            return 1;
        }

        return 0;
    }

    /*
     **
     *   Unscheduled Yoga course € 50.00 -> 5 Tage
     *   Multiple dates 3x Yoga lesson € 30.00 -> 3 Tage
     *   Unscheduled 3-day yoga course € 30.00 -> 3 tage
     */
    private Integer parseBookedYogaHours() {
        String unscheduledYogaCoursePattern = "(\\d+)-day yoga course €";
        Pattern pattern = Pattern.compile(unscheduledYogaCoursePattern);
        Matcher matcher = pattern.matcher(combinedInput);
        if (matcher.find() && matcher.group(1) != null) {
            return Integer.parseInt(matcher.group(1));
        }

        String multipleDatesSurfLessonKidsPattern = "(\\d+)x Yoga lesson €";
        pattern = Pattern.compile(multipleDatesSurfLessonKidsPattern);
        matcher = pattern.matcher(combinedInput);
        if (matcher.find() && matcher.group(1) != null) {
            return Integer.parseInt(matcher.group(1));
        }

        String unscheduledYogaCourse = "Unscheduled Yoga course € 50\\.00";
        pattern = Pattern.compile(unscheduledYogaCourse);
        matcher = pattern.matcher(combinedInput);
        if (matcher.find()) {
            return 5;
        }

        return 0;
    }
}
