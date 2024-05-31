package com.seanatives.SurfCoursePlanner.controller;

import com.seanatives.SurfCoursePlanner.services.DataLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@EnableAsync
@RequestMapping("/api/dataloader")
public class DataLoaderController {

    @Autowired
    private DataLoaderService dataLoaderService;

    @PostMapping("/fullscrape")
    public String fullScrapeOfBookings(@RequestParam("startDate") String startDate,
                                       @RequestParam("endDate") String endDate) throws Exception {

        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        Date start = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        dataLoaderService.loadDataIncludingAllBookingsFromCSV(start, end);
        return "Data loader started";
    }

    @PostMapping("/update")
    public String updateExistingBookings(@RequestParam("startDate") String startDate,
                                           @RequestParam("endDate") String endDate) throws Exception {

        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        Date start = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        dataLoaderService.rescrapeExistingBookings(start, end);
        return "Data loader started";
    }
}
