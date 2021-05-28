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

//    public Record createRecord(){
//
//    }
    public boolean addRecord (Record record){
        try(Connection connection =  Database.getConnection();
            PreparedStatement addRecord = connection.prepareStatement(Query.RECORD_CREATE)) {

//            patientid, serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid
            addRecord.setObject(1,record.getPatientId());
            addRecord.setObject(2,record.getSerialNumber());
            addRecord.setString(3, record.getBedId());
            addRecord.setObject(4, record.getHospitalId());
            addRecord.setDate(5, (Date) record.getRegDate());
            addRecord.setDate(6, (Date) record.getAdmittedDate());
            addRecord.setDate(7, (Date) record.getDischargedDate());
            addRecord.setInt(8,record.getQueueId());
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
        }
        return true;
    }
}
