package com.stellar.android.app.LeavForm;

public class LeaveModel {
    private String Fromdate;
    private String Todate;
    private String leavType;
    private String ResonLeave;
    private String AdminResonLeave;
    private String LeaveStatus;

    public LeaveModel() {
    }

    public String getFromdate() {
        return Fromdate;
    }

    public void setFromdate(String fromdate) {
        Fromdate = fromdate;
    }

    public String getTodate() {
        return Todate;
    }

    public void setTodate(String todate) {
        Todate = todate;
    }

    public String getLeavType() {
        return leavType;
    }

    public void setLeavType(String leavType) {
        this.leavType = leavType;
    }

    public String getResonLeave() {
        return ResonLeave;
    }

    public void setResonLeave(String resonLeave) {
        ResonLeave = resonLeave;
    }

    public String getLeaveStatus() {
        return LeaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        LeaveStatus = leaveStatus;
    }

    public String getAdminResonLeave() {
        return AdminResonLeave;
    }

    public void setAdminResonLeave(String adminResonLeave) {
        AdminResonLeave = adminResonLeave;
    }

    @Override
    public String toString() {
        return "LeaveModel{" +
                "Fromdate='" + Fromdate + '\'' +
                ", Todate='" + Todate + '\'' +
                ", leavType='" + leavType + '\'' +
                ", ResonLeave='" + ResonLeave + '\'' +
                ", LeaveStatus='" + LeaveStatus + '\'' +
                '}';
    }
}
