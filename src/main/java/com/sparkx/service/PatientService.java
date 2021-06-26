package com.sparkx.service;

import com.sparkx.Exception.NotCreatedException;
import com.sparkx.Exception.NotFoundException;
import com.sparkx.model.*;
import com.sparkx.model.Types.RoleType;
import com.sparkx.model.dao.PatientRecordDAO;
import com.sparkx.model.dao.PatientSerialNumberDAO;
import com.sparkx.util.Message;
import com.sparkx.util.Query;
import com.sparkx.core.Database;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;

public class PatientService {
    Logger logger = Logger.getLogger(PatientService.class);

    public PatientRecordDAO addPatient(Patient patient) throws Exception {

        if (patient.getPatientId() == null) {
            patient.setPatientId(Util.getUUID());
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement createPerson = connection.prepareStatement(Query.PERSON_CREATE);
             PreparedStatement createPatient = connection.prepareStatement(Query.PATIENT_CREATE)) {

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
            createPatient.setInt(3, patient.getLocation_x());
            createPatient.setInt(4, patient.getLocation_y());
            createPatient.setString(5, String.valueOf(patient.getGender()));
            createPatient.setString(6, patient.getContact());
            createPatient.setDate(7, patient.getBirthDate());

            createPatient.execute();
            connection.commit();
            patient.setPassword(null);

            PatientRecordDAO patientDAO = new PatientRecordDAO();
            patientDAO.setPatient(patient);
            patientDAO.setNewRecord(addRecord(patient));

            return patientDAO;
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw new NotCreatedException(throwables.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public Record addRecord(Patient patient) throws Exception {
        Record record = new Record();
        record.setPatientId(patient.getPatientId());
        record.setSerialNumber(Util.getUUID());
        HospitalService hospitalService = new HospitalService();
        Bed bed = hospitalService.getNearestHospitalBed(patient.getLocation_x(), patient.getLocation_y());
        long millis = System.currentTimeMillis();
        record.setRegDate(new Date(millis));
        Queue queue = null;

        if (bed != null) {
            record.setBedId(bed.getBedId());
            record.setHospitalId(bed.getHospitalId());
            record.setAdmittedDate(record.getAdmittedDate());
        } else {
            queue = new Queue();
            queue.setQueueId(Util.getUUID());
            record.setQueueId(queue.getQueueId());

        }

        new RecordService().addRecord(record, queue, bed);

        if (bed == null) {
            try {
                record.setQueueNumber(hospitalService.getQueueNumberByQueueId(queue.getQueueId()));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return record;
    }

    public Patient getPatientById(String patientId) throws NotFoundException, InputMismatchException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.PATIENT_BY_PATIENT_ID)) {

            statement.setString(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            Patient patient = mapResultSetToPatientList(resultSet).get(0);

            return patient;
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw new InputMismatchException(throwables.getMessage());
        } catch (IndexOutOfBoundsException e) {
            logger.error(Message.PATIENT_NOT_FOUND);
            throw new NotFoundException(Message.PATIENT_NOT_FOUND);
        }
    }

    public List<PatientSerialNumberDAO> getPatientsByHospitalId(String hospitalId) throws Exception {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.PATIENTS_BY_HOSPITAL_ID)) {
            statement.setString(1, hospitalId);

            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToPatientSerialNumberList(resultSet);

        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public List<String> getAllDistricts() {
        List<String> districts = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(Query.GET_ALL_DISTRICTS);
            while (resultSet.next()) {
                districts.add(resultSet.getString("district"));
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
        }
        return districts;
    }

    private List<Patient> mapResultSetToPatientList(ResultSet resultSet) throws SQLException {
        List<Patient> patientList = new ArrayList<>();

        //patientid, district, location_x, location_y, gender, contact, birthdate, email, first_name,last_name
        while (resultSet.next()) {
            Patient p = new Patient();
            p.setPatientId((UUID) resultSet.getObject("patientid"));
            p.setDistrict(resultSet.getString("district"));
            p.setLocation_x(resultSet.getInt("location_x"));
            p.setLocation_y(resultSet.getInt("location_y"));
            p.setGender(resultSet.getString("gender"));
            p.setContact(resultSet.getString("contact"));
            p.setBirthDate(resultSet.getDate("birthdate"));
            p.setEmail(resultSet.getString("email"));
            p.setFirst_name(resultSet.getString("first_name"));
            p.setLast_name(resultSet.getString("last_name"));
            patientList.add(p);
        }
        return patientList;
    }

    private List<PatientSerialNumberDAO> mapResultSetToPatientSerialNumberList(ResultSet resultSet) throws SQLException {
        List<PatientSerialNumberDAO> patientList = new ArrayList<>();

        //patientid, district, location_x, location_y, gender, contact, birthdate, email, first_name,last_name
        while (resultSet.next()) {
            PatientSerialNumberDAO p = new PatientSerialNumberDAO();
            p.setPatientId((UUID) resultSet.getObject("patientid"));
            p.setDistrict(resultSet.getString("district"));
            p.setLocation_x(resultSet.getInt("location_x"));
            p.setLocation_y(resultSet.getInt("location_y"));
            p.setGender(resultSet.getString("gender"));
            p.setContact(resultSet.getString("contact"));
            p.setBirthDate(resultSet.getDate("birthdate"));
            p.setEmail(resultSet.getString("email"));
            p.setFirst_name(resultSet.getString("first_name"));
            p.setLast_name(resultSet.getString("last_name"));
            p.setSerialNumber((UUID) resultSet.getObject("serialnumber"));
            p.setBedId(resultSet.getInt("bedid"));
            p.setAdmitteddate(resultSet.getDate("admitteddate"));
            patientList.add(p);
        }
        return patientList;
    }
}
