package com.sparkx.model;

import com.sparkx.model.Types.StatusType;

import java.util.UUID;

public class Bed {

    private String bedId;
    private UUID hospitalId;
    private StatusType status;

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public UUID getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(UUID hospitalId) {
        this.hospitalId = hospitalId;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "bedId: " + bedId + " ,hospitalId: " + hospitalId + " ,status: " + status;
    }
}
