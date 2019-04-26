package net.simplifiedcoding.workerattendance1;


public class AttendanceTime {
    String uid;
    String time;
    String date;

    public AttendanceTime(String uid, String time, String date) {
        this.uid = uid;
        this.time = time;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
