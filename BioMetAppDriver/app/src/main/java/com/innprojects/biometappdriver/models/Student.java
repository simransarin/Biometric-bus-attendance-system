package com.innprojects.biometappdriver.models;

import java.io.Serializable;

/**
 * Created by simransarin on 29/05/17.
 */

public class Student implements Serializable {
    private String student_id;
    private String student_adm_no;
    private String student_name;
    private String parent_name;
    private String class_number;
    private String section;
    private String phone_number;
    private String student_bus_stop;

    public Student() {
    }

    public Student(String student_id, String student_adm_no, String student_name, String parent_name, String class_number, String section, String phone_number, String student_bus_stop) {
        this.student_id = student_id;
        this.student_adm_no = student_adm_no;
        this.student_name = student_name;
        this.parent_name = parent_name;
        this.class_number = class_number;
        this.section = section;
        this.phone_number = phone_number;
        this.student_bus_stop = student_bus_stop;
    }
    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
    public String getStudent_adm_no() {
        return student_adm_no;
    }

    public void setStudent_adm_no(String student_adm_no) {
        this.student_adm_no = student_adm_no;
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
}