package com.org.parking.BmobTable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class book extends BmobObject {
    public String Car_number;
    public BmobDate book_time;
    public BmobDate expiration_time;
    public String location;

    public String getCar_number() {
        return Car_number;
    }

    public void setCar_number(String car_number) {
        Car_number = car_number;
    }

    public BmobDate getBook_time() {
        return book_time;
    }

    public void setBook_time(BmobDate book_time) {
        this.book_time = book_time;
    }

    public BmobDate getExpiration_time() {
        return expiration_time;
    }

    public void setExpiration_time(BmobDate expiration_time) {
        this.expiration_time = expiration_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
