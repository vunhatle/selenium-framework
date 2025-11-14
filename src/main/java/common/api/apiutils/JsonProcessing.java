package common.api.apiutils;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.*;

public class JsonProcessing {

    // Phương thức ghi dữ liệu JSON vào CSV, xử lý dữ liệu nested
    public static void writeDataToCsv(JsonNode data, String fileName) throws IOException {
        if (data == null || !data.isArray() || data.size() == 0) {
            return;  // Nếu không có dữ liệu, không ghi gì
        }

        // Lấy thư mục Downloads của người dùng
        String userHome = System.getProperty("user.home");
        String downloadDir = userHome + File.separator + "Downloads"; // Thư mục Downloads
        File dir = new File(downloadDir);
        if (!dir.exists()) {
            dir.mkdir();  // Tạo thư mục nếu chưa có
        }

        // Đặt tên file đầy đủ
        String filePath = downloadDir + File.separator + fileName;

        // Lấy danh sách các trường (headers) từ đối tượng JSON đầu tiên
        Iterator<String> fieldNames = data.get(0).fieldNames();
        Map<String, String> headers = new HashMap<>();
        int headerIndex = 0;

        // Phẳng hóa dữ liệu nested
        for (JsonNode node : data) {
            flattenJson(node, "", headers);
        }

        // Tạo header từ các key trong Map
        String[] headerArray = headers.keySet().toArray(new String[0]);

        // Mở file để ghi dữ liệu
        try (FileWriter fileWriter = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(headerArray))) {

            // Duyệt qua từng phần tử trong mảng JSON và ghi vào file CSV
            for (JsonNode node : data) {
                String[] values = new String[headerArray.length];

                // Lấy giá trị của từng trường và chuyển thành chuỗi
                for (int j = 0; j < headerArray.length; j++) {
                    String header = headerArray[j];
                    JsonNode fieldValue = getNestedValue(node, header.split("\\."));
                    values[j] = fieldValue != null ? fieldValue.asText() : "";  // Nếu không có giá trị, để chuỗi rỗng
                }

                // Ghi giá trị vào CSV
                csvPrinter.printRecord((Object[]) values);
            }

            csvPrinter.flush();
        }

        System.out.println("Data written to CSV at: " + filePath);
    }

    // Hàm phẳng hóa JSON nested
    private static void flattenJson(JsonNode node, String prefix, Map<String, String> headers) {
        if (node.isObject()) {
            // Nếu là đối tượng JSON, duyệt qua các trường của nó
            node.fieldNames().forEachRemaining(fieldName -> {
                flattenJson(node.get(fieldName), prefix + fieldName + ".", headers);
            });
        } else if (node.isArray()) {
            // Nếu là mảng JSON, duyệt qua từng phần tử trong mảng
            for (int i = 0; i < node.size(); i++) {
                flattenJson(node.get(i), prefix + i + ".", headers);

            }
        } else {
            // Nếu là giá trị cơ bản, thêm vào headers
            headers.put(prefix, prefix);
        }
    }

    // Lấy giá trị từ JSON theo key có dạng nested (dùng split để lấy các trường lồng nhau)
    private static JsonNode getNestedValue(JsonNode node, String[] keys) {
        for (String key : keys) {
            if (node.has(key)) {
                node = node.get(key);
            } else {
                return null;  // Trả về null nếu không tìm thấy key
            }
        }
        return node;
    }

    public static List<Map<String, Object>> readCsvToRequestBodies(String fileName) {
        List<Map<String, Object>> requestBodyList = new ArrayList<>();
        String line;
        String cvsSplitBy = ",";

        // Lấy thư mục Downloads của người dùng
        String userHome = System.getProperty("user.home");
        String downloadDir = userHome + File.separator + "Downloads"; // Thư mục Downloads

        // Đặt tên file đầy đủ
        String filePath = downloadDir + File.separator + fileName;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String[] headers = null;
            int rowCount = 0;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);

                if (rowCount == 0) {
                    headers = values; // dòng đầu tiên là header
                } else {
                    Map<String, Object> requestBody = new HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        String key = headers[i];
                        String value = values[i];
                        // Chuyển kiểu cho id và userId
                        if ("id".equalsIgnoreCase(key) || "userId".equalsIgnoreCase(key)) {
                            requestBody.put(key, Integer.parseInt(value));
                        } else {
                            requestBody.put(key, value);
                        }
                    }
                    requestBodyList.add(requestBody);
                }
                rowCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
        return requestBodyList;
    }
}
