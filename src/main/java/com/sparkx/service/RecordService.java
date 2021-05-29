package com.sparkx.service;

import com.sparkx.Exception.NotCreatedException;
import com.sparkx.Exception.NotFoundException;
import com.sparkx.core.Database;
import com.sparkx.model.*;
import com.sparkx.model.Types.StatusType;
import com.sparkx.util.Message;
import com.sparkx.util.Query;
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

    public List<Record> getRecordsByPatientID(String patientId) {
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


    public Record getActiveRecordByPatientID(String patientId) throws NotFoundException, SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query.RECORD_ACTIVE_BY_PATIENT_ID)) {
            preparedStatement.setString(1, patientId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapResultSetToRecordList(resultSet).get(0);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new NotFoundException(Message.RECORD_NOT_FOUND);
        }
    }

    private List<Record> mapResultSetToRecordList(ResultSet resultSet) throws SQLException {
        List<Record> recordList = new ArrayList<>();

        //serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid
        while (resultSet.next()) {
            Record record = new Record();
            record.setSerialNumber((UUID) resultSet.getObject("serialnumber"));
            record.setBedId(resultSet.getString("bedid"));
            record.setHospitalId((UUID) resultSet.getObject("hospitalid"));
            record.setRegDate(resultSet.getDate("regdate"));
            record.setAdmittedDate(resultSet.getDate("admitteddate"));
            record.setDischargedDate(resultSet.getDate("dischargeddate"));
            record.setQueueId((UUID) resultSet.getObject("queueid"));
            recordList.add(record);
        }
        return recordList;
    }
}
