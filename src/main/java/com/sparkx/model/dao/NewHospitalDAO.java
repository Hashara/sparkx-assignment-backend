package com.sparkx.model.dao;

import com.sparkx.model.Hospital;
import com.sparkx.model.Person;

public class NewHospitalDAO {
    private Hospital hospital;
    private Person director;

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Person getDirector() {
        return director;
    }

    public void setDirector(Person director) {
        this.director = director;
    }
}
