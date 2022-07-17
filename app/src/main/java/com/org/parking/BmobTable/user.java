package com.org.parking.BmobTable;

import cn.bmob.v3.BmobObject;

public class user extends BmobObject {
    /*_id integer  primary key autoincrement," +
            "name text not null," +
            "account text unique not null," +
            "password text not null," +
            "Telephone_number text unique not null,"+
            "identity text)";*/
    public Integer id;
    public String name;
    public String account;
    public String password;
    public String Telephone_number;
    public String identity;
    /*public String objectId;

    @Override
    public String getObjectId() {
        return objectId;
    }

    @Override
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone_number() {
        return Telephone_number;
    }

    public void setTelephone_number(String telephone_number) {
        Telephone_number = telephone_number;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
