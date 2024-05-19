package com.seanatives.SurfCoursePlanner.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "participation")
@Data
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String frontendId;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    private String courseType;

    @Temporal(TemporalType.DATE)
    private Date date;

    private boolean attended;
}
