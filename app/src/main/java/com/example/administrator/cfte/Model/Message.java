package com.example.administrator.cfte.Model;

public class Message {
    private String message_name;
    private String message_time;
    private String message_all;

    public Message(String message_name, String message_time, String message_all) {
        this.message_name = message_name;
        this.message_time = message_time;
        this.message_all = message_all;
    }

    public String getMessage_name() {
        return message_name;
    }

    public String getMessage_time() {
        return message_time;
    }

    public String getMessage_all() {
        return message_all;
    }
}
