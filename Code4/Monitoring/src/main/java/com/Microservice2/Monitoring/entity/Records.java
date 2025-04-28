package com.Microservice2.Monitoring.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "water_quality_data")
public class Records {

    @Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Object_id", nullable = false)
    private Long Object_id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "Unique_uuid", unique = true, nullable = false)
    private String Unique_uuid;

    @Column(name = "ph", nullable = false)
    private double ph;

    @Column(name = "cuSol1MgL", nullable = false)
    private double cusol1MgL;

    @Column(name = "cuSol2UgL", nullable = false)
    private double cusol2UgL;

    @Column(name = "feSol1UgL", nullable = false)
    private double fesol1UgL;

    @Column(name = "znSolUgL", nullable = false)
    private double znSolUgL;

    public Records() {
        this.Unique_uuid = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public Records(Long id, LocalDateTime timestamp, double pH, double cuSol1, double cuSol2, double feSol1, double znSol) {
        this.Object_id = id;
        this.timestamp = timestamp;
        this.ph = pH;
        this.cusol1MgL = cuSol1;
        this.cusol2UgL = cuSol2;
        this.fesol1UgL = feSol1;
        this.znSolUgL = znSol;
        this.Unique_uuid = UUID.randomUUID().toString();
    }

    public Long getId() {
        return Object_id;
    }

    public void setId(Long id) {
        this.Object_id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return Unique_uuid;
    }

    public void setUuid(String uuid) {
        this.Unique_uuid = uuid;
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

    @Override
    public String toString() {
        return "Records{" +
                "id=" + Object_id +
                ", timestamp=" + timestamp +
                ", uuid='" + Unique_uuid + '\'' +
                ", pH=" + ph +
                ", cuSol1=" + cusol1MgL +
                ", cuSol2=" + cusol2UgL +
                ", feSol1=" + fesol1UgL+
                ", znSol=" + znSolUgL +
                '}';
    }
}
