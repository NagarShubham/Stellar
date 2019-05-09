package com.stellar.android.app.ManagerAssign;

public class DetailsModel {
    private String id;
   private String name;
   private String mobile;
   private String contacttype;
   private String address;
   private String details;

    public DetailsModel() {
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DetailsModel{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", contacttype='" + contacttype + '\'' +
                ", address='" + address + '\'' +
                ", details='" + details + '\'' +
                '}';
    }

}
