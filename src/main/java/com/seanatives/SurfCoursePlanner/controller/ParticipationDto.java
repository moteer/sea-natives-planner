package com.seanatives.SurfCoursePlanner.controller;

import lombok.Data;

import java.util.Date;

@Data
public class ParticipationDto {
    public Long id;
    public String courseType;
    public Date date;
    public boolean attended;

    public ParticipationDto() {
    }

    public ParticipationDto(Long id, String courseType, Date date, boolean attended) {

        this.id = id;
        this.courseType = courseType;
        this.date = date;
        this.attended = attended;
    }
}
