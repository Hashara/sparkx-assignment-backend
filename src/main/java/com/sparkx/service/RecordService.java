package com.sparkx.service;

import com.sparkx.core.Database;
import com.sparkx.model.Record;
import com.sparkx.util.Query;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecordService {
    Logger logger = Logger.getLogger(RecordService.class);

    public boolean createRecord (Record record){
        try(Connection connection =  Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(Query.RECORD_CREATE)) {

//            patientid, serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid
            statement.setString(1,record.getPatientId());
            statement.setString(2,record.getSerialNumber());
            statement.setString(3, record.getBedId());
            statement.setString(4, record.getHospitalId());
            statement.setDate(5, (Date) record.getRegDate());
            statement.setDate(6, (Date) record.getAdmittedDate());
            statement.setDate(7, (Date) record.getDischargedDate());
            statement.setInt(8,record.getQueueId());
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
        }
        return true;
    }
}
