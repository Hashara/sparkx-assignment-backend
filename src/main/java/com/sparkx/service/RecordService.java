package com.sparkx.service;

import com.sparkx.Exception.NotCreatedException;
import com.sparkx.Exception.NotFoundException;
import com.sparkx.core.Database;
import com.sparkx.model.dao.RecordDAO;
import com.sparkx.model.*;
import com.sparkx.model.Types.SeverityLevel;
import com.sparkx.model.Types.StatusType;
import com.sparkx.util.Message;
import com.sparkx.util.Query;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecordService {
    Logger logger = Logger.getLogger(RecordService.class);

    public void addRecord(Record record, Queue queue, Bed bed) throws NotCreatedException {
        try (Connection connection = Database.getConnection();
             PreparedStatement addRecord = connection.prepareStatement(Query.RECORD_CREATE);
             PreparedStatement updateBedStatus = connection.prepareStatement(Query.BED_UPDATE_STATUS);
             PreparedStatement addToQueue = connection.prepareStatement(Query.QUEUE_CREATE)) {

            connection.setAutoCommit(false);

//            patientId, serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid
            addRecord.setObject(1, record.getPatientId());
            addRecord.setObject(2, record.getSerialNumber());
            addRecord.setString(3, record.getBedId());
            addRecord.setObject(4, record.getHospitalId());
            addRecord.setDate(5, (Date) record.getRegDate());
            addRecord.setDate(6, (Date) record.getAdmittedDate());
            addRecord.setDate(7, (Date) record.getDischargedDate());
            addRecord.setObject(8, record.getQueueId());

            if (bed != null) {
                updateBedStatus.setString(1, String.valueOf(StatusType.unavailable));
                updateBedStatus.setString(2, bed.getBedId());
                updateBedStatus.setObject(3, bed.getHospitalId());

                updateBedStatus.execute();
            } else {
                addToQueue.setObject(1, queue.getQueueId());
                addToQueue.execute();
            }
            addRecord.execute();
            connection.commit();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw new NotCreatedException(Message.RECORD_NOT_CREATED + " " + throwables.getMessage());
        }
    }

    public Severity markSeverity(Severity severity) throws SQLException {
        if (severity.getSeverityId() == null){
            severity.setSeverityId(Util.getUUID());
        }
        if (severity.getMarkedDate() == null){
            severity.setMarkedDate(Util.getDate());
        }
        try(Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(Query.SEVERITY_CREATE)) {
//            severityid, level, doctorid, markeddate, serialnumber
            statement.setObject(1,severity.getSeverityId());
            statement.setString(2, String.valueOf(severity.getLevel()));
            statement.setObject(3,severity.getDoctorId());
            statement.setDate(4, (Date) severity.getMarkedDate());
            statement.setObject(5,severity.getSerialNumber());
            statement.execute();
            return severity;
        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public void updateAdmitDate(String serialNumber) throws Exception {
        try(Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(Query.RECORD_UPDATE_ADMITTED)) {
            long millis = System.currentTimeMillis();
            statement.setDate(1,new Date(millis) );
            statement.setString(2,serialNumber);
            statement.execute();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NotFoundException(Message.RECORD_NOT_FOUND);
        }
    }

    public void updateDischargeDate(String serialNumber) throws Exception {
        try(Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(Query.RECORD_UPDATE_DISCHARGED)) {
            statement.setDate(1, Util.getDate() );
            statement.setString(2,serialNumber);
            statement.execute();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new NotFoundException(Message.RECORD_NOT_FOUND);
        }
    }

    public List<RecordDAO> getRecordsByPatientID(String patientId) throws Exception {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query.RECORD_BY_PATIENT_ID)) {
            preparedStatement.setString(1, patientId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapResultSetToRecordList(resultSet);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
        }
        return null;
    }


    public RecordDAO getActiveRecordByPatientID(String patientId) throws NotFoundException, SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query.RECORD_ACTIVE_BY_PATIENT_ID)) {
            preparedStatement.setString(1, patientId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapResultSetToRecordList(resultSet).get(0);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundException(Message.RECORD_NOT_FOUND);
        }
    }

    public List<Severity> getSeverityList(UUID serialNumber) throws Exception {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.SEVERITY_BY_SERIAL_NUMBER)) {
            statement.setObject(1, serialNumber);
            ResultSet resultSet = statement.executeQuery();
            return mapResultSetToSeverityList(resultSet);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }
    private List<RecordDAO> mapResultSetToRecordList(ResultSet resultSet) throws Exception {
        List<RecordDAO> recordList = new ArrayList<>();

        //serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid
        while (resultSet.next()) {
            RecordDAO record = new RecordDAO();
            record.setSerialNumber((UUID) resultSet.getObject("serialnumber"));
            record.setBedId(resultSet.getString("bedid"));
            record.setHospitalId((UUID) resultSet.getObject("hospitalid"));
            record.setRegDate(resultSet.getDate("regdate"));
            record.setAdmittedDate(resultSet.getDate("admitteddate"));
            record.setDischargedDate(resultSet.getDate("dischargeddate"));
            record.setQueueId((UUID) resultSet.getObject("queueid"));
            record.setSeverityList(getSeverityList((UUID) resultSet.getObject("serialnumber")));
            recordList.add(record);
        }
        return recordList;
    }



    private List<Severity> mapResultSetToSeverityList(ResultSet resultSet) throws SQLException {
        List<Severity> severityList = new ArrayList<>();

        // severityid, level, doctorid, markeddate, serialnumber
        while (resultSet.next()) {
            Severity severity = new Severity();
            severity.setSeverityId((UUID)resultSet.getObject("severityid"));
            severity.setLevel(SeverityLevel.valueOf(resultSet.getString("level")));
            severity.setDoctorId((UUID) resultSet.getObject("doctorid"));
            severity.setMarkedDate(resultSet.getDate("markeddate"));
            severity.setSeverityId((UUID) resultSet.getObject("serialnumber"));

            severityList.add(severity);
        }
        return severityList;

    }
}
