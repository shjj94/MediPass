package com.medi.medipass;

/**
 * Created by shjjthjj on 2016-05-10.
 */
public class ListViewItem_medicine {
    String medName;
    int once_num;
    int day_num;
    String notice;

    public String getMedName() {
        return medName;
    }

    public int getOnce_num() {
        return once_num;
    }

    public int getDay_num() {
        return day_num;
    }

    public String getNotice() {
        return notice;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public void setOnce_num(int once_num) {
        this.once_num = once_num;
    }

    public void setDay_num(int day_num) {
        this.day_num = day_num;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
