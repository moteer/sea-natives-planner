package com.seanatives.SurfCoursePlanner.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class Guest {
    private String name;
    private int numberOfSurfClassesBooked;
    private CsvBooking csvBooking;
}
