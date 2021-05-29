package com.sparkx.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.Nullable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Controller extends HttpServlet {

    public void sendResponse(String data, HttpServletResponse resp, @Nullable Integer code) {
        if (code != null) {
            resp.setStatus(code);
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(data);
        writer.print(jsonElement.toString());
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

    public JsonObject getJsonObject(HttpServletRequest req) throws IOException {
        String jsonResponse = getjsonRequest(req);
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonResponse);
        return jsonElement.getAsJsonObject();
    }

    public void sendMessageResponse(String message, HttpServletResponse resp, @Nullable int code) {
        sendResponse("{\"message\": \"" + message + "\"}", resp, code);
    }
}
