package com.seanatives.SurfCoursePlanner.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guest> guests;

    public void addGuest(Guest guest) {
        createGuestListIfNeeded();
        this.guests.add(guest);
    }

    public void addGuests(List<Guest> guests) {
        createGuestListIfNeeded();
        this.guests.addAll(guests);
    }

    private void createGuestListIfNeeded() {
        if (this.guests == null)
            this.guests = new ArrayList<>();
    }


    private String bookingId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    private String channel;
    private String booking;
    private String status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutAt;
    private Integer nights;
    private String comments;
    private String location;
    private String bookerFirstName;
    private String bookerLastName;
    private String bookerGender;
    private String email;
    private String phone;
    private String address1;
    private String address2;
    private String address3;
    private String zipCode;
    private String city;
    private String state;
    private String country;
    private String currency;
    private Double subtotal;
    private Double total;
    private Double discount;
    private Double due;
    private String partnerCommission;
    private String partnerCommissionPercent;
    private Integer pax;
    private Integer couples;
}
