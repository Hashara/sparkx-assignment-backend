package com.sparkx.controller;

import com.google.gson.JsonObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Controller extends HttpServlet {

    public void sendResponse(String data, HttpServletResponse resp)  {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject json = new JsonObject();
        json.addProperty("Data", data);
        writer.print(json.toString());
        writer.flush();
    }

    public String getjsonRequest(HttpServletRequest req) throws IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;

        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null)
            jb.append(line);

        return jb.toString();
    }

    public void response(String message, int code, HttpServletResponse resp)  {
        resp.setStatus(code);
        sendResponse("{'message':" + message + "}",resp);
    }
}
