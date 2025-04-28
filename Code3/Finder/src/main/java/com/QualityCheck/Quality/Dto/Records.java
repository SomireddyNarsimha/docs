package com.QualityCheck.Quality.Dto;

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
    private String timestamp;
    private double pH;
    private double cuSol1;
    private double cuSol2;
    private double feSol1;
    private double znSol;
    private String uuid;

    public Records() {
        this.timestamp = LocalDateTime.now().toString();
    }

    public Records(Long id, String timestamp, double pH, double cuSol1, double cuSol2, double feSol1, double znSol) {
        this.id = id;
        this.timestamp = timestamp;
        this.pH = pH;
        this.cuSol1 = cuSol1;
        this.cuSol2 = cuSol2;
        this.feSol1 = feSol1;
        this.znSol = znSol;
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getph() {
        return pH;
    }

    public void setph(double pH) {
        this.pH = pH;
    }

    public double getCusol1MgL() {
        return cuSol1;
    }

    public void setCusol1MgL(double cuSol1) {
        this.cuSol1 = cuSol1;
    }

    public double getCusol2UgL() {
        return cuSol2;
    }

    public void setCusol2UgL(double cuSol2) {
        this.cuSol2 = cuSol2;
    }

    public double getFesol1UgL() {
        return feSol1;
    }

    public void setFesol1UgL(double feSol1) {
        this.feSol1 = feSol1;
    }

    public double getZnSolUgL() {
        return znSol;
    }

    public void setZnSolUgL(double znSol) {
        this.znSol = znSol;
    }
    public String getUuid() {
        return uuid;
    }



    @Override
    public String toString() {
        return "Records{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", pH=" + pH +
                ", cuSol1=" + cuSol1 +
                ", cuSol2=" + cuSol2 +
                ", feSol1=" + feSol1 +
                ", znSol=" + znSol +
                '}';
    }

    // Getters and Setters
}


