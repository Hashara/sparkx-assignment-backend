package com.sparkx.controller;

import com.google.gson.Gson;
import com.sparkx.model.Severity;
import com.sparkx.service.RecordService;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DoctorServlet", value = "/doctor")
public class DoctorController extends Controller {
    private RecordService recordService;
    private Logger logger;

    public void init() {
        recordService = new RecordService();
        logger = Logger.getLogger(DoctorController.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cmd = req.getParameter("cmd");

            switch (cmd) {
                case "MARK_SEVERITY":
                    markSeverity(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    private void markSeverity(HttpServletRequest req, HttpServletResponse resp) {
        String jsonResponse = null;
        // todo: check hospital id of record = doctor's hospital id
        try {
            jsonResponse = getjsonRequest(req);
            Gson gson = new Gson();
            Severity severity = gson.fromJson(jsonResponse, Severity.class);
            severity =  recordService.markSeverity(severity);
            sendResponse(gson.toJson(severity), resp, HttpServletResponse.SC_CREATED);
        } catch (IOException | SQLException e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }

    public void destroy() {
    }
}
