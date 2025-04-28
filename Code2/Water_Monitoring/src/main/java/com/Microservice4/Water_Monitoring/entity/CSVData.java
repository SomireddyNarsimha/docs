package com.Microservice4.Water_Monitoring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Storage")
public class CSVData {

    @Id
    //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Record_id", nullable = false)
    private Long Object_id;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "water_sample_id", unique = true, nullable = false)
    private String Unique_uuid;

    @Column(name = "pH", nullable = false)
    private double pH;

    @Column(name = "cuSol1MgL", nullable = false)
    private double cuSol1;

    @Column(name = "cuSol2UgL", nullable = false)
    private double cuSol2;

    @Column(name = "feSol1UgL", nullable = false)
    private double feSol1;

    @Column(name = "znSolUgL", nullable = false)
    private double znSol;
    private boolean processed = false;

    public CSVData () {
        this.Unique_uuid = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now().toString();
    }

    public CSVData (Long id, String timestamp, double pH, double cuSol1, double cuSol2, double feSol1, double znSol) {
        this.Object_id = id;
        this.timestamp = timestamp;
        this.pH = pH;
        this.cuSol1 = cuSol1;
        this.cuSol2 = cuSol2;
        this.feSol1 = feSol1;
        this.znSol = znSol;
        this.Unique_uuid = UUID.randomUUID().toString();
    }

    public Long getId() {
        return Object_id;
    }

    public void setId(Long id) {
        this.Object_id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return Unique_uuid;
    }

    public void setUuid(String uuid) {
        this.Unique_uuid = uuid;
    }

    public double getpH() {
        return pH;
    }

    public void setpH(double pH) {
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


    @Override
    public String toString() {
        return "Records{" +
                "id=" + Object_id +
                ", timestamp=" + timestamp +
                ", uuid='" + Unique_uuid + '\'' +
                ", pH=" + pH +
                ", cuSol1=" + cuSol1 +
                ", cuSol2=" + cuSol2 +
                ", feSol1=" + feSol1 +
                ", znSol=" + znSol +
                '}';
    }
}
