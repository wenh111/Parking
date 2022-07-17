package com.org.parking.List;

public class CarList {
    Integer id;
    String Car_number;
    String Car_in_time;
    String isused;
    String isbook;
    String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCar_number() {
        return Car_number;
    }

    public void setCar_number(String car_number) {
        Car_number = car_number;
    }

    public String getCar_in_time() {
        return Car_in_time;
    }

    public void setCar_in_time(String car_in_time) {
        Car_in_time = car_in_time;
    }

    public String getIsused() {
        return isused;
    }

    public void setIsused(String isused) {
        this.isused = isused;
    }

    public String getIsbook() {
        return isbook;
    }

    public void setIsbook(String isbook) {
        this.isbook = isbook;
    }
}
