package com.org.parking.List;

public class BookCarList {
    public String Car_number;
    public String book_time;
    public String expiration_time;
    public String location;

    public String getCar_number() {
        return Car_number;
    }

    public void setCar_number(String car_number) {
        Car_number = car_number;
    }

    public String getBook_time() {
        return book_time;
    }

    public void setBook_time(String book_time) {
        this.book_time = book_time;
    }

    public String getExpiration_time() {
        return expiration_time;
    }

    public void setExpiration_time(String expiration_time) {
        this.expiration_time = expiration_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
