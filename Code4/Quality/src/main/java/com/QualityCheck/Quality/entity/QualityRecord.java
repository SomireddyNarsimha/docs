package com.QualityCheck.Quality.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

//creating a table to store the  status of the water
@Entity
public class QualityRecord{
    @Id
    private Long Object_id;
    private String qualityStatus;
    private String Unique_uuid;
    private double ph;
    public QualityRecord(){

    }

    public QualityRecord(Long id, double ph, String qualityStatus,String uuid) {
        this.Object_id = id;
        this.ph = ph;
        this.qualityStatus = qualityStatus;
        this.Unique_uuid = uuid;
    }
    public void setUuid(String uuid){
        this.Unique_uuid = uuid;
    }
    public String getUuid(){
        return this.Unique_uuid;
    }

    public Long getId() {
        return Object_id;
    }

    public void setId(Long id) {
        this.Object_id = id;
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

    @Override
    public String toString() {
        return "MonitoringRecordDTO{" +
                "id='" + Object_id + '\'' +
                ", ph=" + ph +
                ", qualityStatus='" + qualityStatus + '\'' +
                ", UUID=" + Unique_uuid +
                '}';
    }
}

