package com.QualityCheck.Quality.Service;

import com.QualityCheck.Quality.Dto.Records;
import com.QualityCheck.Quality.Repo.Repo;
import com.QualityCheck.Quality.entity.QualityRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
public class QualityService {
    private static final String MONITOR_SERVICE_URL = "http://localhost:8094/monitor/latest";
    private static final String ALL_RECORDS_URL = "http://localhost:8094/monitor/all";
    private final RestTemplate restTemplate;
    public Repo repo;

    public QualityService(RestTemplate restTemplate,Repo repository) {
        this.repo=repository;
        this.restTemplate = restTemplate;
    }

    public Records fetchLatestRecord() {
        return restTemplate.getForObject(MONITOR_SERVICE_URL, Records.class);
    }
    public QualityRecord checkWaterQuality(Records record) {
        if (record == null) return null;
       // repo.save(record);
        double cuSol2MgL = record.getCusol2UgL() * 0.001;
        double feSol1MgL = record.getFesol1UgL() * 0.001;
        double znSolMgL = record.getZnSolUgL() * 0.001;

        double tds = record.getCusol1MgL() + cuSol2MgL + feSol1MgL + znSolMgL;
        boolean flag =  record.getph() >= 6.5 && record.getph() <= 8.5 && tds <= 500;
         String qualityStatus;
        if(flag){
            qualityStatus = "Green";
        }else{
            qualityStatus = "Red";
        }
        QualityRecord qd = new QualityRecord(record.getId(), record.getph(), qualityStatus, record.getUuid());
        repo.save(qd);

        return qd;
    }

    public Records getRecords() {
        List<Records> dto = Arrays.asList(restTemplate.getForObject(ALL_RECORDS_URL, Records[].class));

        double ph = 0;
        double cusol1MgL = 0;
        double cusol2UgL = 0;
        double fesol1UgL = 0;
        double znSolUgL = 0;

        for(Records record : dto){
            ph+=record.getph();
            cusol1MgL  = record.getCusol1MgL();
            cusol2UgL = record.getCusol2UgL();
            fesol1UgL = record.getFesol1UgL();
            znSolUgL = record.getZnSolUgL();

        }
        Records  ans = new Records();
        int size = dto.size();
        ans.setph(ph/dto.size());
        ans.setCusol1MgL(cusol1MgL/size);
        ans.setCusol2UgL(cusol2UgL/size);
        ans.setFesol1UgL(fesol1UgL/size);
        ans.setZnSolUgL(znSolUgL/size);
        return ans;


    }
}
