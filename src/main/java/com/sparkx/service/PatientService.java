package com.sparkx.service;

import com.sparkx.model.Types.RoleType;
import com.sparkx.util.Query;
import com.sparkx.core.Database;
import com.sparkx.model.Patient;
import com.sparkx.model.Person;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PatientService{
    Logger logger = Logger.getLogger(PatientService.class);

    public boolean addPatient( Patient patient) throws SQLException {

        if (patient.getPatientId() == null){
            patient.setPatientId(Util.getUUID());
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement createPerson = connection.prepareStatement(Query.PERSON_CREATE);
            PreparedStatement createPatient = connection.prepareStatement(Query.PATIENT_CREATE)){

            connection.setAutoCommit(false);

            createPerson.setObject(1, patient.getUserId());
            createPerson.setString(2, patient.getEmail());
            createPerson.setString(3, patient.getPassword());
            createPerson.setString(4, patient.getFirst_name());
            createPerson.setString(5, patient.getLast_name());
            createPerson.setObject(6, null);
            createPerson.setString(7, String.valueOf(RoleType.Patient));

            createPerson.execute();

            //patientid, district, location_x, location_y, gender, contact, birthdate
            createPatient.setObject(1, patient.getPatientId());
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
            throw throwables;
        }
//        return false;
    }


}
