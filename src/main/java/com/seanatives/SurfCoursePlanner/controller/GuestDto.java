package com.seanatives.SurfCoursePlanner.controller;

import java.util.Date;
import java.util.List;

public class GuestDto {

    public GuestDto() {
    }

    public Long id;

    public String name;
    public String age;
    public String bookingDetails;
    public Date checkInAt;
    public Date checkOutAt;
    public String bookingId;
    public String bookerFirstName;
    public String bookerLastName;
    public List<ParticipationDto> participations;
    public Integer bookedYogaHours;
    public Integer bookedSurfHours;
    public Integer bookedSkateHours;
    public String tent;

}
