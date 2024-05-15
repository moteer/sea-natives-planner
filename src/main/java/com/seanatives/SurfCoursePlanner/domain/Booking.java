package com.seanatives.SurfCoursePlanner.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCheckInAt() {
        return checkInAt;
    }

    public void setCheckInAt(Date checkInAt) {
        this.checkInAt = checkInAt;
    }

    public Date getCheckOutAt() {
        return checkOutAt;
    }

    public void setCheckOutAt(Date checkOutAt) {
        this.checkOutAt = checkOutAt;
    }

    public Integer getNights() {
        return nights;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBookerFirstName() {
        return bookerFirstName;
    }

    public void setBookerFirstName(String bookerFirstName) {
        this.bookerFirstName = bookerFirstName;
    }

    public String getBookerLastName() {
        return bookerLastName;
    }

    public void setBookerLastName(String bookerLastName) {
        this.bookerLastName = bookerLastName;
    }

    public String getBookerGender() {
        return bookerGender;
    }

    public void setBookerGender(String bookerGender) {
        this.bookerGender = bookerGender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDue() {
        return due;
    }

    public void setDue(Double due) {
        this.due = due;
    }

    public String getPartnerCommission() {
        return partnerCommission;
    }

    public void setPartnerCommission(String partnerCommission) {
        this.partnerCommission = partnerCommission;
    }

    public String getPartnerCommissionPercent() {
        return partnerCommissionPercent;
    }

    public void setPartnerCommissionPercent(String partnerCommissionPercent) {
        this.partnerCommissionPercent = partnerCommissionPercent;
    }

    public Integer getPax() {
        return pax;
    }

    public void setPax(Integer pax) {
        this.pax = pax;
    }

    public Integer getCouples() {
        return couples;
    }

    public void setCouples(Integer couples) {
        this.couples = couples;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingId='" + bookingId + '\'' +
                ", created=" + created +
                ", channel='" + channel + '\'' +
                ", booking='" + booking + '\'' +
                ", status='" + status + '\'' +
                ", checkInAt=" + checkInAt +
                ", checkOutAt=" + checkOutAt +
                ", nights=" + nights +
                ", comments='" + comments + '\'' +
                ", location='" + location + '\'' +
                ", bookerFirstName='" + bookerFirstName + '\'' +
                ", bookerLastName='" + bookerLastName + '\'' +
                ", bookerGender='" + bookerGender + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", currency='" + currency + '\'' +
                ", subtotal=" + subtotal +
                ", total=" + total +
                ", discount=" + discount +
                ", due=" + due +
                ", partnerCommission='" + partnerCommission + '\'' +
                ", partnerCommissionPercent='" + partnerCommissionPercent + '\'' +
                ", pax=" + pax +
                ", couples=" + couples +
                '}';
    }
}
