package com.sparkx.model.dao;

import com.sparkx.model.Person;

public class AuthDAO {
    private String jwt;
    private Person person;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
