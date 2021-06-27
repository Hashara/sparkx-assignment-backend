package com.sparkx.service;

import com.sparkx.Exception.FailedToAddException;
import com.sparkx.Exception.FailedToGetException;
import com.sparkx.Exception.NotCreatedException;
import com.sparkx.Exception.NotFoundException;
import com.sparkx.core.Database;
import com.sparkx.model.dao.QueuePatientSerialDAO;
import com.sparkx.model.dao.RecordDAO;
import com.sparkx.model.*;
import com.sparkx.model.Types.SeverityLevel;
import com.sparkx.model.Types.StatusType;
import com.sparkx.model.dao.StatsDAO;
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
        if (severity.getSeverityId() == null) {
            severity.setSeverityId(Util.getUUID());
        }
        if (severity.getMarkedDate() == null) {
            severity.setMarkedDate(Util.getDate());
        }
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.SEVERITY_CREATE)) {
//            severityid, level, doctorid, markeddate, serialnumber
            statement.setObject(1, severity.getSeverityId());
            statement.setString(2, String.valueOf(severity.getLevel()));
            statement.setObject(3, severity.getDoctorId());
            statement.setDate(4, (Date) severity.getMarkedDate());
            statement.setObject(5, severity.getSerialNumber());
            statement.execute();
            return severity;
        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public void markDeath(String patientId) throws SQLException, NotFoundException {
        try (Connection connection = Database.getConnection();
             PreparedStatement closeRecord = connection.prepareStatement(Query.RECORD_CLOSE);
             PreparedStatement patientUpdate = connection.prepareStatement(Query.PATIENT_DEATH)) {

            connection.setAutoCommit(false);
            Date date = Util.getDate();
            // closed =? WHERE patientId=?
            closeRecord.setDate(1, date);
            closeRecord.setString(2, patientId);

            // death=? WHERE patientId=?
            patientUpdate.setDate(1, date);
            patientUpdate.setString(2, patientId);

            closeRecord.execute();
            patientUpdate.execute();
            connection.commit();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundException(Message.RECORD_NOT_FOUND);
        }
    }

    public void updateAdmitDate(String serialNumber) throws Exception {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.RECORD_UPDATE_ADMITTED)) {
            long millis = System.currentTimeMillis();
            statement.setDate(1, new Date(millis));
            statement.setString(2, serialNumber);
            statement.execute();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundException(Message.RECORD_NOT_FOUND);
        }
    }

    public void updateDischargeDate(String serialNumber) throws Exception {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.RECORD_UPDATE_DISCHARGED);
             Statement queueFirst = connection.createStatement();
             PreparedStatement updateRecord = connection.prepareStatement(Query.RECORD_UPDATE);
             PreparedStatement deleteQueueItem = connection.prepareStatement(Query.QUEUE_DELETE);
             PreparedStatement updateBedStatus = connection.prepareStatement(Query.BED_UPDATE_STATUS)
        ) {

            connection.setAutoCommit(false);
            statement.setDate(1, Util.getDate());
            statement.setString(2, serialNumber);

            ResultSet resultSet = queueFirst.executeQuery(Query.QUEUE_FIRST);
            Bed bed = getBedDetailsBySerialNumber(serialNumber);
            try {
                QueuePatientSerialDAO firstQueue = mapResultSetToQueuePatientSerialDAO(resultSet).get(0);
                if (firstQueue == null) {
                    throw new NotFoundException(Message.QUEUE_EMPTY);
                }

                //  hospitalid =? bedid=? WHERE serialNumber=?
                updateRecord.setObject(1, bed.getHospitalId());
                updateRecord.setString(2, bed.getBedId());
                updateRecord.setObject(3, firstQueue.getSerialNumber());

                deleteQueueItem.setObject(1, firstQueue.getQueueId());


                updateRecord.execute();
                deleteQueueItem.execute();
            } catch (Exception e) {
                updateBedStatus.setString(1, String.valueOf(StatusType.available));
                updateBedStatus.setString(2, bed.getBedId());
                updateBedStatus.setObject(3, bed.getHospitalId());

//                updateRecord.execute();
//                deleteQueueItem.execute();
                updateBedStatus.execute();
            }
            statement.execute();

            connection.commit();

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundException(Message.RECORD_NOT_FOUND);
        }
    }

    private Bed getBedDetailsBySerialNumber(String serialNumber) throws Exception {
        try (Connection connection = Database.getConnection();
             PreparedStatement bedDetails = connection.prepareStatement(Query.RECORD_BED_BY_SERIAL_NUMBER)) {
            bedDetails.setString(1, serialNumber);
            ResultSet resultSet = bedDetails.executeQuery();
            return mapResultSetToBedList(resultSet).get(0);

        }
    }

    public List<Bed> mapResultSetToBedList(ResultSet resultSet) throws SQLException {
        List<Bed> bedList = new ArrayList<>();

        while (resultSet.next()) {
            Bed bed = new Bed();
            bed.setBedId(resultSet.getString("bedid"));
            bed.setHospitalId((UUID) resultSet.getObject("hospitalid"));
            bedList.add(bed);
        }
        return bedList;
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

    public void addQueuePatientToHospital(UUID hospitalId) throws FailedToGetException, FailedToAddException, SQLException {
        try {
            List<QueuePatientSerialDAO> firstsInQueue = getFirstsInQueue();

            System.out.println(firstsInQueue.size());
            int count = 1;
            for (QueuePatientSerialDAO q :
                    firstsInQueue) {
                addToBed(q, count, hospitalId);
                count++;
            }
        } catch (FailedToGetException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (FailedToAddException e) {
            logger.error(e.getMessage());
            throw e;
        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public StatsDAO getDailyStatsCountryLevel(Date date) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement newCases = connection.prepareStatement(Query.DAILY_NEW_CASES_COUNTRY_LEVEL);
             PreparedStatement recovered = connection.prepareStatement(Query.DAILY_RECOVERED_COUNTRY_LEVEL);
             PreparedStatement deaths = connection.prepareStatement(Query.DAILY_DEATHS_COUNTRY_LEVEL)) {

            //regdate=?
            newCases.setDate(1, date);

            //dischargeddate=?
            recovered.setDate(1, date);

            //closed=?
            deaths.setDate(1, date);

            return mapToResultSetsToStatsDAO(newCases, recovered, deaths);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }

    }

    public StatsDAO getDailyStatsDistrictLevel(Date date, String district) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement newCases = connection.prepareStatement(Query.DAILY_NEW_CASES_DISTRICT_LEVEL);
             PreparedStatement recovered = connection.prepareStatement(Query.DAILY_RECOVERED_DISTRICT_LEVEL);
             PreparedStatement deaths = connection.prepareStatement(Query.DAILY_DEATHS_DISTRICT_LEVEL)) {

            //district =? AND regdate=?"
            newCases.setString(1, district);
            newCases.setDate(2, date);

            //district =? AND dischargeddate=?
            recovered.setString(1, district);
            recovered.setDate(2, date);

            //district =? AND closed=?
            deaths.setString(1, district);
            deaths.setDate(2, date);


            return mapToResultSetsToStatsDAO(newCases, recovered, deaths);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public StatsDAO getDailyStatsHospitalLevel(Date date, String hospitalId) throws SQLException {
        // todo: distinct patientId serial admittedDate = today , dischargedDat = today patient = district
        // todo:  check patient death = today join record hospitalid = hospital id dischargeDate = null

        try (Connection connection = Database.getConnection();
             PreparedStatement newCases = connection.prepareStatement(Query.DAILY_NEW_CASES_HOSPITAL_LEVEL);
             PreparedStatement recovered = connection.prepareStatement(Query.DAILY_RECOVERED_HOSPITAL_LEVEL);
             PreparedStatement deaths = connection.prepareStatement(Query.DAILY_DEATHS_HOSPITAL_LEVEL)) {


            //regdate=? AND hospitalid=?::uuid
            newCases.setDate(1, date);
            newCases.setString(2, hospitalId);

            //dischargeddate=? AND hospitalid=?::uuid
            recovered.setDate(1, date);
            recovered.setString(2, hospitalId);

            //closed=? AND hospitalid=?::uuid
            deaths.setDate(1, date);
            deaths.setString(2, hospitalId);

            return mapToResultSetsToStatsDAO(newCases, recovered, deaths);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public StatsDAO getOverAllStats() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement totalCases = connection.createStatement();
             Statement totalRecovered = connection.createStatement();
             Statement totalDeaths = connection.createStatement();
        ) {
            ResultSet newCaseResult = totalCases.executeQuery(Query.TOTAL_CASES);
            ResultSet recoveredResult = totalRecovered.executeQuery(Query.TOTAL_RECOVER);
            ResultSet deathResult = totalDeaths.executeQuery(Query.TOTAL_DEATHS);

            StatsDAO statsDAO = new StatsDAO();

            newCaseResult.next();
            //newcases
            statsDAO.setNewCases(newCaseResult.getInt(1));

            //recovered
            recoveredResult.next();
            statsDAO.setRecovered(recoveredResult.getInt(1));
            //deaths
            deathResult.next();
            statsDAO.setDeaths(deathResult.getInt(1));
            return statsDAO;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    private StatsDAO mapToResultSetsToStatsDAO(PreparedStatement newCases, PreparedStatement recovered, PreparedStatement deaths) throws SQLException {
//        System.out.println(deaths);
        ResultSet newCaseResult = newCases.executeQuery();
        ResultSet recoveredResult = recovered.executeQuery();
        ResultSet deathResult = deaths.executeQuery();

        StatsDAO statsDAO = new StatsDAO();

        newCaseResult.next();
        //newcases
        statsDAO.setNewCases(newCaseResult.getInt("newcases"));

        //recovered
        recoveredResult.next();
        statsDAO.setRecovered(recoveredResult.getInt("recovered"));
        //deaths
        deathResult.next();
        statsDAO.setDeaths(deathResult.getInt("deaths"));

        return statsDAO;
    }

    private void addToBed(QueuePatientSerialDAO queuePatientSerialDAO, Integer bedId, UUID hospitalId) throws FailedToAddException {
        try (Connection connection = Database.getConnection();
             PreparedStatement updateRecord = connection.prepareStatement(Query.RECORD_UPDATE);
             PreparedStatement deleteQueueItem = connection.prepareStatement(Query.QUEUE_DELETE);
             PreparedStatement updateBedStatus = connection.prepareStatement(Query.BED_UPDATE_STATUS)) {
            connection.setAutoCommit(false);

            //  hospitalid =? bedid=? WHERE serialNumber=?
            updateRecord.setObject(1, hospitalId);
            updateRecord.setString(2, bedId.toString());
            updateRecord.setObject(3, queuePatientSerialDAO.getSerialNumber());

            deleteQueueItem.setObject(1, queuePatientSerialDAO.getQueueId());

            updateBedStatus.setString(1, String.valueOf(StatusType.unavailable));
            updateBedStatus.setString(2, bedId.toString());
            updateBedStatus.setObject(3, hospitalId);

            updateRecord.execute();
            deleteQueueItem.execute();
            updateBedStatus.execute();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new FailedToAddException(Message.FAILED_TO_ADD_TO_HOSPITAL + (bedId - 1));
        }
    }

    private List<QueuePatientSerialDAO> getFirstsInQueue() throws FailedToGetException {
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(Query.QUEUE_TOP);

            return mapResultSetToQueuePatientSerialDAO(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new FailedToGetException(Message.QUEUE_FAILED);
        }
    }

    private List<QueuePatientSerialDAO> mapResultSetToQueuePatientSerialDAO(ResultSet resultSet) throws SQLException {
        List<QueuePatientSerialDAO> queuePatientSerialDAOS = new ArrayList<>();
        // queueid,serialnumber
        while (resultSet.next()) {
            QueuePatientSerialDAO queuePatientSerialDAO = new QueuePatientSerialDAO();
            queuePatientSerialDAO.setQueueId((UUID) resultSet.getObject("queueid"));
            queuePatientSerialDAO.setSerialNumber((UUID) resultSet.getObject("serialnumber"));
            queuePatientSerialDAOS.add(queuePatientSerialDAO);
        }
        return queuePatientSerialDAOS;
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
            severity.setSeverityId((UUID) resultSet.getObject("severityid"));
            severity.setLevel(SeverityLevel.valueOf(resultSet.getString("level")));
            severity.setDoctorId((UUID) resultSet.getObject("doctorid"));
            severity.setMarkedDate(resultSet.getDate("markeddate"));
            severity.setSeverityId((UUID) resultSet.getObject("serialnumber"));

            severityList.add(severity);
        }
        return severityList;

    }
}
