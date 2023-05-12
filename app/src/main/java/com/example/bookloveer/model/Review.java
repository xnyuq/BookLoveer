package com.example.bookloveer.model;

public class Review {
    String id;
    float rating;
    String review;
    String user_id;
    String book_id;

    public Review() {
    }
    public Review(String id, float rating, String review, String user_id, String book_id) {
        this.id = id;
        this.rating = rating;
        this.review = review;
        this.user_id = user_id;
        this.book_id = book_id;
    }

    public Review(float rating, String review, String user_id, String book_id) {
        this.rating = rating;
        this.review = review;
        this.user_id = user_id;
        this.book_id = book_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }
}
