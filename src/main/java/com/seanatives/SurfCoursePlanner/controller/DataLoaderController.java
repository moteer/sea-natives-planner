package com.seanatives.SurfCoursePlanner.controller;

import com.seanatives.SurfCoursePlanner.services.DataLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/start")
    public String startDataLoader(@RequestParam("startDate") String startDate,
                                  @RequestParam("endDate") String endDate) throws Exception{

        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        Date start = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        dataLoaderService.loadData(start, end, messagingTemplate);
        return "Data loader started";
    }
}
