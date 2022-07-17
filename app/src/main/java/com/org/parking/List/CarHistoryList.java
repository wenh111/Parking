package com.org.parking.List;

public class CarHistoryList {
    /*String createtable_history = "CREATE TABLE history " +
            "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Card_id INTEGER NOT NULL, " +
            "Car_number TEXT NOT NULL, " +
            "in_time TIMESTAMP NOT NULL, " +
            "out_time TIMESTAMP NOT NULL, " +
            "cost DOUBLE NOT NULL)";*/
    Integer Card_id;
    String Car_number;
    String in_time;
    String out_time;
    double cost;

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

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}
