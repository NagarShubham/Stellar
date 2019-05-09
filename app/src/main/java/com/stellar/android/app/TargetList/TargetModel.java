package com.stellar.android.app.TargetList;

public class TargetModel {
   private String Target_value;
   private String From_date;
   private String To_date;
   private String Assigned_by;
   private String Acheived_value;


    public TargetModel() {
    }

    public String getTarget_value() {
        return Target_value;
    }

    public void setTarget_value(String target_value) {
        Target_value = target_value;
    }

    public String getFrom_date() {
        return From_date;
    }

    public void setFrom_date(String from_date) {
        From_date = from_date;
    }

    public String getTo_date() {
        return To_date;
    }

    public void setTo_date(String to_date) {
        To_date = to_date;
    }

    public String getAssigned_by() {
        return Assigned_by;
    }

    public void setAssigned_by(String assigned_by) {
        Assigned_by = assigned_by;
    }

    public String getAcheived_value() {
        return Acheived_value;
    }

    public void setAcheived_value(String acheived_value) {
        Acheived_value = acheived_value;
    }

    @Override
    public String toString() {
        return "TargetModel{" +
                "Target_value='" + Target_value + '\'' +
                ", From_date='" + From_date + '\'' +
                ", To_date='" + To_date + '\'' +
                ", Assigned_by='" + Assigned_by + '\'' +
                ", Acheived_value='" + Acheived_value + '\'' +
                '}';
    }
}
