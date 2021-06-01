package com.sparkx;

import com.google.gson.Gson;
import com.sparkx.model.Severity;
import com.sparkx.service.RecordService;

import java.sql.SQLException;


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

        try {
            new RecordService().updateDischargeDate("60e996a8-a99f-4062-9573-ac4a4e4646a7");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
