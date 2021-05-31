package com.sparkx.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sparkx.Exception.NotFoundException;
import com.sparkx.model.Record;
import com.sparkx.service.RecordService;
import com.sparkx.util.Message;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HospitalStaffServlet", value = "/staff")
public class HospitalStaffController extends Controller {
    private RecordService recordService;
    private Logger logger;

    public void init() {
        recordService = new RecordService();
        logger = Logger.getLogger(DoctorController.class);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cmd = req.getParameter("cmd");

            switch (cmd) {
                case "UPDATE_ADMIT_DATE":
                    updateAdmitDateOfRecord(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void updateAdmitDateOfRecord(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String serialNumber = req.getParameter("serialNumber");
        // todo: check role = hospitalStaff and hospitalId equal?? and check admitted date == null
        try {
            recordService.updateAdmitDate(serialNumber);
            sendMessageResponse(Message.RECORD_UPDATED_SUCCESSFULLY, resp, HttpServletResponse.SC_OK);
        } catch (NotFoundException e) {
            logger.info(Message.RECORD_NOT_FOUND);
            sendMessageResponse(Message.RECORD_NOT_FOUND, resp, HttpServletResponse.SC_OK);
        }


    }

    public void destroy() {
    }
}
