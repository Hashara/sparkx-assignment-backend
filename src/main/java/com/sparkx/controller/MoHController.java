package com.sparkx.controller;

import com.google.gson.Gson;
import com.sparkx.Exception.FailedToAddException;
import com.sparkx.Exception.FailedToGetException;
import com.sparkx.Exception.InvalidInputException;
import com.sparkx.Exception.NotCreatedException;
import com.sparkx.model.dao.NewHospitalDAO;
import com.sparkx.model.dao.QueueDetailsDAO;
import com.sparkx.service.HospitalService;
import com.sparkx.service.PersonService;
import com.sparkx.service.RecordService;
import com.sparkx.util.Message;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "MoHServlet", value = "/moh")
public class MoHController extends Controller {

    private HospitalService hospitalService;
    private RecordService recordService;
    private PersonService personService;
    private Logger logger;


    public void init() {
        hospitalService = new HospitalService();
        recordService = new RecordService();
        personService = new PersonService();
        logger = Logger.getLogger(MoHController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String Command = req.getParameter("cmd");

            switch (Command) {
                case "QUEUE_DETAILS":
                    getQueueDetails(req, resp);
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
            String Command = req.getParameter("cmd");

            switch (Command) {
                case "ADD_HOSPITAL":
                    addHospital(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void addHospital(HttpServletRequest req, HttpServletResponse resp) throws InvalidInputException, NotCreatedException, SQLException, FailedToAddException, FailedToGetException {
        String jsonResponse;
        try {
            jsonResponse = getjsonRequest(req);
        } catch (IOException e) {
            logger.error(Message.INVALID_INPUT);
            throw new InvalidInputException(Message.INVALID_INPUT);
        }
        Gson gson = new Gson();
        NewHospitalDAO newHospitalDAO = gson.fromJson(jsonResponse, NewHospitalDAO.class);
        hospitalService.addNewHospital(newHospitalDAO);
        sendMessageResponse(Message.HOSPITAL_SUCCESS, resp, HttpServletResponse.SC_CREATED);
    }


    private void getQueueDetails(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            QueueDetailsDAO queueDetailsDAO = hospitalService.getQueueDetails();
            Gson gson = new Gson();
            sendResponse(gson.toJson(queueDetailsDAO), resp, HttpServletResponse.SC_OK);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            sendMessageResponse(throwables.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
