package com.sparkx.model.dao;

import com.sparkx.model.Hospital;

public class BedStatsDAO {
    private Hospital hospital;
    private int available;
    private int unavailable;

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getUnavailable() {
        return unavailable;
    }

    public void setUnavailable(int unavailable) {
        this.unavailable = unavailable;
    }
}
