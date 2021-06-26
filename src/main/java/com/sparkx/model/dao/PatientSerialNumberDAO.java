package com.sparkx.model.dao;

import com.sparkx.model.Patient;

import java.util.Date;
import java.util.UUID;

public class PatientSerialNumberDAO extends Patient {
    private UUID serialNumber;
    private int bedId;
    private Date admitteddate;

    public Date getAdmitteddate() {
        return admitteddate;
    }

    public void setAdmitteddate(Date admitteddate) {
        this.admitteddate = admitteddate;
    }

    public int getBedId() {
        return bedId;
    }

    public void setBedId(int bedId) {
        this.bedId = bedId;
    }

    public UUID getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }
}
