package com.org.parking.BmobTable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Card extends BmobObject {
    public Integer id;
    public String Car_number;
    public BmobDate Car_in_time;
    public Boolean isused;


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

    public BmobDate getCar_in_time() {
        return Car_in_time;
    }

    public void setCar_in_time(BmobDate car_in_time) {
        Car_in_time = car_in_time;
    }

    public Boolean getIsused() {
        return isused;
    }

    public void setIsused(Boolean isused) {
        this.isused = isused;
    }
}
