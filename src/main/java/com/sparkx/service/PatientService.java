package com.sparkx.service;

import com.sparkx.util.Query;
import com.sparkx.core.Database;
import com.sparkx.model.Patient;
import com.sparkx.model.Person;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PatientService{
    Logger logger = Logger.getLogger(PatientService.class);

    public boolean addPatient(Person person, Patient patient){
        try (Connection connection = Database.getConnection();
             PreparedStatement createPerson = connection.prepareStatement(Query.PERSON_CREATE);
            PreparedStatement createPatient = connection.prepareStatement(Query.PATIENT_CREATE)){

            connection.setAutoCommit(false);

            createPerson.setString(1, person.getUserId());
            createPerson.setString(2, person.getEmail());
            createPerson.setString(3, person.getPassword());
            createPerson.setString(4, person.getFirst_name());
            createPerson.setString(5, person.getLast_name());
            createPerson.setString(6, null);
            createPerson.setString(7, String.valueOf(person.getRole()));

            createPerson.execute();

            //patientid, district, location_x, location_y, gender, contact, birthdate
            createPatient.setString(1, patient.getPatientId());
            createPatient.setString(2, patient.getDistrict());
            createPatient.setInt(3,patient.getLocation_x());
            createPatient.setInt(4,patient.getLocation_y());
            createPatient.setString(5, String.valueOf(patient.getGender()));
            createPatient.setString(6,patient.getContact());
            createPatient.setDate(7,  patient.getBirthDate());

            createPatient.execute();
            connection.commit();
            return true;
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
        }
        return false;
    }


}
