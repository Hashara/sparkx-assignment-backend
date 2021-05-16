package com.sparkx.service;

import com.sparkx.config.Config;
import com.sparkx.core.Database;
import com.sparkx.model.Hospital;
import com.sparkx.model.Types.StatusType;

import java.sql.*;

public class HospitalService {

    public boolean createHospital(Hospital hospital) {
        try (Connection connection = Database.getConnection();
             PreparedStatement createHospital = connection.prepareStatement(Query.HOSPITAL_CREATE);
             PreparedStatement createBeds = connection.prepareStatement(Query.BEDS_CREATE)) {


            connection.setAutoCommit(false);
            //(hospitalid, name, district, location_x, location_y)
            createHospital.setString(1, hospital.getHospitalId());
            createHospital.setString(2, hospital.getName());
            createHospital.setString(3, hospital.getDistrict());
            createHospital.setInt(4, hospital.getLocation_x());
            createHospital.setInt(5, hospital.getLocation_y());

            createHospital.execute();
            for (int i = 0; i < Config.BEDS_PER_HOSPITAL * 2; i++) {
                if ((i + 1) % 2 == 1) {
                    createBeds.setString(i + 1, hospital.getHospitalId());
                } else {
                    createBeds.setString(i + 1, String.valueOf(StatusType.available));
                }
            }
            createBeds.execute();
            connection.commit();
            return true;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throwables.printStackTrace();
            return false;
        }

    }

    public void getAllHospitals() {
        // todo: covert to json and add return
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(Query.HOSPITAL_ALL);

            System.out.println(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
