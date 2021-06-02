package com.sparkx.controller;

import com.google.gson.Gson;
import com.sparkx.model.dao.QueueDetailsDAO;
import com.sparkx.service.HospitalService;
import com.sparkx.service.RecordService;
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
    private Logger logger;


    public void init() {
        hospitalService = new HospitalService();
        recordService = new RecordService();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    private void addHospital(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String jsonResponse = getjsonRequest(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
    }

    // todo - POST - add hospital
    private void getQueueDetails(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // todo: check role = MOH
        try {
            QueueDetailsDAO queueDetailsDAO = hospitalService.getQueueDetails();
//            if (queueDetailsDAO.getLength() == 0){
            Gson gson = new Gson();
            sendResponse(gson.toJson(queueDetailsDAO), resp, HttpServletResponse.SC_OK);
//            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            sendMessageResponse(throwables.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
