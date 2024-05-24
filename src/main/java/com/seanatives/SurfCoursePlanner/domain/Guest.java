package com.seanatives.SurfCoursePlanner.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "guest")
@ToString(exclude = "participations")
public  class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer bookedYogaLessons = 0;
    private boolean bookedYogaLessonsSuccessfullyScraped;
    private Integer bookedSurfLessons = 0;
    private boolean bookedSurfLessonsSuccessfullyScraped;
    private Integer bookedSkateLessons = 0;
    private boolean bookedSkateLessonsSuccessfullyScraped;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
    private String age;
    @Lob
    @Column(name = "booking_details", columnDefinition = "TEXT")
    private String bookingDetails;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations;

    public String tent;
}
