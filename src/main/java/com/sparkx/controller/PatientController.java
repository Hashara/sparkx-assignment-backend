package com.sparkx.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sparkx.model.Patient;
import com.sparkx.service.PatientService;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


@WebServlet(name = "PatientServlet", value = "/patient")
public class PatientController extends Controller {
    private PatientService patientService;
    private Logger logger;

    public void init(){
        patientService = new PatientService();
        logger = Logger.getLogger(PatientController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1> This is the hospital servlet </h1>");
        out.println("</body></html>");
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


    private void registerPatient(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String jsonResponse = getjsonRequest(req);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-mm-dd").create();
        Patient patient = gson.fromJson(jsonResponse, Patient.class);

        patient.setPatientId(Util.hashPassword(patient.getPassword()));
        patientService.addPatient(patient);

        response(Messages.REGISTER_SUCCESS, HttpServletResponse.SC_CREATED, resp);
    }

    public void destroy(){

    }
}
