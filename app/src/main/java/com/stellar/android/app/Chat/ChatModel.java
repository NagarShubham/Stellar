package com.stellar.android.app.Chat;

public class ChatModel {
   private String msg;
   private String id;
   private String date;
   private Boolean isSend;



    public ChatModel() {
    }

    public Boolean getSend() {
        return isSend;
    }

    public void setSend(Boolean send) {
        isSend = send;
    }


    public ChatModel(String msg, String id, String date,boolean isSend) {
        this.msg = msg;
        this.id = id;
        this.date = date;
        this.isSend = isSend;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "msg='" + msg + '\'' +
                ", id='" + id + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

