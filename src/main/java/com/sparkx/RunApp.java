package com.sparkx;

import com.google.gson.Gson;
import com.sparkx.Exception.FailedToAddException;
import com.sparkx.Exception.FailedToGetException;
import com.sparkx.Exception.NotFoundException;
import com.sparkx.Exception.UnauthorizedException;
import com.sparkx.model.Patient;
import com.sparkx.model.Person;
import com.sparkx.model.Severity;
import com.sparkx.model.Types.RoleType;
import com.sparkx.model.dao.AuthDAO;
import com.sparkx.model.dao.QueueDetailsDAO;
import com.sparkx.model.dao.StatsDAO;
import com.sparkx.service.*;
import com.sparkx.util.DataInsert;
import com.sparkx.util.Util;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;


public class RunApp {


    public static void main(String[] args) {

//        Hospital h = new Hospital();
//        h.setName("Hospital4");
//        h.setDistrict("District4");
//        h.setHospitalId("114");
//        h.setLocation_x(5);
//        h.setLocation_y(16);
//
//        new HospitalService().createHospital(h);

//        List<Hospital> hl = new HospitalService().getAllHospitals();
//        for (Hospital h:
//             hl) {
//            System.out.println(h.toString());
//        }


//        Person person = new Person();
//        person.setUserId("128");
//        person.setHospitalId("111");
//        person.setFirst_name("te7");
//        person.setLast_name("St7");
//        person.setEmail("test7@test.com");
//        person.setPassword(new Util().hashPassword("hello1"));
//        person.setRole(RoleType.HospitalStaff);
//
//        System.out.println(new PersonService().createPerson(person));

//        Person p =new PersonService().getUserById("124");
//        System.out.println(p);
/*
        List<Person> p = new PersonService().getUsersByRole(RoleType.MoH);
        for (Person per:
             p) {
            System.out.println(per);
        }*/


//        Person person = new Person();
//        person.setUserId("130");
//        person.setFirst_name("patient");
//        person.setLast_name("St7");
//        person.setEmail("testPatien@test.com");
//        person.setPassword(new Util().hashPassword("hello1"));
//        person.setRole(RoleType.Patient);
//
//        Patient p = new Patient();
//        p.setPatientId("130");
//        p.setBirthDate(new Date(2001-12-20));
//        p.setContact("0712354584");
//        p.setGender(GenderType.female);
//        p.setDistrict("District1");
//        p.setLocation_x(7);
//        p.setLocation_y(9);
//
//        new PatientService().addPatient(person,p);


//        new HospitalService().getNearestHospitalBed(16,3);
//        new HospitalService().getQueue();
//        new DataInsert().insert();
//
//        try {
//            System.out.println(new PatientService().getPatientById("038d145b-70e1-45c4-9415-211074117f6c"));
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//        }

//        for (int i = 0; i < 20 ; i++) {
//            PatientService patientService = new PatientService();
//            Patient patient = null;
//            try {
//                patient = patientService.getPatientById("247d37a7-ac72-4063-a157-b790f39de283");
//            } catch (NotFoundException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                Record record = patientService.addRecord(patient);
//                System.out.println(record);
//                System.out.println(record.getQueueNumber());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

//        try {
//            System.out.println(new HospitalService().getQueueNumberByQueueId(UUID.fromString("53c28109-baab-4cab-b0d6-cbf45363198c")));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        System.out.println(new RecordService().getRecordsByPatientID("247d37a7-ac72-4063-a157-b790f39de283"));

//    String patientId = "247d37a7-ac72-4063-a157-b790f39de283";
//        try {
//            Patient patient = new PatientService().getPatientById(patientId);
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd").create();

//        try {
//            List<RecordDAO> recordList = new RecordService().getRecordsByPatientID("b9b05317-33c6-4c5b-befd-d25b52f1b417");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(gson.toJson(recordList));
//        JsonParser parser = new JsonParser();
//        JsonElement jsonElement = parser.parse(gson.toJson(recordList));
//        JsonArray json = jsonElement.getAsJsonArray();
//        System.out.println(jsonElement);

/*
        String jsonResponse ="{" +
                "    \"first_name\": \"MoH\",\n" +
                "    \"last_name\": \"One\",\n" +
                "    \"email\":\"moh1@gmail.com\",\n" +
                "    \"password\":\"123456\",\n" +
                "    \"role\":\"MoH\"\n" +
                "}";
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-mm-dd").create();
        Person person = gson.fromJson(jsonResponse, Person.class);

        person.setPassword(Util.hashPassword(person.getPassword()));
        try {
            person = new PersonService().createPerson(person);
            System.out.println(person);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/

//        try {
//            System.out.println(new PersonService().getPersonById("e351f5a3-c655-4dac-a23f-222999099eea"));
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            List<Patient> patientList = new PatientService().getPatientsByHospitalId("fc526c67-d055-4b7e-938b-5794a14e806b");
//            System.out.println(patientList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            new RecordService().updateAdmitDate("60e996a8-a99f-4062-9573-ac4a4e4646a7");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String jsonResponse = "{\n" +
//                "    \"level\":\"critical\",\n" +
//                "    \"doctorId\":\"6cf2b002-04e3-4b87-994b-b4a66c5e166a\",\n" +
//                "    \"serialNumber\": \"60e996a8-a99f-4062-9573-ac4a4e4646a7\"\n" +
//                "}";
//            Gson gson = new Gson();
//            Severity severity = gson.fromJson(jsonResponse, Severity.class);
//        try {
//            severity =  new RecordService().markSeverity(severity);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

//        try {
//            new RecordService().updateDischargeDate("60e996a8-a99f-4062-9573-ac4a4e4646a7");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            QueueDetailsDAO queueDetailsDAO = new HospitalService().getQueueDetails();
//            System.out.println(queueDetailsDAO.getDistrict());
//            System.out.println(queueDetailsDAO.getLength());
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

//        try {
//            new RecordService().addQueuePatientToHospital(UUID.fromString("94908c08-8799-4ee9-9ef8-c3c36e6f7ead"));
//        } catch (FailedToGetException e) {
//            e.printStackTrace();
//        } catch (FailedToAddException e) {
//            e.printStackTrace();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

//        try {
//            new RecordService().updateDischargeDate("0f200f2d-c76d-45f5-aad6-8b209c350ea2");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(new PersonService().authenticate("director1@gmail.com","123456"));
//        try {
//            new RecordService().markDeath("e61cfcdb-e905-47f5-b7ee-818550e984ea");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//
//            StatsDAO statsDAO = new RecordService().getDailyStatsHospitalLevel(Util.covertStringToDate("2021-06-01"),"78ef9ab0-85d2-4fba-99df-995484c50b18");
//            System.out.println(statsDAO);
//        } catch (ParseException | SQLException e) {
//            e.printStackTrace();
//        }

//        try {
//            System.out.println(new RecordService().getOverAllStats());
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            System.out.println(Util.getPropValues("SECRET_KEY"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Gson gson = new Gson();
//        Person person = gson.fromJson("{\n" +
//                "    \"email\": \"director1@gmail.com\",\n" +
//                "    \"password\": \"123456\"\n" +
//                "}", Person.class);
//        AuthDAO authDAO = null;
//        try {
//            authDAO = new AuthService().authenticate("moh4@gmail.com", "123456");
//
//            System.out.println(authDAO.getPerson());
//        } catch (IOException | UnauthorizedException e) {
//            e.printStackTrace();
//        }
//        try {
//            List<Patient> patientList = new PatientService().getPatientsByHospitalId("b3170262-2b27-49e6-ad2b-d0e1d1dff10c");
//            System.out.println(patientList);
//            System.out.println(new Gson().toJson(patientList));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        System.out.println(new PatientService().getAllDistricts());
//        System.out.println(authDAO.getPerson() == null);
//        System.out.println(gson.toJson(authDAO));

//        try {
//            System.out.println(new RecordService().getOverAllStats());
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//            try {
//
//                Claims claims =AuthService.decodeJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4YzNhODlkMi0yYWNjLTQ4ZDEtOTQ4NS02NjlhNjU1YTE2ZjEiLCJpYXQiOjE2MjM0MTQ4NjgsImlzcyI6Ik5DTVMiLCJyb2xlIjoiRGlyZWN0b3IiLCJuYW1lIjoiZGlyZWN0b3IgT25lIiwiaG9zcGl0YWxJZCI6Ijc4ZWY5YWIwLTg1ZDItNGZiYS05OWRmLTk5NTQ4NGM1MGIxOCIsImV4cCI6MTYyMzQ0NDg2OH0.q170H48m61pvNM7EB7jJd2M3LfTavUNAey71vFEHz7E");
//                System.out.println(claims.get("role"));
//                System.out.println(RoleType.Director.toString());
//                System.out.println("Director".equals(RoleType.Director.toString()));
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }


        /* int serverPort = 8000;

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/api/hello", (exchange -> {

            if ("GET".equals(exchange.getRequestMethod())) {
                String respText = "Hello!";
                exchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        server.setExecutor(null); // creates a default executor
        server.start();*/
    }
}
