package com.sparkx.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sparkx.model.Person;
import com.sparkx.service.PersonService;
import com.sparkx.util.Util;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

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
                case "REGISTER":
                    registerPerson(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void registerPerson(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String jsonResponse = getjsonRequest(req);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-mm-dd").create();
        Person person = gson.fromJson(jsonResponse, Person.class);

        person.setPassword(Util.hashPassword(person.getPassword()));

        // todo: check role != patient, {role=hosptal staff|director|doctor hosptalId != null}, {role = MoH and hospitalId ==null
        person = personService.createPerson(person);
        sendResponse(gson.toJson(person), resp, HttpServletResponse.SC_CREATED);

    }

    public void destroy() {
    }
}
