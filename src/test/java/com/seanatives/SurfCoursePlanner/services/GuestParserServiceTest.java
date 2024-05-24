package com.seanatives.SurfCoursePlanner.services;

import com.seanatives.SurfCoursePlanner.domain.Guest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GuestParserServiceTest {


    private GuestParserService guestParserService = new GuestParserService();

    private String ownAccomodation3 = "Date(s) Item Amount\n" +
            "29 Jun 2024 - 13 Jul 2024\n" +
            "Package Family Surf Camp main season 2024\n" +
            "€ 495.00\n" +
            "29 Jun 2024 - 13 Jul 2024\n" +
            "Own accommodation 3\n" +
            "2\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Food\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Surf equipment hire\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Surf course adults\n" +
            "€ 125.00\n" +
            "Unscheduled\n" +
            "3-day yoga course\n" +
            "€ 30.00\n" +
            "29 Jun 2024\n" +
            "Photos\n" +
            "€ 0.00";


    /**
     * Todo:
     *   Own accommodation 3
     *   Tent #19
     */
    @Test
    void parseOwnAccomodation() {
        Guest guest = new Guest();
        guest.setBookingDetails(ownAccomodation3);
        guestParserService.parseGuest(guest);
        assertThat(guest.getTent(), is("Own accommodation 3"));
    }

    private String tent_2 = "Date(s) Item Amount\n" +
            "1 Jun 2024 - 8 Jun 2024\n" +
            "Package Surf & Skate Package\n" +
            "€ 250.00\n" +
            "1 Jun 2024 - 8 Jun 2024\n" +
            "Tent #2\n" +
            "€ 140.00\n" +
            "Unscheduled\n" +
            "Food\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Surf equipment hire\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Skate lessons\n" +
            "€ 50.00\n" +
            "Unscheduled\n" +
            "Surf course\n" +
            "€ 125.00\n" +
            "Unscheduled\n" +
            "Yoga course\n" +
            "€ 50.00\n" +
            "Unscheduled\n" +
            "Private Transfer Porto - Surf Camp\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Private Transfer Surf Camp - Porto\n" +
            "€ 0.00";

    @Test
    void parseTent() {
        Guest guest = new Guest();
        guest.setBookingDetails(tent_2);
        guestParserService.parseGuest(guest);
        assertThat(guest.getTent(), is("Tent #2"));
    }

    /*
     ** Todo:
     *   Unscheduled Skate lessons € 50.00 -> 3 Tage
     *
     * */
    @Test
    void parseBookedSkateaHours_Uneschedules_Skate_lessons_€_50_00() {
        Guest guest = new Guest();
        guest.setBookingDetails(tent_2);
        guestParserService.parseGuest(guest);
        assertThat(guest.getBookedSkateLessons(), is(3));
    }

    private String Unscheduled_Skate_lessons_€_17_50 = "Date(s) Item Amount\n" +
            "29 Jun 2024 - 13 Jul 2024\n" +
            "Package Family Surf Camp main season 2024\n" +
            "€ 333.00\n" +
            "29 Jun 2024 - 13 Jul 2024\n" +
            "Own accommodation 3\n" +
            "3\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Food\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Surf equipment hire\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Surf course kids\n" +
            "€ 175.00\n" +
            "29 Jun 2024\n" +
            "Photos\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Skate lessons\n" +
            "€ 17.50";

    /*
     ** Todo:
     *   Unscheduled Skate lessons € 17.50 -> 1 Tag
     *
     * */
    @Test
    void parseBookedSkateaHours_Uneschedules_Skate_lessons_€_17_50() {
        Guest guest = new Guest();
        guest.setBookingDetails(Unscheduled_Skate_lessons_€_17_50);
        guestParserService.parseGuest(guest);
        assertThat(guest.getBookedSkateLessons(), is(1));
    }


    String _3x_Skate_lessons = "Date(s) Item Amount\n" +
            "18 Jun 2024 - 22 Jun 2024\n" +
            "Package Surf Camp Spring 2024\n" +
            "€ 140.00\n" +
            "18 Jun 2024 - 22 Jun 2024\n" +
            "Tent #8\n" +
            "2\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Full board\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Surf equipment hire\n" +
            "€ 0.00\n" +
            "Multiple dates\n" +
            "2x\n" +
            "Surf lesson adults\n" +
            "€ 50.00\n" +
            "Multiple dates\n" +
            "2x\n" +
            "Skate lessons\n" +
            "€ 35.00\n" +
            "Unscheduled\n" +
            "Photos\n" +
            "€ 0.00";

    /*
     ** Todo:
     *   2x Skate lessons € 35.00
     * */
    @Test
    void parseBookedSkateaHours_3x_Skate_lessons() {
        Guest guest = new Guest();
        guest.setBookingDetails(_3x_Skate_lessons);
        guestParserService.parseGuest(guest);
        assertThat(guest.getBookedSkateLessons(), is(2));
    }

    /*
     ** Todo:
     *   Unscheduled Surf course adults € 125.00 -> 5 Tage
     *   Unscheduled Surf course kids € 175.00 -> 5 Tage
     *   Surf course kids € 175.00 -> 5 Tage
     *   Surf lesson kids € 35.00 -> 1 Tag
     *   Multiple dates 5x Surf lesson kids € 0.00 -> 5 Tage
     *   Multiple dates 5x Surf lesson adults € 125.00 -> 5 Tage
     *   Surf course adults € 125.00 -> 5 Tage
     *   Multiple dates 5x Surf lesson adults € 0.00 -> 5 Tage
     * */
    @Test
    void parseBookedSurfHours_Multiple_dates_5x_Surf_lesson_adults() {
        Guest guest = new Guest();
        guest.setBookingDetails(_3x_Skate_lessons);
        guestParserService.parseGuest(guest);
        assertThat(guest.getBookedSurfLessons(), is(2));
    }

    String four_yoga_lessons = "Date(s) Item Amount\n" +
            "4 Jun 2024 - 11 Jun 2024\n" +
            "Package Surf Camp Spring 2024\n" +
            "€ 245.00\n" +
            "4 Jun 2024 - 11 Jun 2024\n" +
            "Tent #19\n" +
            "1\n" +
            "€ 385.00\n" +
            "Unscheduled\n" +
            "Full board\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Surf equipment hire\n" +
            "€ 0.00\n" +
            "Multiple dates\n" +
            "4x\n" +
            "Yoga lesson\n" +
            "€ 40.00\n" +
            "Multiple dates\n" +
            "5x\n" +
            "Surf lesson adults\n" +
            "€ 125.00\n" +
            "Unscheduled\n" +
            "Private Transfer Surf Camp - Porto\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Private Transfer Porto - Surf Camp\n" +
            "€ 0.00\n" +
            "Unscheduled\n" +
            "Photos\n" +
            "€ 0.00";

    /*
     ** Todo:
     *   3-day yoga course -> 3 Tage
     *   4x Yoga lesson € 40.00 -> 4 Tage
     *   Unscheduled Yoga course € 50.00 -> 5 Tage
     *   Multiple dates 4x Yoga lesson € 40.00 -> 4 Tage
     *   Multiple dates 3x Yoga lesson € 30.00 -> 3 Tage
     */
    @Test
    void parseBookedYogaHours() {
        Guest guest = new Guest();
        guest.setBookingDetails(four_yoga_lessons);
        guestParserService.parseGuest(guest);
        assertThat(guest.getBookedYogaLessons(), is(4));
    }
}
