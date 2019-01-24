package com.trustatrader.trustatrader.common;

import javax.persistence.*;

@Entity
@Table(name = "FINAL")
public class Combination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int cmbID;

    @Column(name = "CODE")
    private String location;

    @Column(name = "SELL_TYPE")
    private String trade;

    @Column(name = "STATUS")
    private String status;

    public int getCmbID() {
        return cmbID;
    }

    public void setCmbID(int cmbID) {
        this.cmbID = cmbID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
