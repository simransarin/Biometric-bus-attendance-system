package com.innprojects.biometappdriver.models;

/**
 * Created by simransarin on 29/05/17.
 */

public class Bus {

    private String driver_name;
    private String conductor_name;
    private String number_plate;
    private String bus_no;
    private String teacher_incharge;
    private String driver_name_number;
    private String teacher_incharge_number;

    public Bus(String driver_name, String conductor_name, String number_plate, String bus_no, String teacher_incharge, String driver_name_number, String teacher_incharge_number) {
        this.driver_name = driver_name;
        this.conductor_name = conductor_name;
        this.number_plate = number_plate;
        this.bus_no = bus_no;
        this.teacher_incharge = teacher_incharge;
        this.driver_name_number = driver_name_number;
        this.teacher_incharge_number = teacher_incharge_number;
    }

    public String getDriver_name_number() {
        return driver_name_number;
    }

    public String getTeacher_incharge_number() {
        return teacher_incharge_number;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public String getConductor_name() {
        return conductor_name;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public String getBus_no() {
        return bus_no;
    }

    public String getTeacher_incharge() {
        return teacher_incharge;
    }
}
