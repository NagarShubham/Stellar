package coms.example.cwaa1.stellar.ManagerAssign;

public class TabIdModel {

    String name;
    String nameId;


    public TabIdModel() {
    }

    public TabIdModel(String name, String nameId) {
        this.name = name;
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    @Override
    public String toString() {
        return "TabIdModel{" +
                "name='" + name + '\'' +
                ", nameId='" + nameId + '\'' +
                '}';
    }
}
