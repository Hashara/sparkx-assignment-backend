package com.sparkx.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sparkx.Exception.NotCreatedException;
import com.sparkx.model.Hospital;
import com.sparkx.model.Patient;
import com.sparkx.model.Person;
import com.sparkx.model.dao.AuthDAO;
import com.sparkx.model.dao.PatientRecordDAO;
import com.sparkx.model.dao.StatsDAO;
import com.sparkx.service.*;
import com.sparkx.util.Message;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@WebServlet(name = "PublicServlet", value = "/public")
public class PublicController extends Controller {
    private Logger logger;
    private PersonService personService;
    private PatientService patientService;
    private RecordService recordService;
    private AuthService authService;
    public HospitalService hospitalService;

    public void init() {
        logger = Logger.getLogger(DoctorController.class);
        personService = new PersonService();
        patientService = new PatientService();
        recordService = new RecordService();
        authService = new AuthService();
        hospitalService = new HospitalService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cmd = req.getParameter("cmd");

            switch (cmd) {
                case "REGISTER":
                    registerPerson(req, resp);
                    break;
                case "PATIENT_REGISTER":
                    registerPatient(req, resp);
                    break;
                case "LOGIN":
                    signIn(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cmd = req.getParameter("cmd");

            switch (cmd) {
                case "GET_STATUS":
                    getStatus(req, resp);
                    break;
                case "DAILY_COUNTRY_LEVEL":
                    dailyCountryLevel(req, resp);
                    break;
                case "DAILY_DISTRICT_LEVEL":
                    dailyDistrictLevel(req, resp);
                    break;
                case "DAILY_HOSPITAL_LEVEL":
                    dailyHospitalLevel(req, resp);
                    break;
                case "OVERALL_STATUS":
                    overallStatus(req, resp);
                    break;
                case "GET_ALL_HOSPITALS":
                    getAllHospitals(req, resp);
                    break;
                case "GET_ALL_DISTRICTS":
                    getAllDistricts(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    private void getAllDistricts(HttpServletRequest req, HttpServletResponse resp) {
        List<String> districtList = patientService.getAllDistricts();
        Gson gson = new Gson();
        sendResponse(gson.toJson(districtList), resp, HttpServletResponse.SC_OK);
    }

    private void getAllHospitals(HttpServletRequest req, HttpServletResponse resp) {
        List<Hospital> hospitalList = hospitalService.getAllHospitals();
        Gson gson = new Gson();
        sendResponse(gson.toJson(hospitalList), resp, HttpServletResponse.SC_OK);
    }

    private void getStatus(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String level = req.getParameter("level");

        switch (level) {
            case "OVERALL":
                overallStatus(req, resp);
                break;
            case "COUNTRY":
                dailyCountryLevel(req, resp);
                break;
            case "DISTRICT":
                dailyDistrictLevel(req, resp);
                break;
            case "HOSPITAL":
                dailyHospitalLevel(req, resp);
                break;
            default:
                sendMessageResponse(Message.INVALID_INPUT, resp, HttpServletResponse.SC_BAD_REQUEST);
                break;

        }

    }

    private void overallStatus(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        StatsDAO statsDAO = recordService.getOverAllStats();
        Gson gson = new Gson();
        sendResponse(gson.toJson(statsDAO), resp, HttpServletResponse.SC_OK);
    }

    private void dailyHospitalLevel(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String strDate = req.getParameter("date");
        Date date = null;
        try {
            date = Util.covertStringToDate(strDate);
        } catch (ParseException e) {
            logger.error(Message.WRONG_DATE_FORMAT);
            sendMessageResponse(Message.WRONG_DATE_FORMAT, resp, HttpServletResponse.SC_BAD_REQUEST);
        }
        String hospitalId = req.getParameter("hospitalId");

        try {
            StatsDAO statsDAO = recordService.getDailyStatsHospitalLevel(date, hospitalId);
            Gson gson = new Gson();
            sendResponse(gson.toJson(statsDAO), resp, HttpServletResponse.SC_OK);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    private void dailyDistrictLevel(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String strDate = req.getParameter("date");
        Date date = null;
        try {
            date = Util.covertStringToDate(strDate);
        } catch (ParseException e) {
            logger.error(Message.WRONG_DATE_FORMAT);
            sendMessageResponse(Message.WRONG_DATE_FORMAT, resp, HttpServletResponse.SC_BAD_REQUEST);
        }

        String district = req.getParameter("district");

        try {
            StatsDAO statsDAO = recordService.getDailyStatsDistrictLevel(date, district);
            Gson gson = new Gson();
            sendResponse(gson.toJson(statsDAO), resp, HttpServletResponse.SC_OK);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw throwables;
        }

    }

    private void dailyCountryLevel(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String strDate = req.getParameter("date");
        Date date = null;
        try {
            date = Util.covertStringToDate(strDate);
        } catch (ParseException e) {
            logger.error(Message.WRONG_DATE_FORMAT);
            sendMessageResponse(Message.WRONG_DATE_FORMAT, resp, HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            StatsDAO statsDAO = recordService.getDailyStatsCountryLevel(date);
            Gson gson = new Gson();
            sendResponse(gson.toJson(statsDAO), resp, HttpServletResponse.SC_OK);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    private void registerPerson(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String jsonResponse = getjsonRequest(req);
        Gson gson = new Gson();
        Person person = gson.fromJson(jsonResponse, Person.class);

        person.setPassword(Util.hashPassword(person.getPassword()));

        // todo: check role != patient, {role=hosptal staff|director|doctor hosptalId != null}, {role = MoH and hospitalId ==null
        person = personService.createPerson(person);
        sendResponse(gson.toJson(person), resp, HttpServletResponse.SC_CREATED);

    }

    private void registerPatient(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String jsonResponse = getjsonRequest(req);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-mm-dd").create();
        Patient patient = gson.fromJson(jsonResponse, Patient.class);

        patient.setPassword(Util.hashPassword(patient.getPassword()));

        try {
            PatientRecordDAO patientDao = patientService.addPatient(patient);
            sendResponse(gson.toJson(patientDao), resp, HttpServletResponse.SC_CREATED);

        } catch (NotCreatedException e) {
            sendMessageResponse(Message.REGISTER_FAILED, resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    private void signIn(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String jsonResponse = getjsonRequest(req);
        Gson gson = new Gson();
        Person person = gson.fromJson(jsonResponse, Person.class);

        AuthDAO authDAO = null;
        try {
            authDAO = authService.authenticate(person.getEmail(), person.getPassword());

            if (authDAO == null) {
                sendMessageResponse(Message.INVALID_CREDENTIALS, resp, HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                sendResponse(gson.toJson(authDAO), resp, HttpServletResponse.SC_OK);
            }
        } catch (IOException e) {
            sendMessageResponse(Message.INVALID_INPUT, resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    public void destroy() {
    }
}
