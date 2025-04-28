package com.microservice2.Quality.dto;


import jakarta.persistence.Column;

//creating a class  to store the data temperorly in its object
public class MonitoringRecordDTO {
    private Long id;
    private double ph;
    private String qualityStatus;
    private String uuid;
    private double cusol1MgL;
    private double cusol2UgL;
    private double fesol1UgL;
    private double znSolUgL;

    public MonitoringRecordDTO() {}

    public MonitoringRecordDTO(Long id, double ph, String qualityStatus,String uuid) {
        this.id = id;
        this.ph = ph;
        this.qualityStatus = qualityStatus;
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public String getQualityStatus() {
        return qualityStatus;
    }

    public void setQualityStatus(String qualityStatus) {
        this.qualityStatus = qualityStatus;
    }
    public void setUuid(String uuid){
        this.uuid = uuid;
    }
    public String getUuid(){
        return uuid;
    }


    @Override
    public String toString() {
        return "MonitoringRecordDTO{" +
                "id='" + id + '\'' +
                ", ph=" + ph +
                ", qualityStatus='" + qualityStatus + '\'' +
                ", UUID=" + uuid +
                '}';
    }

    public double getCusol1MgL() {
        return cusol1MgL;
    }

    public void setCusol1MgL(double cusol1MgL) {
        this.cusol1MgL = cusol1MgL;
    }

    public double getCusol2UgL() {
        return cusol2UgL;
    }

    public void setCusol2UgL(double cusol2UgL) {
        this.cusol2UgL = cusol2UgL;
    }

    public double getFesol1UgL() {
        return fesol1UgL;
    }

    public void setFesol1UgL(double fesol1UgL) {
        this.fesol1UgL = fesol1UgL;
    }

    public double getZnSolUgL() {
        return znSolUgL;
    }

    public void setZnSolUgL(double znSolUgL) {
        this.znSolUgL = znSolUgL;
    }
}
