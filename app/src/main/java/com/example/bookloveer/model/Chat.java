package com.example.bookloveer.model;

import java.util.List;

public class Chat {
    private String id;
    private String email;
    private List<Mess> messages;

    public Chat(String id, String email, List<Mess> messages) {
        this.id = id;
        this.email = email;
        this.messages = messages;
    }

    public Chat(List<Mess> messages) {
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Mess> getMessages() {
        return messages;
    }

    public void setMessages(List<Mess> messages) {
        this.messages = messages;
    }

    public String getLatestMessagePreview() {
        if (messages.size() > 0) {
            Mess latestMessage = messages.get(messages.size() - 1);
            return latestMessage.getMessage();
        } else {
            return "";
        }
    }
}
