package com.sparkx.model;

import com.sparkx.model.Types.RoleType;

public class Person {
    private String userId;
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String hospitalId;
    private RoleType role;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "userId:" + getUserId() + ", firstName:" + getFirst_name() + ", lastName:" + getLast_name() +
                ", email:" + getEmail() + ", role:" + getRole() + ", hospitalId:" + getHospitalId();
    }
}
