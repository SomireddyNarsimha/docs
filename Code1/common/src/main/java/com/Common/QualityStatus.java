package com.Common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QualityStatus {
    private String uuid;
    private double pH;
    @JsonProperty("qualityStatus")
    private String qualityStatus;

    public QualityStatus(String uuid, double pH, String qualityStatus) {
        this.uuid = uuid;
        this.pH = pH;
        this.qualityStatus = qualityStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public double getpH() {
        return pH;
    }

    public String getStatus() {
        return qualityStatus;
    }
}
