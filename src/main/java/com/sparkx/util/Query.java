package com.sparkx.util;

public class Query {

    private Query() {
    }

    public static final String HOSPITAL_TABLE = "hospital";
    public static final String PERSON_TABLE = "person";
    public static final String PATIENT_TABLE = "patient";
    public static final String BED_TABLE = "bed";
    public static final String QUEUE_TABLE = "queue";
    public static final String RECORD_TABLE = "record";
    public static final String SEVERITY_TABLE = "severity";

    public static final String QUEUE_VIEW = "queue_view";

    /* hospital queries */
    public static final String HOSPITAL_CREATE = "INSERT INTO " + HOSPITAL_TABLE + " (hospitalid, name, district, location_x, location_y) VALUES (?, ?, ?, ?, ?)";
    public static final String HOSPITAL_ALL = "SELECT hospitalid, name, district, location_x, location_y FROM " + HOSPITAL_TABLE;
    public static final String HOSPITAL_BY_DISTRICT = HOSPITAL_ALL + "WHERE district=?";

    /* person queries */
    public static final String PERSON_CREATE = "INSERT INTO " + PERSON_TABLE + " (userid, email, password, first_name, last_name, hospitalid, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String PERSON_ALL = "SELECT userid, email, first_name, last_name, hospitalid, role FROM " + PERSON_TABLE;
    public static final String PERSON_BY_ID = PERSON_ALL + " WHERE userid = ?";
    public static final String PERSON_BY_ROLE = PERSON_ALL + " WHERE role=?";

    /* patient queries */
    public static final String PATIENT_CREATE = "INSERT INTO " + PATIENT_TABLE + " ( patientid, district, location_x, location_y, gender, contact, birthdate) VALUES (?, ?, ?, ?, ?::genderTypes, ?, ?)";
    public static final String PATIENT_ALL = "SELECT patientid, district, location_x, location_y, gender, contact, birthdate, email, first_name,last_name FROM " + PATIENT_TABLE + " join " + PERSON_TABLE + " on " + PATIENT_TABLE + ".patientId = " + PERSON_TABLE + ".userId";
    public static final String PATIENT_BY_PATIENT_ID = PATIENT_ALL + " WHERE patientid = ?::uuid";
    public static final String PATIENT_BY_USER_ID = PATIENT_ALL + " WHERE userid = ?";

    /* bed queries */
    public static final String BEDS_CREATE = "INSERT INTO " + BED_TABLE + " (bedid, hospitalid, status) VALUES (1, ?, ?::statusType),(2, ?, ?::statusType),(3, ?, ?::statusType),(4, ?, ?::statusType),(5, ?, ?::statusType),(6, ?, ?::statusType),(7, ?, ?::statusType),(8, ?, ?::statusType),(9, ?, ?::statusType),(10, ?, ?::statusType)";
    public static final String BED_ALL = "SELECT bedid, hospitalid, status FROM " + BED_TABLE;
    public static final String BED_BY_STATUS = BED_ALL + " WHERE status= ?";
    public static final String BED_NEAREST = "SELECT bedid, bed.hospitalid, status, name, district, location_x, location_y,power((location_x - ?),2) + power((location_y - ?),2) AS distance\n" +
            "\tFROM " + BED_TABLE + " JOIN " + HOSPITAL_TABLE + " ON bed.hospitalid = hospital.hospitalid WHERE status = 'available' ORDER BY distance LIMIT 1";
    public static final String BED_BY_HOSPITAL_ID = BED_ALL + " WHERE hospitalid=?";
    public static final String BED_UPDATE_STATUS = "UPDATE " + BED_TABLE + " SET status=?::statustype WHERE bedid=? AND hospitalid=?";

    /* queue queries */
    public static final String QUEUE_CREATE = "INSERT INTO " + QUEUE_TABLE + "(queueid) VALUES (?)";
    public static final String QUEUE_DELETE = "DELETE FROM " + QUEUE_TABLE + " WHERE queueid=?";
    public static final String QUEUE_COUNT = "SELECT MAX(queueid) AS currentId FROM " + QUEUE_TABLE;

    /* queue view queries*/
    public static final String QUEUE_BY_ID = "SELECT queue_number, queueid, created_timestamp, patientid, district, location_x, location_y,serialNumber\n" +
            "\tFROM " + QUEUE_VIEW + " WHERE queueid = ?";
    public static final String QUEUE_NO_BY_ID = "SELECT queue_number FROM " + QUEUE_VIEW + " WHERE queueid = ?";

    /* record queries */
    public static final String RECORD_CREATE = "INSERT INTO " + RECORD_TABLE + " (patientid, serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String RECORD_UPDATE_ADMITTED = "UPDATE " + RECORD_TABLE + " SET bedid=?, hospitalid=?, admitteddate=? WHERE serialnumber = ?";
    public static final String RECORD_UPDATE_DISCHARGED = "UPDATE " + RECORD_TABLE + " SET dischargeddatee=? WHERE serialnumber = ?";
    public static final String RECORD_BY_PATIENT_ID = "SELECT serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid FROM " + RECORD_TABLE + " WHERE patientid = ?::uuid";
    public static final String RECORD_ACTIVE_BY_PATIENT_ID = "SELECT serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid FROM " + RECORD_TABLE + " WHERE patientid = ?::uuid AND dischargeddate is NULL";
    public static final String RECORD_BY_HOSPITAL = "SELECT patientid,serialnumber, bedid, regdate, admitteddate, dischargeddate FROM " + RECORD_TABLE + " WHERE hospitalid = ?";

    /* severity queries */
    public static final String SEVERITY_CREATE = "INSERT INTO " + SEVERITY_TABLE + "(severityid, level, doctorid, markeddate, serialnumber) VALUES (?, ?, ?, ?, ?)";
    public static final String SEVERITY_BY_SERIAL_NUMBER = "SELECT severityid, level, doctorid, markeddate, serialnumber FROM " + SEVERITY_TABLE +
            "WHERE serialnumber = ?";

}
