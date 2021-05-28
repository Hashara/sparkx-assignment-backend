package com.sparkx.model;

import com.sparkx.model.Types.SeverityLevel;

import java.util.Date;
import java.util.UUID;

public class Severity {
    private String severityId;
    private SeverityLevel level;
    private UUID doctorId;
    private Date markedDate;
    private String serialNumber;

    public String getSeverityId() {
        return severityId;
    }

    public void setSeverityId(String severityId) {
        this.severityId = severityId;
    }

    public SeverityLevel getLevel() {
        return level;
    }

    public void setLevel(SeverityLevel level) {
        this.level = level;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public Date getMarkedDate() {
        return markedDate;
    }

    public void setMarkedDate(Date markedDate) {
        this.markedDate = markedDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
