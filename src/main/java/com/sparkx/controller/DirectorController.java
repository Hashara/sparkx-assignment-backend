package com.sparkx.controller;

import com.sparkx.Exception.NotFoundException;
import com.sparkx.service.RecordService;
import com.sparkx.util.Message;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "Director", value = "/director")
public class DirectorController extends Controller{
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
                case "DISCHARGE_PATIENT":
                    dischargePatient(req, resp);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            sendMessageResponse(e.getMessage(), resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void dischargePatient(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String serialNumber = req.getParameter("serialNumber");

        // todo: check tole = director , serial number discharged date == null and hospital id  = director hospital id
        try {
            recordService.updateDischargeDate(serialNumber);
            sendMessageResponse(Message.RECORD_UPDATED_SUCCESSFULLY, resp, HttpServletResponse.SC_OK);
        } catch (NotFoundException e) {
            logger.info(Message.RECORD_NOT_FOUND);
            sendMessageResponse(Message.RECORD_NOT_FOUND, resp, HttpServletResponse.SC_OK);
        }

    }

    public void destroy() {
    }
}
