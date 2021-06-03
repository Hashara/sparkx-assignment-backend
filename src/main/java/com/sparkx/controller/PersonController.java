package com.sparkx.controller;

import com.google.gson.Gson;
import com.sparkx.model.Person;
import com.sparkx.service.PersonService;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "PersonServlet", value = "/person")
public class PersonController extends Controller {
    private Logger logger;
    private PersonService personService;

    public void init() {
        logger = Logger.getLogger(PersonController.class);
        personService = new PersonService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cmd = req.getParameter("cmd");

            switch (cmd) {
//                case "REGISTER":
//                    registerPerson(req, resp);
//                    break;
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
                case "PATIENT_BY_ID":
                    getPersonById(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void getPersonById(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String personId = req.getParameter("id");
        Person person = personService.getPersonById(personId);

        // todo: check person role == patient and id= patientId | role = staff|doctor|director
        Gson gson = new Gson();
        sendResponse(gson.toJson(person), resp, HttpServletResponse.SC_OK);

    }

    public void destroy() {
    }
}
