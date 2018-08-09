package com.thesis2.genise_villanueva.thesis;

public class Reviews {

    String reviewId;
    String user;
    String location;
    String review;

    public Reviews() {
    }

    Reviews(String reviewId, String user, String location, String review){
        this.reviewId = reviewId;
        this.user = user;
        this.location = location;
        this.review = review;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getUser() {
        return user;
    }

    public String getLocation() {
        return location;
    }

    public String getReview() {
        return review;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
