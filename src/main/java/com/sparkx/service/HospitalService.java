package com.sparkx.service;

import com.sparkx.Exception.FailedToAddException;
import com.sparkx.Exception.FailedToGetException;
import com.sparkx.Exception.NotCreatedException;
import com.sparkx.Exception.NotFoundException;
import com.sparkx.model.Bed;
import com.sparkx.model.Person;
import com.sparkx.model.Types.RoleType;
import com.sparkx.model.dao.BedStatsDAO;
import com.sparkx.model.dao.DetailedHospitalDAO;
import com.sparkx.model.dao.NewHospitalDAO;
import com.sparkx.model.dao.QueueDetailsDAO;
import com.sparkx.util.Message;
import com.sparkx.util.Query;
import com.sparkx.core.config.Config;
import com.sparkx.core.Database;
import com.sparkx.model.Hospital;
import com.sparkx.model.Types.StatusType;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HospitalService {
    Logger logger = Logger.getLogger(HospitalService.class);

    public void addNewHospital(NewHospitalDAO newHospitalDAO) throws NotCreatedException, SQLException, FailedToAddException, FailedToGetException {
        UUID hospitalId = Util.getUUID();
        newHospitalDAO.getHospital().setHospitalId(hospitalId);
        newHospitalDAO.getDirector().setHospitalId(hospitalId);
        newHospitalDAO.getDirector().setRole(RoleType.Director);
        newHospitalDAO.getDirector().setPassword(Util.hashPassword(newHospitalDAO.getDirector().getPassword()));
        try {
            createHospital(newHospitalDAO.getHospital());
        } catch (NotCreatedException e) {
            logger.error(Message.HOSPITAL_NOT_CREATED);
            throw new NotCreatedException(Message.HOSPITAL_NOT_CREATED);
        }

        try {
            new PersonService().createPerson(newHospitalDAO.getDirector());
        } catch (Exception e) {
            logger.error(Message.PERSON_NOT_CREATED);
            throw new NotCreatedException(Message.PERSON_NOT_CREATED);
        }

        try {
            new RecordService().addQueuePatientToHospital(hospitalId);
        } catch (Exception e) {
            throw e;
        }
    }

    public void createHospital(Hospital hospital) throws NotCreatedException {
        try (Connection connection = Database.getConnection();
             PreparedStatement createHospital = connection.prepareStatement(Query.HOSPITAL_CREATE);
             PreparedStatement createBeds = connection.prepareStatement(Query.BEDS_CREATE)) {


            connection.setAutoCommit(false);
            //(hospitalid, name, district, location_x, location_y)
            createHospital.setObject(1, hospital.getHospitalId());
            createHospital.setString(2, hospital.getName());
            createHospital.setString(3, hospital.getDistrict());
            createHospital.setInt(4, hospital.getLocation_x());
            createHospital.setInt(5, hospital.getLocation_y());

            createHospital.execute();
            for (int i = 0; i < Config.BEDS_PER_HOSPITAL * 2; i++) {
                if ((i + 1) % 2 == 1) {
                    createBeds.setObject(i + 1, hospital.getHospitalId());
                } else {
                    createBeds.setString(i + 1, String.valueOf(StatusType.available));
                }
            }
            createBeds.execute();
            connection.commit();
        } catch (Exception throwables) {
            logger.error(throwables.getMessage());
            throw new NotCreatedException(Message.HOSPITAL_NOT_CREATED);
        }

    }

    public List<Hospital> getAllHospitals() {
        List<Hospital> hospitalList = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(Query.HOSPITAL_ALL);

            hospitalList = mapResultSetToHospitalList(resultSet);

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throwables.printStackTrace();
        }
        return hospitalList;
    }

    public List<Hospital> getHospitalByDistrict(String district) {
        List<Hospital> hospitalList = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.HOSPITAL_BY_DISTRICT)) {

            statement.setString(1, district);
            ResultSet resultSet = statement.executeQuery();

            hospitalList = mapResultSetToHospitalList(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return hospitalList;

    }

    public DetailedHospitalDAO getHospitalByID(String hospitalId) throws Exception {
        DetailedHospitalDAO detailedHospitalDAO = null;
        try (Connection connection = Database.getConnection();
             PreparedStatement hospitalById = connection.prepareStatement(Query.HOSPITAL_BY_ID);
             PreparedStatement director = connection.prepareStatement(Query.PERSON_BY_ROLE_HOSPITAL);
             PreparedStatement beds = connection.prepareStatement(Query.BED_BY_HOSPITAL_ID)
        ) {
            hospitalById.setString(1, hospitalId);

            director.setString(1, String.valueOf(RoleType.Director));
            director.setString(2, hospitalId);

            beds.setString(1, hospitalId);

            ResultSet hospitalResult = hospitalById.executeQuery();
            ResultSet directorResult = director.executeQuery();
            ResultSet bedsResult = beds.executeQuery();

            Hospital hospital = mapResultSetToHospitalList(hospitalResult).get(0);
            Person person = new PersonService().mapResultSetToPerson(directorResult).get(0);
            List<Bed> bedList = mapResultSetToBedList(bedsResult);

            detailedHospitalDAO = new DetailedHospitalDAO();
            detailedHospitalDAO.setHospital(hospital);
            detailedHospitalDAO.setDirector(person);
            detailedHospitalDAO.setBeds(bedList);

            return detailedHospitalDAO;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    public List<BedStatsDAO> getBedStatsWithHospitalDetails() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(Query.GET_BED_STATUS_WITH_HOSPITAL);
            return mapResultSetToBedStatsDAOList(resultSet);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public Bed getNearestHospitalBed(int locationX, int locationY) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.BED_NEAREST)) {
            List<Bed> bedList;

            statement.setInt(1, locationX);
            statement.setInt(2, locationY);
            ResultSet resultSet = statement.executeQuery();
            bedList = mapResultSetToBedList(resultSet);
            return bedList.get(0);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
        } catch (IndexOutOfBoundsException e) {
            logger.info(Message.NO_BEDS_AVAILABLE);
        }
        return null;
    }

    public int getQueueNumberByQueueId(UUID queueID) throws Exception {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.QUEUE_NO_BY_ID)) {

            statement.setObject(1, queueID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt("queue_number");
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        }
        throw new NotFoundException(Message.QUEUE_NOT_FOUND);
    }


    public QueueDetailsDAO getQueueDetails() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement getDistrict = connection.createStatement();
             Statement getLength = connection.createStatement();) {
            ResultSet resultSetDistrict = getDistrict.executeQuery(Query.QUEUE_NEED_HOSPITAL);
            resultSetDistrict.next();
            QueueDetailsDAO queueDetailsDAO = new QueueDetailsDAO();

            ResultSet resultSetLength = getLength.executeQuery(Query.QUEUE_LENGTH);
            resultSetLength.next();
            queueDetailsDAO.setLength(resultSetLength.getInt("length"));

            if (queueDetailsDAO.getLength() > 0) {
                queueDetailsDAO.setDistrict(resultSetDistrict.getString("maxdis"));
            }
            return queueDetailsDAO;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    private List<BedStatsDAO> mapResultSetToBedStatsDAOList(ResultSet resultSet) throws SQLException {
        List<BedStatsDAO> bedStatsDAOS = new ArrayList<>();

        while (resultSet.next()) {
            BedStatsDAO bedStatsDAO = new BedStatsDAO();

            Hospital hospital = new Hospital();
            hospital.setHospitalId((UUID) resultSet.getObject("hospitalid"));
            hospital.setDistrict(resultSet.getString("name"));
            hospital.setLocation_x(resultSet.getInt("location_x"));
            hospital.setLocation_y(resultSet.getInt("location_y"));
            hospital.setName(resultSet.getString("name"));

            bedStatsDAO.setHospital(hospital);
            bedStatsDAO.setAvailable(resultSet.getInt("available"));
            bedStatsDAO.setUnavailable(resultSet.getInt("unavailable"));

            bedStatsDAOS.add(bedStatsDAO);
        }
        return bedStatsDAOS;
    }

    private List<Hospital> mapResultSetToHospitalList(ResultSet resultSet) throws SQLException {
        List<Hospital> hospitalList = new ArrayList<>();

        while (resultSet.next()) {
            Hospital h = new Hospital();
            h.setHospitalId((UUID) resultSet.getObject("hospitalid"));
            h.setName(resultSet.getString("name"));
            h.setDistrict(resultSet.getString("district"));
            h.setLocation_x(resultSet.getInt("location_x"));
            h.setLocation_y(resultSet.getInt("location_y"));

            hospitalList.add(h);
        }
        return hospitalList;
    }

    private List<Bed> mapResultSetToBedList(ResultSet resultSet) throws SQLException {
        List<Bed> bedList = new ArrayList<>();

        while (resultSet.next()) {
            Bed bed = new Bed();
            bed.setBedId(resultSet.getString("bedid"));
            bed.setHospitalId((UUID) resultSet.getObject("hospitalid"));
            bed.setStatus(StatusType.valueOf(resultSet.getString("status")));
            bedList.add(bed);
        }
        return bedList;
    }

}
