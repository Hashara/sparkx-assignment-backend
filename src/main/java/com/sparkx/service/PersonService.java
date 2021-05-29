package com.sparkx.service;

import com.sparkx.util.Query;
import com.sparkx.core.Database;
import com.sparkx.model.Person;
import com.sparkx.model.Types.RoleType;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonService {
    Logger logger = Logger.getLogger(PersonService.class);

    public Person createPerson(Person person) throws Exception {
        try (Connection connection = Database.getConnection();
             PreparedStatement createPerson = connection.prepareStatement(Query.PERSON_CREATE)) {
            person.setUserId(UUID.randomUUID());
            //userid, email, password, first_name, last_name, hospitalid, role
            createPerson.setObject(1, person.getUserId());
            createPerson.setString(2, person.getEmail());
            createPerson.setString(3, person.getPassword());
            createPerson.setString(4, person.getFirst_name());
            createPerson.setString(5, person.getLast_name());
            createPerson.setObject(6, person.getHospitalId());
            createPerson.setString(7, String.valueOf(person.getRole()));

            createPerson.execute();
            person.setPassword(null);
            return person;
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
            throw throwables;
        }

    }

    public Person getUserById(String userId) {
        Person person = null;
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query.PERSON_BY_ID)) {
            preparedStatement.setString(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            person = mapResultSetToPerson(resultSet).get(0);
            return person;
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
        }
        return person;
    }

    public List<Person> getUsersByRole(RoleType roleType) {
        List<Person> person = null;
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Query.PERSON_BY_ROLE)) {

            preparedStatement.setString(1, roleType.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            person = mapResultSetToPerson(resultSet);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage());
        }
        return person;
    }

    //todo: password compare

    private List<Person> mapResultSetToPerson(ResultSet resultSet) throws SQLException {
        List<Person> personList = null;
        while (resultSet.next()) {
            if (personList == null) {
                personList = new ArrayList<>();
            }
            Person person = new Person();
            person.setUserId((UUID) resultSet.getObject("userid"));
            person.setEmail(resultSet.getString("email"));
            person.setFirst_name(resultSet.getString("first_name"));
            person.setLast_name(resultSet.getString("last_name"));
            person.setHospitalId((UUID) resultSet.getObject("hospitalid"));
            person.setRole(RoleType.valueOf(resultSet.getString("role")));
            personList.add(person);
        }
        return personList;
    }
}
