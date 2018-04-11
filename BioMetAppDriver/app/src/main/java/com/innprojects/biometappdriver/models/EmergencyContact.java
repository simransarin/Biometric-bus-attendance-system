package com.innprojects.biometappdriver.models;

/**
 * Created by simransarin on 04/07/17.
 */

public class EmergencyContact {

    private String id;
    private String contact_name;
    private String phone_number;

    public EmergencyContact(String id, String contact_name, String phone_number) {
        this.id = id;
        this.contact_name = contact_name;
        this.phone_number = phone_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
