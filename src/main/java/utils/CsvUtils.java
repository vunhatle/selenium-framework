package utils;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.*;

public class CsvUtils {

    public static List<Map<String, String>> readCsv(String filePath) {
        List<Map<String, String>> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] headers = csvReader.readNext();
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                Map<String, String> record = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    record.put(headers[i], values[i]);
                }
                records.add(record);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV: " + filePath, e);
        }
        return records;
    }
}