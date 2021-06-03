package com.sparkx.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sparkx.Exception.NotCreatedException;
import com.sparkx.Exception.NotFoundException;
import com.sparkx.model.dao.PatientRecordDAO;
import com.sparkx.model.dao.RecordDAO;
import com.sparkx.model.Patient;
import com.sparkx.model.Record;
import com.sparkx.service.PatientService;
import com.sparkx.service.RecordService;
import com.sparkx.util.Message;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "PatientServlet", value = "/patient")
public class PatientController extends Controller {
    private PatientService patientService;
    private RecordService recordService;
    private Logger logger;

    public void init() {
        patientService = new PatientService();
        logger = Logger.getLogger(PatientController.class);
        recordService = new RecordService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String cmd = req.getParameter("cmd");

            switch (cmd) {
                case "PATIENT_BY_ID":
                    getPatientById(req, resp);
                    break;
                case "GET_ALL_RECORDS":
                    getAllRecords(req, resp);
                    break;
                case "GET_ACTIVE_RECORD":
                    getActiveRecord(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cmd = req.getParameter("cmd");

            switch (cmd) {
                case "PATIENT_RECORD":
                    createRecord(req, resp);
                    break;
                case "PATIENTS_BY_HOSPITAL":
                    getPatientsByHospital(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    private void getPatientById(HttpServletRequest req, HttpServletResponse resp) throws IOException, NotFoundException {
        // todo: check id = user id or hospitalid = current record id
        String patientId = req.getParameter("id");
        Patient patient = patientService.getPatientById(patientId);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();

        sendResponse(gson.toJson(patient), resp, HttpServletResponse.SC_OK);
    }

    private void createRecord(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //  todo: check id = userid
        String patientId = req.getParameter("id");
        Patient patient = patientService.getPatientById(patientId);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();

        try {
            RecordDAO record = recordService.getActiveRecordByPatientID(patientId);
            sendResponse(gson.toJson(record), resp, HttpServletResponse.SC_OK);
        } catch (NotFoundException e) {
            Record record = patientService.addRecord(patient);
            sendResponse(gson.toJson(record), resp, HttpServletResponse.SC_CREATED);
        }
    }

    private void getAllRecords(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // todo: check id = user id or tole = hospital role
        String patientId = req.getParameter("id");
        Patient patient = patientService.getPatientById(patientId);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();

        List<RecordDAO> recordList = recordService.getRecordsByPatientID(patientId);
        sendResponse(gson.toJson(recordList), resp, HttpServletResponse.SC_OK);
    }

    private void getActiveRecord(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String patientId = req.getParameter("id");
        Patient patient = patientService.getPatientById(patientId);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();

        RecordDAO record = recordService.getActiveRecordByPatientID(patientId);
        sendResponse(gson.toJson(record), resp, HttpServletResponse.SC_OK);
    }

    private void getPatientsByHospital(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String hospitalId = req.getParameter("id");
        // todo: check role of user = hospital staff|doctor|director and hospital id of user = hospital id

        List<Patient> patientList = patientService.getPatientsByHospitalId(hospitalId);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();

        sendResponse(gson.toJson(patientList), resp, HttpServletResponse.SC_OK);
    }

    public void destroy() {
    }
}
