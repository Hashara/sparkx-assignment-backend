package com.sparkx;

import com.sparkx.Exception.NotFoundException;
import com.sparkx.model.Queue;
import com.sparkx.model.Record;
import com.sparkx.service.HospitalService;
import com.sparkx.service.RecordService;
import com.sparkx.util.DataInsert;
import com.sparkx.util.Util;
import com.sparkx.model.Patient;
import com.sparkx.model.Person;
import com.sparkx.model.Types.GenderType;
import com.sparkx.model.Types.RoleType;
import com.sparkx.service.PatientService;

import java.sql.Date;
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
