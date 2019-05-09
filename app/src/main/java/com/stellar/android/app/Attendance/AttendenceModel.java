package com.stellar.android.app.Attendance;

public class AttendenceModel {

    private String date;
    private String month;
    private String year;
    private String status;


    public AttendenceModel(String date, String month, String year, String status) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "AttendenceModel{" +
                "date='" + date + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
