package com.sparkx.util;

import com.sparkx.core.config.Config;

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
    public static final String HOSPITAL_CREATE = "INSERT INTO " + HOSPITAL_TABLE + " (hospitalid, name, district, location_x, location_y) VALUES (?, ?, ?::district, ?, ?)";
    public static final String HOSPITAL_ALL = "SELECT hospitalid, name, district, location_x, location_y FROM " + HOSPITAL_TABLE;
    public static final String HOSPITAL_BY_DISTRICT = HOSPITAL_ALL + " WHERE district=?::district";
    public static final String HOSPITAL_BY_ID = HOSPITAL_ALL + " WHERE hospitalid=?::uuid";

    /* person queries */
    public static final String PERSON_CREATE = "INSERT INTO " + PERSON_TABLE + " (userid, email, password, first_name, last_name, hospitalid, role) VALUES (?, ?, ?, ?, ?, ?, ?::RoleType)";
    public static final String PERSON_ALL = "SELECT userid, email, first_name, last_name, hospitalid, role FROM " + PERSON_TABLE;
    public static final String PERSON_BY_ID = PERSON_ALL + " WHERE userid = ?::uuid";
    public static final String PERSON_BY_ROLE = PERSON_ALL + " WHERE role=?::RoleType";
    public static final String PERSON_BY_EMAIL = "SELECT userid, email, first_name, last_name, hospitalid, role, password FROM " + PERSON_TABLE + " WHERE email=?";
    public static final String PERSON_BY_ROLE_HOSPITAL = PERSON_ALL + " WHERE role=?::RoleType AND hospitalid=?::uuid";

    /* patient queries */
    public static final String PATIENT_CREATE = "INSERT INTO " + PATIENT_TABLE + " ( patientid, district, location_x, location_y, gender, contact, birthdate) VALUES (?, ?::district, ?, ?, ?::genderTypes, ?, ?)";
    public static final String PATIENT_ALL = "SELECT patientid, district, location_x, location_y, gender, contact, birthdate, email, first_name,last_name FROM " + PATIENT_TABLE + " join " + PERSON_TABLE + " on " + PATIENT_TABLE + ".patientId = " + PERSON_TABLE + ".userId";
    public static final String PATIENT_BY_PATIENT_ID = PATIENT_ALL + " WHERE patientid = ?::uuid";
    public static final String PATIENT_BY_USER_ID = PATIENT_ALL + " WHERE userid = ?";
    public static final String PATIENTS_BY_HOSPITAL_ID = "SELECT serialnumber, bedid, admitteddate," + RECORD_TABLE + ".hospitalid, regdate, admitteddate, queueid," + PATIENT_TABLE +
            ".patientid, district, location_x, location_y, gender, contact, birthdate, email, first_name,last_name " +
            " FROM " + PERSON_TABLE + " join " + PATIENT_TABLE + " on " + PATIENT_TABLE + ".patientId = " + PERSON_TABLE + ".userId"
            + " JOIN " + RECORD_TABLE + " on " + PATIENT_TABLE + ".patientId = " + RECORD_TABLE + ".patientId " +
            " WHERE " + RECORD_TABLE + ".hospitalid = ?::uuid AND dischargeddate is NULL AND closed is NULL";
    public static final String PATIENT_DEATH = "UPDATE " + PATIENT_TABLE + " SET death=? WHERE patientid=?::uuid";
    public static final String GET_ALL_DISTRICTS = "SELECT unnest(enum_range(NULL::district)) as district";

    /* bed queries */
    public static final String BEDS_CREATE = "INSERT INTO " + BED_TABLE + " (bedid, hospitalid, status) VALUES (1, ?, ?::statusType),(2, ?, ?::statusType),(3, ?, ?::statusType),(4, ?, ?::statusType),(5, ?, ?::statusType),(6, ?, ?::statusType),(7, ?, ?::statusType),(8, ?, ?::statusType),(9, ?, ?::statusType),(10, ?, ?::statusType)";
    public static final String BED_ALL = "SELECT bedid, hospitalid, status FROM " + BED_TABLE;
    public static final String BED_BY_STATUS = BED_ALL + " WHERE status= ?";
    public static final String BED_NEAREST = "SELECT bedid, bed.hospitalid, status, name, district, location_x, location_y,power((location_x - ?),2) + power((location_y - ?),2) AS distance\n" +
            "\tFROM " + BED_TABLE + " JOIN " + HOSPITAL_TABLE + " ON bed.hospitalid = hospital.hospitalid WHERE status = 'available' ORDER BY distance LIMIT 1";
    public static final String BED_BY_HOSPITAL_ID = BED_ALL + " WHERE hospitalid=?::uuid";
    public static final String BED_UPDATE_STATUS = "UPDATE " + BED_TABLE + " SET status=?::statustype WHERE bedid=? AND hospitalid=?";
    public static final String GET_BED_STATUS_WITH_HOSPITAL = "SELECT \n" +
            "hospital.hospitalid as hospitalid,\n" +
            "hospital.name as name, \n" +
            "hospital.district as district,\n" +
            "hospital.location_x as location_x,\n" +
            "hospital.location_y as location_y,\n" +
            "c.available as available,\n" +
            "c.unavailable as unavailable\n" +
            "FROM hospital JOIN (\n" +
            "SELECT \n" +
            "\tcount(status = 'available'::statustype OR NULL) as available ,\n" +
            "\tcount(status = 'unavailable' ::statustype OR NULL) as unavailable,\n" +
            "\thospitalid\n" +
            "\tFROM bed  \n" +
            "group by hospitalid) c\n" +
            "ON (c.hospitalid = hospital.hospitalid)";

    /* hospital stats */
    public static final String OVERALL_AVAILABLE_BED = "SELECT COUNT(bedid), hospitalid FROM " + BED_TABLE + "WHERE status = 'available' GROUP BY hospitalid";


    /* queue queries */
    public static final String QUEUE_CREATE = "INSERT INTO " + QUEUE_TABLE + "(queueid) VALUES (?)";
    public static final String QUEUE_DELETE = "DELETE FROM " + QUEUE_TABLE + " WHERE queueid=?";
    public static final String QUEUE_COUNT = "SELECT MAX(queueid) AS currentId FROM " + QUEUE_TABLE;

    /* queue view queries*/
    public static final String QUEUE_BY_ID = "SELECT queue_number, queueid, created_timestamp, patientid, district, location_x, location_y,serialNumber\n" +
            "\tFROM " + QUEUE_VIEW + " WHERE queueid = ?";
    public static final String QUEUE_NO_BY_ID = "SELECT queue_number FROM " + QUEUE_VIEW + " WHERE queueid = ?";
    public static final String QUEUE_NEED_HOSPITAL = "SELECT district as maxdis\n" +
            "FROM queue_view  GROUP BY district\n" +
            "HAVING COUNT (district)=(\n" +
            "    SELECT MAX(    maxdis)\n" +
            "    FROM (\n" +
            "             SELECT COUNT(district) AS maxdis FROM queue_view\n" +
            "             GROUP BY district) AS maxcount)";
    public static final String QUEUE_LENGTH = "SELECT count(queue_number) as length FROM queue_view";
    public static final String QUEUE_TOP = "SELECT queueid,serialnumber FROM " + QUEUE_VIEW + " WHERE queue_number <= " + Config.BEDS_PER_HOSPITAL;
    public static final String QUEUE_FIRST = "SELECT queueid,serialnumber FROM " + QUEUE_VIEW + " WHERE queue_number = 1";

    /* record queries */
    public static final String RECORD_CREATE = "INSERT INTO " + RECORD_TABLE + " (patientid, serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String RECORD_UPDATE_ADMITTED = "UPDATE " + RECORD_TABLE + " SET admitteddate=? WHERE serialnumber = ?::uuid";
    public static final String RECORD_UPDATE_DISCHARGED = "UPDATE " + RECORD_TABLE + " SET dischargeddate=? WHERE serialnumber = ?::uuid";
    public static final String RECORD_BY_PATIENT_ID = "SELECT serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid FROM " + RECORD_TABLE + " WHERE patientid = ?::uuid";
    public static final String RECORD_ACTIVE_BY_PATIENT_ID = "SELECT serialnumber, bedid, hospitalid, regdate, admitteddate, dischargeddate, queueid FROM " + RECORD_TABLE + " WHERE patientid = ?::uuid AND dischargeddate is NULL AND closed is null";
    public static final String RECORD_BY_HOSPITAL = "SELECT patientid,serialnumber, bedid, regdate, admitteddate, dischargeddate FROM " + RECORD_TABLE + " WHERE hospitalid = ?";
    public static final String RECORD_UPDATE = "UPDATE " + RECORD_TABLE + " SET queueId = null, hospitalid =?, bedid=? WHERE serialNumber=?";
    public static final String RECORD_BED_BY_SERIAL_NUMBER = "SELECT bedid, hospitalid FROM " + RECORD_TABLE + " WHERE serialnumber = ?::uuid";
    public static final String RECORD_BED_BY_PATIENT_ID = "SELECT bedid, hospitalid FROM " + RECORD_TABLE + " WHERE patientid = ?::uuid AND dischargeddate is NULL AND closed is null";

    public static final String RECORD_CLOSE = "UPDATE " + RECORD_TABLE + " SET closed =? WHERE patientId=?::uuid AND dischargeddate is NULL";

    /* daily status*/
    public static final String DAILY_NEW_CASES_COUNTRY_LEVEL = "SELECT COUNT(serialnumber) AS newcases FROM " + RECORD_TABLE + " WHERE regdate=?";
    public static final String DAILY_RECOVERED_COUNTRY_LEVEL = "SELECT COUNT(serialnumber) AS recovered FROM " + RECORD_TABLE + " WHERE dischargeddate=?";
    public static final String DAILY_DEATHS_COUNTRY_LEVEL = "SELECT COUNT(serialnumber) AS deaths FROM " + RECORD_TABLE + " WHERE closed=?";

    public static final String DAILY_NEW_CASES_HOSPITAL_LEVEL = DAILY_NEW_CASES_COUNTRY_LEVEL + " AND hospitalid=?::uuid";
    public static final String DAILY_RECOVERED_HOSPITAL_LEVEL = DAILY_RECOVERED_COUNTRY_LEVEL + " AND hospitalid=?::uuid";
    public static final String DAILY_DEATHS_HOSPITAL_LEVEL = DAILY_DEATHS_COUNTRY_LEVEL + " AND hospitalid=?::uuid";

    public static final String DAILY_NEW_CASES_DISTRICT_LEVEL = "SELECT COUNT(serialnumber) AS newcases FROM " + RECORD_TABLE
            + " JOIN " + PATIENT_TABLE + " ON " + RECORD_TABLE + ".patientId = " + PATIENT_TABLE
            + ".patientId WHERE district =?::district AND regdate=?";
    public static final String DAILY_RECOVERED_DISTRICT_LEVEL = "SELECT COUNT(serialnumber) AS recovered FROM " + RECORD_TABLE
            + " JOIN " + PATIENT_TABLE + " ON " + RECORD_TABLE + ".patientId = " + PATIENT_TABLE
            + ".patientId WHERE district =?::district AND dischargeddate=?";
    public static final String DAILY_DEATHS_DISTRICT_LEVEL = "SELECT COUNT(serialnumber) AS deaths FROM " + RECORD_TABLE
            + " JOIN " + PATIENT_TABLE + " ON " + RECORD_TABLE + ".patientId = " + PATIENT_TABLE
            + ".patientId WHERE district =?::district AND closed=?";

    public static final String TOTAL_CASES = "SELECT COUNT( DISTINCT serialnumber) FROM " + RECORD_TABLE;
    public static final String TOTAL_RECOVER = "SELECT COUNT( DISTINCT serialnumber) FROM " + RECORD_TABLE + " WHERE dischargeddate IS NOT NULL";
    public static final String TOTAL_DEATHS = "SELECT COUNT( DISTINCT serialnumber) FROM " + RECORD_TABLE + " WHERE closed IS NOT NULL";

    /* severity queries */
    public static final String SEVERITY_CREATE = "INSERT INTO " + SEVERITY_TABLE + "(severityid, level, doctorid, markeddate, serialnumber) VALUES (?, ?::severitylevel, ?, ?, ?::uuid)";
    public static final String SEVERITY_BY_SERIAL_NUMBER = "SELECT severityid, level, doctorid, markeddate, serialnumber FROM " + SEVERITY_TABLE +
            " WHERE serialnumber = ?";

    /* types */
    public static final String GET_ALL_ROLE_TYPE = "SELECT unnest(enum_range(NULL::roleType)) as roleType";
    public static final String GET_ALL_SEVERITY_TYPE = "SELECT unnest(enum_range(NULL::severitylevel)) as severitylevel";

}
