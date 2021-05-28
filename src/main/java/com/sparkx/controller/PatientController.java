package com.sparkx.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sparkx.Exception.NotCreatedException;
import com.sparkx.Exception.NotFoundException;
import com.sparkx.model.Patient;
import com.sparkx.service.PatientService;
import com.sparkx.util.Message;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "PatientServlet", value = "/patient")
public class PatientController extends Controller {
    private PatientService patientService;
    private Logger logger;

    public void init(){
        patientService = new PatientService();
        logger = Logger.getLogger(PatientController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String cmd = req.getParameter("cmd");

            switch(cmd){
                case "PATIENT_BY_ID":
                    getPatientById(req,resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response( e.getMessage() ,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  {
        try {
            String cmd = req.getParameter("cmd");

            switch(cmd){
                case "REGISTER":
                    registerPatient(req,resp);
                    break;
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage());
            response( e.getMessage() ,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
        }
    }


    private void registerPatient(HttpServletRequest req, HttpServletResponse resp) throws IOException,  NotCreatedException {
        String jsonResponse = getjsonRequest(req);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-mm-dd").create();
        Patient patient = gson.fromJson(jsonResponse, Patient.class);

        patient.setPassword(Util.hashPassword(patient.getPassword()));
        patientService.addPatient(patient);

        response(Message.REGISTER_SUCCESS, HttpServletResponse.SC_CREATED, resp);
    }

    private void getPatientById (HttpServletRequest req, HttpServletResponse resp) throws IOException, NotFoundException {
        String patientId = req.getParameter("id");
        Patient patient = patientService.getPatientById(patientId);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-mm-dd").create();

        sendResponse(gson.toJson(patient), resp);
    }

    public void destroy(){

    }
}
