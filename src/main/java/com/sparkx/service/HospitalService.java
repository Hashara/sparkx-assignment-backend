package com.sparkx.service;

import com.sparkx.Exception.NotFoundException;
import com.sparkx.model.Bed;
import com.sparkx.model.Queue;
import com.sparkx.util.Message;
import com.sparkx.util.Query;
import com.sparkx.config.Config;
import com.sparkx.core.Database;
import com.sparkx.model.Hospital;
import com.sparkx.model.Types.StatusType;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HospitalService {
    Logger logger = Logger.getLogger(HospitalService.class);

    public boolean createHospital(Hospital hospital) {
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
            return true;
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            return false;
        }

    }

    public List<Hospital>  getAllHospitals() {
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

    public List<Hospital> getHospitalByDistrict(String district){
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

    public Bed getNearestHospitalBed(int locationX, int locationY){
        try(Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(Query.BED_NEAREST)) {
            List<Bed> bedList;

            statement.setInt(1,locationX);
            statement.setInt(2,locationY);
            ResultSet resultSet = statement.executeQuery();
            bedList = mapResultSetToBedList(resultSet);
            return bedList.get(0);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
        }catch (IndexOutOfBoundsException e){
            logger.info(Message.NO_BEDS_AVAILABLE);
        }
        return null;
    }

    public int getQueueNumberByQueueId(UUID queueID) throws Exception {
        try(Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(Query.QUEUE_NO_BY_ID)) {

            statement.setObject(1,queueID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                return resultSet.getInt("queue_number");
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        }
        throw new NotFoundException(Message.QUEUE_NOT_FOUND);
    }


    private List<Hospital> mapResultSetToHospitalList(ResultSet resultSet) throws SQLException {
        List<Hospital> hospitalList = new ArrayList<>();

        while ( resultSet.next() ) {
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

        while (resultSet.next()){
            Bed bed = new Bed();
            bed.setBedId(resultSet.getString("bedid"));
            bed.setHospitalId((UUID) resultSet.getObject("hospitalid"));
            bed.setStatus(StatusType.valueOf(resultSet.getString("status")));
            bedList.add(bed);
            System.out.println(bed);
        }
        return bedList;
    }

}
