package com.innprojects.biometatt.javaModels;

/**
 * Created by simransarin on 04/07/17.
 */

public class EmergencyContact {

    private String id;
    private String contact_name;
    private String phone_number;
    private String email_id;

    public EmergencyContact(String id, String contact_name, String phone_number, String email_id) {
        this.id = id;
        this.contact_name = contact_name;
        this.phone_number = phone_number;
        this.email_id = email_id;
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

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }
}
