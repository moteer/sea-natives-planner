package com.seanatives.SurfCoursePlanner;

import com.seanatives.SurfCoursePlanner.domain.Booking;
import com.seanatives.SurfCoursePlanner.domain.CsvBooking;
import com.seanatives.SurfCoursePlanner.domain.Guest;
import com.seanatives.SurfCoursePlanner.repository.BookingRepository;
import com.seanatives.SurfCoursePlanner.repository.GuestRepository;
import com.seanatives.SurfCoursePlanner.services.BookingCopyService;
import com.seanatives.SurfCoursePlanner.services.DataLoadingService;
import com.seanatives.SurfCoursePlanner.services.SeleniumScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class AppStartUp implements CommandLineRunner {

    @Autowired
    private DataLoadingService dataLoadingService;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("do nothing for now. The loading has been commented out");

        // dataLoadingService.loadData();
    }
}

