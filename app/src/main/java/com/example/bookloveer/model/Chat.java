package com.example.bookloveer.model;

import java.util.ArrayList;
import java.util.List;
public class Chat {
    private String email;
    private String lastMessage;
    private long timestamp;

    public Chat() {}

    public Chat(String email, String lastMessage, long timestamp) {
        this.email = email;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
