package com.sparkx.model;

import java.util.UUID;

public class Hospital {
    private UUID hospitalId;
    private String name;
    private String district;
    private int location_x;
    private int location_y;

    public UUID getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(UUID hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getLocation_x() {
        return location_x;
    }

    public void setLocation_x(int location_x) {
        this.location_x = location_x;
    }

    public int getLocation_y() {
        return location_y;
    }

    public void setLocation_y(int location_y) {
        this.location_y = location_y;
    }

    @Override
    public String toString() {
        return "id:" + getHospitalId() + ", name:" + getName() + ", district:" + getDistrict() +
                ", x:" + getLocation_x() + ", y:" + getLocation_y();
    }
}
