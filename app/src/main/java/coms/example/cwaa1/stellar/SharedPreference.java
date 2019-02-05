package coms.example.cwaa1.stellar;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference  {
    private Context context;
    private SharedPreferences sharedPreferences;
    private String uid;
    private String user_emailid;
    private String type;
    private Boolean flag;
    private int value;
    private String email;
    private String password;

//    public JSONArray getJsonArray() {
//        jsonArray = sharedPreferences.getString("jsonArray",null);
//        return jsonArray;
//    }
//
//    public void setJsonArray(String jsonArray) {
//        this.jsonArray = jsonArray;
//        sharedPreferences.edit().putString("jsonArray",jsonArray.toString());
//    }

    public String getJsonArray() {
        jsonArray = sharedPreferences.getString("jsonArray",null);
        return jsonArray;
    }

    public void setJsonArray(String jsonArray) {
        this.jsonArray = jsonArray;
        sharedPreferences.edit().putString("jsonArray",jsonArray).apply();
    }

    private String jsonArray;

    public int getCounter() {
        counter = sharedPreferences.getInt("Counter",0);
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
        sharedPreferences.edit().putInt("Counter",counter).apply();
    }

    private int counter;

    public String getUser_name() {
        user_name = sharedPreferences.getString("user_name","");
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
        sharedPreferences.edit().putString("user_name",user_name).apply();

    }

    private String user_name;

    public String getAdminid() {
        adminid = sharedPreferences.getString("adminid","");
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
        sharedPreferences.edit().putString("adminid",adminid).apply();
    }

    private String adminid;

    public String getPassword() {
        password = sharedPreferences.getString("password","");
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        sharedPreferences.edit().putString("password",password).apply();
    }

    public String getUid() {
        uid = sharedPreferences.getString("uid","");
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
        sharedPreferences.edit().putString("uid",uid).apply();
    }

    public String getUser_emailid() {
        user_emailid = sharedPreferences.getString("useremail","");
        return user_emailid;
    }

    public void setUser_emailid(String user_emailid) {
        this.user_emailid = user_emailid;
        sharedPreferences.edit().putString("useremail",user_emailid).apply();

    }

    public String getType() {
        type = sharedPreferences.getString("type","");
        return type;
    }

    public void setType(String type) {
        this.type = type;
        sharedPreferences.edit().putString("type",type).apply();
    }

    public Boolean getFlag() {
        flag = sharedPreferences.getBoolean("login",false);
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
        sharedPreferences.edit().putBoolean("login",flag).apply();
    }



    public int getValue() {
        value = sharedPreferences.getInt("value",0);
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        sharedPreferences.edit().putInt("value",value).apply();
    }


    public void remove() {
        sharedPreferences.edit().clear().commit();
    }

    public String getEmail() {
        email = sharedPreferences.getString("userdata","");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("userdata",email).apply();
    }

    public SharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }
}

