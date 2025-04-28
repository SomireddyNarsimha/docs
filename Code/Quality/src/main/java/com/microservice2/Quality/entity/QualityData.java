package com.microservice2.Quality.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;


//creating a table to store the  status of the water
@Entity
public class QualityData {
    @Id
    private Long id;
    private double ph;
    private String qualityStatus;
    private String uuid;
    public QualityData(){

    }

    public QualityData(Long id, double ph, String qualityStatus,String uuid) {
        this.id = id;
        this.ph = ph;
        this.qualityStatus = qualityStatus;
        this.uuid = uuid;
    }
     public void setUuid(String uuid){
         this.uuid = uuid;
     }
     public String getUuid(){
        return this.uuid;
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

    @Override
    public String toString() {
        return "MonitoringRecordDTO{" +
                "id='" + id + '\'' +
                ", ph=" + ph +
                ", qualityStatus='" + qualityStatus + '\'' +
                ", UUID=" + uuid +
                '}';
    }
}
