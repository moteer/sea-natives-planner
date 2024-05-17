package com.seanatives.SurfCoursePlanner.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "guest")
public  class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int numberOfSurfClassesBooked;
    // Many-to-One Beziehung zu Booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
    private String age;

}
