package com.stellar.android.app.teamTask;

public class PandingModel {
    private String id;
    private String name;
    private String day;


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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "PandingModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
