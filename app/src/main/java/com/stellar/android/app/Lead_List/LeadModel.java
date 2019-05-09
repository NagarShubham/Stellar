package com.stellar.android.app.Lead_List;

public class LeadModel {
    private String numberId;
    private String id;
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
    private String mobile1;
    private String date;
    private String contacttype;
    private String address;

    private String purpose;
    private String formtime;
    private String totime;
    private String category;
    private String typeofRequest;
    private String leadValue;



    public LeadModel() {
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getLeadValue() {
        return leadValue;
    }

    public void setLeadValue(String leadValue) {
        this.leadValue = leadValue;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getFormtime() {
        return formtime;
    }

    public void setFormtime(String formtime) {
        this.formtime = formtime;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTypeofRequest() {
        return typeofRequest;
    }

    public void setTypeofRequest(String typeofRequest) {
        this.typeofRequest = typeofRequest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "LeadModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", officephoen='" + officephoen + '\'' +
                ", company_name='" + company_name + '\'' +
                ", department='" + department + '\'' +
                ", designation='" + designation + '\'' +
                ", source='" + source + '\'' +
                ", assigned_to='" + assigned_to + '\'' +
                ", description='" + description + '\'' +
                ", type_of_requirement='" + type_of_requirement + '\'' +
                ", expected_lead_value='" + expected_lead_value + '\'' +
                ", lead_type='" + lead_type + '\'' +
                ", mobile='" + mobile + '\'' +
                ", date='" + date + '\'' +
                ", contacttype='" + contacttype + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
