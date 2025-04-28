package com.microservice1.monitoring.Service;

import com.microservice1.monitoring.Repository.Repo;
import com.microservice1.monitoring.entity.Records;
import com.opencsv.CSVReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
@Service
public class MonitorService {
    public final Repo repository;

    public MonitorService(Repo repository) {
        this.repository = repository;
    }

    private static final String CSV_FILE = "D:\\WaterQualityFile\\River_Water_Quality_Monitoring.csv";
    Queue<Records> queue = new LinkedList<>();

    // Extracted method for mocking in tests
    protected CSVReader createCsvReader() throws Exception {
        return new CSVReader(new FileReader(CSV_FILE));
    }
     //calling this method for every 30 seconds to keep the record in the database
    @Scheduled(fixedRate = 30000)
    public void readCsvAndSaveData() {
        try (CSVReader csvReader = createCsvReader()) { //  Use extracted method
            String[] values;
            csvReader.readNext(); // Skip header

            while ((values = csvReader.readNext()) != null) {
                try {
                    // Check if the pH value is missing
                    if (values[1] == null || values[1].trim().isEmpty()) {
                        System.out.println("Skipping record due to missing pH value: " + String.join(", ", values));
                        continue;
                    }

                    double pHValue = Double.parseDouble(values[1]);

                    Records record = new Records(
                            Long.parseLong(values[0]),
                            LocalDateTime.now(),
                            pHValue,
                            Double.parseDouble(values[6]), // CUSOL1_MGL
                            Double.parseDouble(values[7]), // CUSOL2_UGL
                            Double.parseDouble(values[8]), // FESOL1_UGL
                            Double.parseDouble(values[9])  // ZN_SOL_UGL

                    );
                    System.out.println(record.getUuid());
                    if(!repository.existsById(record.getId())) {
                        repository.save(record);
                        System.out.println("Saved record: " + record);
                    }
                    queue.add(record);
                    Thread.sleep(10000); // 1-second delay between records
                } catch (NumberFormatException e) {
                    System.out.println("Skipping record due to invalid number format: " + String.join(", ", values));
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing CSV file: " + e.getMessage());
        }
    }
   //used to retrive the first record which is inserted in the database
    public Records getLatestRecord()
    {
        if(queue.isEmpty()){
            return null;
        }
        return queue.poll();
    }

    public List<Records> getAllRecords() {
        return repository.findAll();
    }
}
