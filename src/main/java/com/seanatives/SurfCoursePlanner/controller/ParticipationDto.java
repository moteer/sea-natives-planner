package com.seanatives.SurfCoursePlanner.controller;

import lombok.Data;

import java.util.Date;

@Data
public class ParticipationDto {
    public Long id;
    public String frontendId;
    public Long guestId;
    public String courseType;
    public Date date;
    public boolean attended;

    public ParticipationDto() {
    }

    public ParticipationDto(Long id, String frontendId, Long guestId, String courseType, Date date, boolean attended) {

        this.id = id;
        this.frontendId = frontendId;
        this.guestId = guestId;
        this.courseType = courseType;
        this.date = date;
        this.attended = attended;
    }
}
