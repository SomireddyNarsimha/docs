package com.monitoring;

import com.Common.QualityStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import static spark.Spark.*;

public class MonitoringService {

    public static void main(String[] args) {
        port(8098);
         final ObjectMapper objectMapper = new ObjectMapper();
        DatabaseManager.initializeDatabase();
        CSVReaderService.initializeCSV("D:\\WaterQualityFile\\River_Water_Quality_Monitoring.csv");

        post("/start", (req, res) -> {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Record record = CSVReaderService.readNextRecord();
                    if (record != null) {
                        DatabaseManager.storeData(record);
                    }
                }
            }, 0, 30000);
            return "Monitoring started";
        });

        get("/allrecords", (req, res) -> {
            res.type("application/json");
            List<Record> statusList = DatabaseManager.getAllRecords();
            double ph = 0;
            double cusol1MgL = 0;
            double cusol2UgL = 0;
            double fesol1UgL = 0;
            double znSolUgL = 0;
            int size = statusList.size();
            for(Record record : statusList){
                ph+=record.getph();
                cusol1MgL  = record.getcusol1MgL();
                cusol2UgL = record.getcusol2UgL();
                fesol1UgL = record.getfesol1UgL();
                znSolUgL = record.getZnSolUgL();
            }
            ph = ph/size;
            cusol1MgL = cusol1MgL/size;
            cusol2UgL = cusol2UgL/size;
            fesol1UgL = fesol1UgL/size;
            znSolUgL = znSolUgL/size;
           Record ans = new Record(0L,null,"",ph,cusol1MgL,cusol2UgL,fesol1UgL,znSolUgL);


            return objectMapper.writeValueAsString(ans);
        });

    }
}
