package com.example.bookloveer.model;

public class User {
    String id;
    String email;

    public User() {
    }
    public User(String id) {
        this.id = id;
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
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
}
