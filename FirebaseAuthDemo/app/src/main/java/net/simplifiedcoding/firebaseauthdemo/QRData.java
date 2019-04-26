package net.simplifiedcoding.firebaseauthdemo;

public class QRData {
    String bathroom_id;
    String utilityCode;

    public QRData(String bathroom_id, String utilityCode) {
        this.bathroom_id = bathroom_id;
        this.utilityCode = utilityCode;
    }

    public String getBathroom_id() {
        return bathroom_id;
    }

    public void setBathroom_id(String bathroom_id) {
        this.bathroom_id = bathroom_id;
    }

    public String getUtilityCode() {
        return utilityCode;
    }

    public void setUtilityCode(String utilityCode) {
        this.utilityCode = utilityCode;
    }
}
