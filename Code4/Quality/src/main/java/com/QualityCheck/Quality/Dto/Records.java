package com.QualityCheck.Quality.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class Records {

    private Long id;
    private LocalDateTime timestamp;

    private double ph;
    private double cusol1MgL;
    private double cusol2UgL;
    private double fesol1UgL;
    private double znSolUgL;
    private String uuid;

    public Records() {
        this.timestamp = LocalDateTime.now();
    }

    public Records(Long id, LocalDateTime timestamp, double pH, double cuSol1, double cuSol2, double feSol1, double znSol) {
        this.id = id;
        this.timestamp = timestamp;
        this.ph = pH;
        this.cusol1MgL = cuSol1;
        this.cusol2UgL = cuSol2;
        this.fesol1UgL = feSol1;
        this.znSolUgL = znSol;
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getph() {
        return ph;
    }

    public void setph(double pH) {
        this.ph = pH;
    }

    public double getCusol1MgL() {
        return cusol1MgL;
    }

    public void setCusol1MgL(double cuSol1) {
        this.cusol1MgL = cuSol1;
    }

    public double getCusol2UgL() {
        return cusol2UgL;
    }

    public void setCusol2UgL(double cuSol2) {
        this.cusol2UgL = cuSol2;
    }

    public double getFesol1UgL() {
        return fesol1UgL;
    }

    public void setFesol1UgL(double feSol1) {
        this.fesol1UgL = feSol1;
    }

    public double getZnSolUgL() {
        return znSolUgL;
    }

    public void setZnSolUgL(double znSol) {
        this.znSolUgL = znSol;
    }
    public String getUuid() {
        return uuid;
    }



    @Override
    public String toString() {
        return "Records{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", pH=" + ph +
                ", cuSol1Mgl=" + cusol1MgL +
                ", cuSol2Ugl=" + cusol2UgL +
                ", feSol1Ugl=" + fesol1UgL +
                ", znSolUgl=" + znSolUgL +
                '}';
    }

    // Getters and Setters
}


