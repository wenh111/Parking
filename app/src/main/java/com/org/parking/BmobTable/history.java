package com.org.parking.BmobTable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class history extends BmobObject {

    public Integer Card_id;
    public String Car_number;
    public BmobDate in_time;
    public BmobDate out_time;
    public Double cost;

    public Integer getCard_id() {
        return Card_id;
    }

    public void setCard_id(Integer card_id) {
        Card_id = card_id;
    }

    public String getCar_number() {
        return Car_number;
    }

    public void setCar_number(String car_number) {
        Car_number = car_number;
    }

    public BmobDate getIn_time() {
        return in_time;
    }

    public void setIn_time(BmobDate in_time) {
        this.in_time = in_time;
    }

    public BmobDate getOut_time() {
        return out_time;
    }

    public void setOut_time(BmobDate out_time) {
        this.out_time = out_time;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
