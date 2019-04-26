package net.simplifiedcoding.firebaseauthdemo;

import java.util.Date;

public class UserFeedback{
    float stars;
    String time;
    String bathroom_id;
    String floor;
    String code;
    boolean unclean;
    boolean unrepaired;
    String customFeedback;


    public UserFeedback(float stars, String time, String floor, String bathroom_id, String code, boolean unclean, boolean unrepaired, String customFeedback) {
        this.stars = stars;
        this.time = time;
        this.floor = floor;
        this.code = code;
        this.unclean = unclean;
        this.unrepaired = unrepaired;
        this.customFeedback = customFeedback;
        this.bathroom_id = bathroom_id;
    }

    public String getBathroom_id() {
        return bathroom_id;
    }

    public void setBathroom_id(String bathroom_id) {
        this.bathroom_id = bathroom_id;
    }

    public String getCustomFeedback() {
        return customFeedback;
    }

    public void setCustomFeedback(String customFeedback) {
        this.customFeedback = customFeedback;
    }

    public boolean isUnclean() {
        return unclean;
    }

    public void setUnclean(boolean unclean) {
        this.unclean = unclean;
    }

    public boolean isUnrepaired() {
        return unrepaired;
    }

    public void setUnrepaired(boolean unrepaired) {
        this.unrepaired = unrepaired;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}