package com.innprojects.biometatt.javaModels;

/**
 * Created by simransarin on 29/05/17.
 */

public class Student {
    private String student_id;
    private String student_name;
    private String parent_name;
    private String class_number;
    private String section;
    private String phone_number;
    private String student_bus_stop;
    private String bus;
    private String emailID;

    public Student() {
    }

    public Student(String student_id, String student_name, String parent_name, String class_number, String section, String phone_number, String student_bus_stop, String bus, String emailID) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.parent_name = parent_name;
        this.class_number = class_number;
        this.section = section;
        this.phone_number = phone_number;
        this.student_bus_stop = student_bus_stop;
        this.bus = bus;
        this.emailID = emailID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStudent_bus_stop() {
        return student_bus_stop;
    }

    public void setStudent_bus_stop(String student_bus_stop) {
        this.student_bus_stop = student_bus_stop;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }


}