package com.monitoring;

import java.time.LocalDateTime;
import java.util.UUID;

public class Record {
    private long id;
    private String uuid;
    private String timestamp;
    private double pH;
    private double cuSol1;
    private double cuSol2;
    private double feSol1;
    private double znSol;

    public Record(long id, String uuid, String timestamp, double pH, double cuSol1, double cuSol2, double feSol1, double znSol) {
        this.id = id;
        this.uuid = (uuid != null) ? uuid : UUID.randomUUID().toString();
        if(timestamp.length() == 0){
               this.timestamp = LocalDateTime.now().toString();
        }else {
            this.timestamp = LocalDateTime.parse(timestamp).toString();
        }
        this.pH = pH;
        this.cuSol1 = cuSol1;
        this.cuSol2 = cuSol2;
        this.feSol1 = feSol1;
        this.znSol = znSol;
    }

    public String getUuid() {
        return uuid;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public double getph() {
        return pH;
    }
    public double getcusol1MgL() {
        return cuSol1;
    }
    public double getcusol2UgL() {
        return cuSol2;
    }
    public double getfesol1UgL() {
        return feSol1;
    }
    public double getZnSolUgL() {
        return znSol;
    }
}
