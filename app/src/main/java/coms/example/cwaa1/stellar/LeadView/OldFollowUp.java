package coms.example.cwaa1.stellar.LeadView;

public class OldFollowUp {
   private String oldDate;
   private String nextDate;
   private String oldcomant;
   private String oldresp;
   private String empname;
   private String no;


    public OldFollowUp() {
    }

    public String getOldDate() {
        return oldDate;
    }

    public void setOldDate(String oldDate) {
        this.oldDate = oldDate;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public String getOldcomant() {
        return oldcomant;
    }

    public void setOldcomant(String oldcomant) {
        this.oldcomant = oldcomant;
    }

    public String getOldresp() {
        return oldresp;
    }

    public void setOldresp(String oldresp) {
        this.oldresp = oldresp;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "OldFollowUp{" +
                "oldDate='" + oldDate + '\'' +
                ", nextDate='" + nextDate + '\'' +
                ", oldcomant='" + oldcomant + '\'' +
                ", oldresp='" + oldresp + '\'' +
                ", empname='" + empname + '\'' +
                ", no='" + no + '\'' +
                '}';
    }
}
