package com.seanatives.SurfCoursePlanner.controller;

import com.seanatives.SurfCoursePlanner.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorld {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/bookings")
    public String bookings(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) throws Exception {
        model.addAttribute("name", name);

        return bookingRepository.findAll().toString();
    }
}
