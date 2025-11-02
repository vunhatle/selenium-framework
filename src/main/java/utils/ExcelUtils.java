package utils;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelUtils {
    public static List<Map<String, String>> readExcel(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> data = new LinkedHashMap<>();
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String key = headerRow.getCell(j).getStringCellValue();
                    String value = row.getCell(j).getStringCellValue();
                    data.put(key, value);
                }
                dataList.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
