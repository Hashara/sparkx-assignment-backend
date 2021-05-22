package com.sparkx.controller;

import com.sparkx.service.PatientService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "PatientServlet", value = "/patient")
public class PatientController extends HttpServlet {
    private PatientService service;
    private Logger logger;

    public void init(){
        service = new PatientService();
        logger = Logger.getLogger(PatientController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.error("hi");
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1> This is the hospital servlet </h1>");
        out.println("</body></html>");
    }

    public void destroy(){

    }
}
