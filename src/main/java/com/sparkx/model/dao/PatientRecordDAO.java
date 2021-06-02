package com.sparkx.model.dao;

import com.sparkx.model.Patient;
import com.sparkx.model.Record;

import java.util.List;

public class PatientRecordDAO {
    private Patient patient;
    private List<RecordDAO> records;
    private Record newRecord;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<RecordDAO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordDAO> records) {
        this.records = records;
    }

    public Record getNewRecord() {
        return newRecord;
    }

    public void setNewRecord(Record newRecord) {
        this.newRecord = newRecord;
    }

}
