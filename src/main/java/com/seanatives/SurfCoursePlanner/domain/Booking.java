package com.seanatives.SurfCoursePlanner.domain;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.util.Date;

@Data
public class Booking {

    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "Created")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private Date created;

    @CsvBindByName(column = "Channel")
    private String channel;

    @CsvBindByName(column = "Booking")
    private String booking;

    @CsvBindByName(column = "Status")
    private String status;

    @CsvBindByName(column = "Check-in at")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private Date checkInAt;

    @CsvBindByName(column = "Check-out at")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private Date checkOutAt;

    @CsvBindByName(column = "Nights")
    private Integer nights;

    @CsvBindByName(column = "Comments")
    private String comments;

    @CsvBindByName(column = "Location")
    private String location;

    @CsvBindByName(column = "Booker first name")
    private String bookerFirstName;

    @CsvBindByName(column = "Booker last name")
    private String bookerLastName;

    @CsvBindByName(column = "Booker gender")
    private String bookerGender;

    @CsvBindByName(column = "Email")
    private String email;

    @CsvBindByName(column = "Phone")
    private String phone;

    @CsvBindByName(column = "Address 1")
    private String address1;

    @CsvBindByName(column = "Address 2")
    private String address2;

    @CsvBindByName(column = "Address 3")
    private String address3;

    @CsvBindByName(column = "Zip code")
    private String zipCode;

    @CsvBindByName(column = "City")
    private String city;

    @CsvBindByName(column = "State")
    private String state;

    @CsvBindByName(column = "Country")
    private String country;

    @CsvBindByName(column = "Currency")
    private String currency;

    @CsvBindByName(column = "Subtotal")
    private Double subtotal;

    @CsvBindByName(column = "Total")
    private Double total;

    @CsvBindByName(column = "Discount")
    private Double discount;

    @CsvBindByName(column = "Due")
    private Double due;

    @CsvBindByName(column = "Partner commission")
    private String partnerCommission;

    @CsvBindByName(column = "Partner commission %")
    private String partnerCommissionPercent;

    @CsvBindByName(column = "Pax")
    private Integer pax;

    @CsvBindByName(column = "Couples")
    private Integer couples;

}
