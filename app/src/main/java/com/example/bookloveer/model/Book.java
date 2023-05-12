package com.example.bookloveer.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String id;
    private String image;
    private String title;
    private String author;
    private String description;

    public Book(String id, String image, String title, String author, String description) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.author = author;
        this.description = description;
    }
    public Book() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
