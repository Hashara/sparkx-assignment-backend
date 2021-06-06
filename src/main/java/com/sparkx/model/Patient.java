package com.sparkx.model;

import com.sparkx.model.Types.GenderType;

import java.sql.Date;
import java.util.UUID;

public class Patient extends Person {
    private UUID patientId;
    private String district;
    private int location_x;
    private int location_y;
    private GenderType gender;
    private String contact;
    private Date birthDate;
    private Date death;

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public Date getDeath() {
        return death;
    }

    public void setDeath(Date death) {
        this.death = death;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
        setUserId(patientId);
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

    public GenderType getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = GenderType.valueOf(gender);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
