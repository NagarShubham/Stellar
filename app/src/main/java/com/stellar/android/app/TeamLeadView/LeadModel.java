package com.stellar.android.app.TeamLeadView;

import java.io.Serializable;

public class LeadModel  implements Serializable {

    private String id;
    private String empname;
    private String name;
    private String email;
    private String officephoen;
    private String company_name;
    private String department;
    private String designation;
    private String source;
    private String assigned_to;
    private String description;
    private String type_of_requirement;
    private String expected_lead_value;
    private String lead_type;

    private String mobile;
    private String date;
    private String contacttype;
    private String address;


    public LeadModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficephoen() {
        return officephoen;
    }

    public void setOfficephoen(String officephoen) {
        this.officephoen = officephoen;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(String assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_of_requirement() {
        return type_of_requirement;
    }

    public void setType_of_requirement(String type_of_requirement) {
        this.type_of_requirement = type_of_requirement;
    }

    public String getExpected_lead_value() {
        return expected_lead_value;
    }

    public void setExpected_lead_value(String expected_lead_value) {
        this.expected_lead_value = expected_lead_value;
    }

    public String getLead_type() {
        return lead_type;
    }

    public void setLead_type(String lead_type) {
        this.lead_type = lead_type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContacttype() {
        return contacttype;
    }

    public void setContacttype(String contacttype) {
        this.contacttype = contacttype;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
