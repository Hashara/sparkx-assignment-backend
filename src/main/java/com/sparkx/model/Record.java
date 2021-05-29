package com.sparkx.model;

import java.util.Date;
import java.util.UUID;

public class Record {
    private UUID patientId;
    private UUID serialNumber;
    private String bedId;
    private UUID hospitalId;
    private Date regDate;
    private Date admittedDate;
    private Date dischargedDate;
    private UUID queueId;
    private int queueNumber;

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }

    public UUID getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(UUID hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getAdmittedDate() {
        return admittedDate;
    }

    public void setAdmittedDate(Date admittedDate) {
        this.admittedDate = admittedDate;
    }

    public Date getDischargedDate() {
        return dischargedDate;
    }

    public void setDischargedDate(Date dischargedDate) {
        this.dischargedDate = dischargedDate;
    }

    public UUID getQueueId() {
        return queueId;
    }

    public void setQueueId(UUID queueId) {
        this.queueId = queueId;
    }

    @Override
    public String toString() {
        return  "patientId: " + patientId + ", serialNumber: " + serialNumber+
                ", bedId: " + bedId + ", hospitalId: " + hospitalId + ", regDate: " + regDate +
                ", admittedDate: " + admittedDate + ", dischargedDate: " + dischargedDate +
                ", queueId: " + queueId;
    }
}
