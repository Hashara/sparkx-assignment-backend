package com.sparkx.util;

import com.sparkx.model.Hospital;
import com.sparkx.service.HospitalService;

import java.util.List;

public class DataInsert {

    public void insert(){
        Hospital h1 = new Hospital();
        h1.setName("Hospital1");
        h1.setDistrict("District1");
        h1.setHospitalId(Util.getUUID());
        h1.setLocation_x(32);
        h1.setLocation_y(10);

        new HospitalService().createHospital(h1);

        Hospital h2 = new Hospital();
        h2.setName("Hospital2");
        h2.setDistrict("District2");
        h2.setHospitalId(Util.getUUID());
        h2.setLocation_x(2);
        h2.setLocation_y(4);

        new HospitalService().createHospital(h2);

        Hospital h3 = new Hospital();
        h3.setName("Hospital3");
        h3.setDistrict("District3");
        h3.setHospitalId(Util.getUUID());
        h3.setLocation_x(16);
        h3.setLocation_y(1);

        new HospitalService().createHospital(h3);

        Hospital h4 = new Hospital();
        h4.setName("Hospital4");
        h4.setDistrict("District4");
        h4.setHospitalId(Util.getUUID());
        h4.setLocation_x(30);
        h4.setLocation_y(4);

        new HospitalService().createHospital(h4);

        List<Hospital> hl = new HospitalService().getAllHospitals();
        for (Hospital hospital:
             hl) {
            System.out.println(hospital.toString());
        }


    }
}
