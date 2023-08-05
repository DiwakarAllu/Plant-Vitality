package com.example.plantvitality;

public class ChatsModal {
//
//    public static String SENT_BY_ME = "me";
//    public static String SENT_BY_BOT="bot";
//
//    String message;
//    String sentBy;
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getSentBy() {
//        return sentBy;
//    }
//
//    public void setSentBy(String sentBy) {
//        this.sentBy = sentBy;
//    }
//
//    public ChatsModal(String message, String sentBy) {
//        this.message = message;
//        this.sentBy = sentBy;
//    }

    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT="bot";
    private String message;
    private String sender;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ChatsModal(String message,String sender){
        this.message=message;
        this.sender=sender;
    }
}
