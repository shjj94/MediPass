package com.medi.medipass;

/**
 * Created by Nara on 2016-05-18.
 */
public class SubmitItem {
    protected String vTitle;//vName//tv_title 진료일
    protected String vDate;//vSurname//tv_date 처방전 발급 마감일
    protected String vHospital;//vEmail//tv_hospital 병원이름
    protected String vDisease;//vTitle//tv_disease 병이름
    protected String vPrescription;//vPres//tv_presnum 처방전번호


    public String getTitle() {
        return vTitle;
    }

    public void setTitle(String vTitle) {
        this.vTitle = vTitle;
    }

    public String getDate() {
        return vDate;
    }

    public void setDate(String vDate) {
        this.vDate = vDate;
    }

    public String getHospital() {
        return vHospital;
    }

    public void setHospital(String vHospital) {
        this.vHospital = vHospital;
    }

    public String getDisease() {
        return vDisease;
    }

    public void setDisease(String vDisease) {
        this.vDisease = vDisease;
    }

    public String getPrescription() {
        return vPrescription;
    }

    public void setPrescription(String vPrescription) {
        this.vPrescription = vPrescription;
    }

    public SubmitItem(String vTitle, String vDate, String vHospital, String vDisease, String vPrescription) {
        this.vTitle = vTitle;
        this.vDate = vDate;
        this.vHospital = vHospital;
        this.vDisease = vDisease;
        this.vPrescription = vPrescription;
    }

}
