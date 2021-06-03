package com.sparkx.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sparkx.Exception.NotCreatedException;
import com.sparkx.model.Patient;
import com.sparkx.model.Person;
import com.sparkx.model.dao.PatientRecordDAO;
import com.sparkx.service.PatientService;
import com.sparkx.service.PersonService;
import com.sparkx.util.Message;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PublicServlet", value = "/public")
public class PublicController extends Controller {
    private Logger logger;
    private PersonService personService;
    private PatientService patientService;

    public void init() {
        logger = Logger.getLogger(DoctorController.class);
        personService = new PersonService();
        patientService = new PatientService();
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
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

    // todo: GET - get all details, by date
    public void destroy() {
    }
}
