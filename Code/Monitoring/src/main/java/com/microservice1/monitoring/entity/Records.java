package com.microservice1.monitoring.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;


//creating table
@Entity
@Data
@Table(name = "water_quality_records")
public class Records {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "uuid", unique = true, updatable = false, nullable = false)
    private String uuid;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "ph", nullable = false)
    private double ph;

    @Column(name = "cusol1MgL", nullable = false)
    private double cusol1MgL;

    @Column(name = "cusol2UgL", nullable = false)
    private double cusol2UgL;

    @Column(name = "fesol1UgL", nullable = false)
    private double fesol1UgL;

    @Column(name = "znSolUgL", nullable = false)
    private double znSolUgL;
    public Records(){

    }
    public Records(Long id, LocalDateTime timestamp, double ph, double cusol1MgL,
                   double cusol2UgL, double fesol1UgL, double znSolUgL) {
        this.id = id;
        this.timestamp = timestamp;
        this.ph = ph;
        this.cusol1MgL = cusol1MgL;
        this.cusol2UgL = cusol2UgL;
        this.fesol1UgL = fesol1UgL;
        this.znSolUgL = znSolUgL;
        this.uuid = UUID.randomUUID().toString();
    }
    public double getPh(){
        return ph;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getCusol1MgL() {
        return cusol1MgL;
    }

    public double getCusol2UgL() {
        return cusol2UgL;
    }

    public double getFesol1UgL() {
        return fesol1UgL;
    }

    public double getZnSolUgL() {
        return znSolUgL;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public void setCusol1MgL(double cusol1MgL) {
        this.cusol1MgL = cusol1MgL;
    }

    public void setCusol2UgL(double cusol2UgL) {
        this.cusol2UgL = cusol2UgL;
    }

    public void setFesol1UgL(double fesol1UgL) {
        this.fesol1UgL = fesol1UgL;
    }

    public void setZnSolUgL(double znSolUgL) {
        this.znSolUgL = znSolUgL;
    }
    public String getUuid(){
        return uuid;
    }

    @Override
    public String toString() {
        return "Records{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", ph=" + ph +
                ", cusol1MgL=" + cusol1MgL +
                ", cusol2UgL=" + cusol2UgL +
                ", fesol1UgL=" + fesol1UgL +
                ", znSolUgL=" + znSolUgL +
                ", UUID=" +uuid+
                '}';
    }
}