package com.innprojects.biometatt.javaModels;

/**
 * Created by simransarin on 29/05/17.
 */

public class Bus {

    private String driver_name;
    private String conductor_name;
    private String number_plate;
    private String bus_no;
    private String teacher_incharge;
    private String longitude;
    private String latitude;
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

    public Bus(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getDriver_name_number() {
        return driver_name_number;
    }

    public void setDriver_name_number(String driver_name_number) {
        this.driver_name_number = driver_name_number;
    }

    public String getTeacher_incharge_number() {
        return teacher_incharge_number;
    }

    public void setTeacher_incharge_number(String teacher_incharge_number) {
        this.teacher_incharge_number = teacher_incharge_number;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getConductor_name() {
        return conductor_name;
    }

    public void setConductor_name(String conductor_name) {
        this.conductor_name = conductor_name;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public String getTeacher_incharge() {
        return teacher_incharge;
    }

    public void setTeacher_incharge(String teacher_incharge) {
        this.teacher_incharge = teacher_incharge;
    }
}
