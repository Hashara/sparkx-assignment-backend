package com.sparkx.service;

import com.sparkx.Exception.NotCreatedException;
import com.sparkx.core.Database;
import com.sparkx.model.Bed;
import com.sparkx.model.Queue;
import com.sparkx.model.Record;
import com.sparkx.model.Types.StatusType;
import com.sparkx.util.Message;
import com.sparkx.util.Query;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

            if (bed != null){
                updateBedStatus.setString(1, String.valueOf(StatusType.unavailable));
                updateBedStatus.setString(2,bed.getBedId());
                updateBedStatus.setObject(3,bed.getHospitalId());

                updateBedStatus.execute();
            }
            else {
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
}
