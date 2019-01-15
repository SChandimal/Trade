package com.trustatrader.trustatrader.common;

import javax.persistence.*;

@Entity
@Table(name = "t_trust")
public class Item_Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tru_ID")
    private int truID;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_trade")
    private String itemTrade;

    @Column(name = "item_location")
    private String itemLocation;

    @Column(name = "phone_number_1")
    private String phoneNumber1;

    @Column(name = "phone_number_2")
    private String phoneNumber2;

    public int getTruID() {
        return truID;
    }

    public void setTruID(int truID) {
        this.truID = truID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemTrade() {
        return itemTrade;
    }

    public void setItemTrade(String itemTrade) {
        this.itemTrade = itemTrade;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }
}
