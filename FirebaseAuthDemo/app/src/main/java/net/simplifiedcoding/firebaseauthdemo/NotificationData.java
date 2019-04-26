package net.simplifiedcoding.firebaseauthdemo;

public class NotificationData {
    float stars;
    float avgRating;
    String workerName;
    String bathroomID;

    public NotificationData(float stars, float avgRating, String workerName, String bathroomID) {
        this.stars = stars;
        this.avgRating = avgRating;
        this.workerName = workerName;
        this.bathroomID = bathroomID;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerUID(String workerName) {
        this.workerName = workerName;
    }

    public String getBathroomID() {
        return bathroomID;
    }

    public void setBathroomID(String bathroomID) {
        this.bathroomID = bathroomID;
    }
}
