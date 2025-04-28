package com.monitoring;
import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class CSVReaderService {

    private static BufferedReader reader;

    public static void initializeCSV(String filePath) {
        try {
            reader = new BufferedReader(new FileReader(filePath));
            reader.readLine(); // Skip the header
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Record readNextRecord() {
        try {
            String line = reader.readLine();
            if (line == null) return null;

            String[] values = line.split(",");
            return new Record(
                    Long.parseLong(values[0]),
                    UUID.randomUUID().toString(),
                    LocalDateTime.now().toString(),
                    Double.parseDouble(values[1]),
                    Double.parseDouble(values[6]),
                    Double.parseDouble(values[7]),
                    Double.parseDouble(values[8]),
                    Double.parseDouble(values[9])
            );
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
