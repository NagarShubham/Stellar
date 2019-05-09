package com.stellar.android.app.Task_List;

public class ListModel  {


    private String id;
    private String day;
    private String plan_type;
    private String customer_name;
    private String mobile;
    private String state;
    private String city;
    private String address;
    private String details;
    private String task_status;
    private String createdateandtime;

    private String companyName;
    private String purpose;
    private String formtime;
    private String totime;
    private String category;
    private String source;
    private String typeofRequest;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTypeofRequest() {
        return typeofRequest;
    }

    public void setTypeofRequest(String typeofRequest) {
        this.typeofRequest = typeofRequest;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCreatedateandtime() {
        return createdateandtime;
    }

    public void setCreatedateandtime(String createdateandtime) {
        this.createdateandtime = createdateandtime;
    }

    public ListModel() {
    }

    public ListModel(String id, String day, String plan_type, String customer_name, String mobile, String state, String city, String address, String details, String task_status, String createdateandtime) {
        this.id = id;
        this.day = day;
        this.plan_type = plan_type;
        this.customer_name = customer_name;
        this.mobile = mobile;
        this.state = state;
        this.city = city;
        this.address = address;
        this.details = details;
        this.task_status = task_status;
        this.createdateandtime = createdateandtime;
    }

    @Override
    public String toString() {
        return "ListModel{" +
                "id='" + id + '\'' +
                ", day='" + day + '\'' +
                ", plan_type='" + plan_type + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", details='" + details + '\'' +
                ", createdateandtime='" + createdateandtime + '\'' +
                '}';
    }
}

