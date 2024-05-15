package com.seanatives.SurfCoursePlanner.controller;

import com.seanatives.SurfCoursePlanner.scraper.SeleniumScraper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorld {

    @GetMapping("/hello")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        //return new SeleniumScraper().scrapeAllBookings();
        return new SeleniumScraper().scrapeParticipantsForBooking("7c2d1392-22fe-442e-a121-28d19d185e65").toString();
    }
}
