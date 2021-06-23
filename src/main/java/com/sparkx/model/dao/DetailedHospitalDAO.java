package com.sparkx.model.dao;

import com.sparkx.model.Bed;
import com.sparkx.model.Hospital;
import com.sparkx.model.Person;

import java.util.List;

public class DetailedHospitalDAO {
    private Hospital hospital;
    private Person director;
    private List<Bed> beds;


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

    public List<Bed> getBeds() {
        return beds;
    }

    public void setBeds(List<Bed> beds) {
        this.beds = beds;
    }

}
