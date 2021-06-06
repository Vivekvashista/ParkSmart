package com.codearena.parksmart.Model;

public class Bookings
{
    private String booking_id, date_time, name, mobile_no, number_of_vehicles, vehicle_no_1, vehicle_no_2, vehicle_no_3, vehicle_no_4,  vehicle_no_5, parking_address, amount, mode_of_payment, status;

    public Bookings(){}

    public Bookings(String booking_id, String date_time, String name, String mobile_no, String number_of_vehicles, String vehicle_no_1, String vehicle_no_2, String vehicle_no_3, String vehicle_no_4, String vehicle_no_5, String parking_address, String amount, String mode_of_payment, String status) {
        this.booking_id = booking_id;
        this.date_time = date_time;
        this.name = name;
        this.mobile_no = mobile_no;
        this.number_of_vehicles = number_of_vehicles;
        this.vehicle_no_1 = vehicle_no_1;
        this.vehicle_no_2 = vehicle_no_2;
        this.vehicle_no_3 = vehicle_no_3;
        this.vehicle_no_4 = vehicle_no_4;
        this.vehicle_no_5 = vehicle_no_5;
        this.parking_address = parking_address;
        this.amount = amount;
        this.mode_of_payment = mode_of_payment;
        this.status = status;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getNumber_of_vehicles() {
        return number_of_vehicles;
    }

    public void setNumber_of_vehicles(String number_of_vehicles) {
        this.number_of_vehicles = number_of_vehicles;
    }

    public String getVehicle_no_1() {
        return vehicle_no_1;
    }

    public void setVehicle_no_1(String vehicle_no_1) {
        this.vehicle_no_1 = vehicle_no_1;
    }

    public String getVehicle_no_2() {
        return vehicle_no_2;
    }

    public void setVehicle_no_2(String vehicle_no_2) {
        this.vehicle_no_2 = vehicle_no_2;
    }

    public String getVehicle_no_3() {
        return vehicle_no_3;
    }

    public void setVehicle_no_3(String vehicle_no_3) {
        this.vehicle_no_3 = vehicle_no_3;
    }

    public String getVehicle_no_4() {
        return vehicle_no_4;
    }

    public void setVehicle_no_4(String vehicle_no_4) {
        this.vehicle_no_4 = vehicle_no_4;
    }

    public String getVehicle_no_5() {
        return vehicle_no_5;
    }

    public void setVehicle_no_5(String vehicle_no_5) {
        this.vehicle_no_5 = vehicle_no_5;
    }

    public String getParking_address() {
        return parking_address;
    }

    public void setParking_address(String parking_address) {
        this.parking_address = parking_address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMode_of_payment() {
        return mode_of_payment;
    }

    public void setMode_of_payment(String mode_of_payment) {
        this.mode_of_payment = mode_of_payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
