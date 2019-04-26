package net.simplifiedcoding.workerattendance1;

public class AttendanceData {

    String bathroomID;
    String floor;
    String time;
    String date;

    public AttendanceData(String bathroomID, String floor, String time, String date) {
        this.bathroomID = bathroomID;
        this.floor = floor;
        this.time = time;
        this.date = date;
    }

    public String getBathroomID() {
        return bathroomID;
    }

    public void setBathroomID(String bathroomID) {
        this.bathroomID = bathroomID;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
