package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class CsvFeatureLoader {

    private static final String FEATURE_DIR = "src/test/resources/features/";
    private static final String DATA_DIR = "src/test/resources/dataprofiles/";

    public static void preprocessAllFeatures() throws Exception {
        // Xóa các file _generated.feature cũ
        Files.walk(Paths.get(FEATURE_DIR))
                .filter(p -> p.toString().endsWith("_generated.feature"))
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        // Khôi phục .disabled → .feature
        Files.walk(Paths.get(FEATURE_DIR))
                .filter(p -> p.toString().endsWith(".feature.disabled"))
                .forEach(p -> {
                    try {
                        Path restored = Paths.get(p.toString().replace(".feature.disabled", ".feature"));
                        Files.move(p, restored, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        // Duyệt tất cả feature có chứa @csv
        List<Path> featureFiles = Files.walk(Paths.get(FEATURE_DIR))
                .filter(p -> p.toString().endsWith(".feature"))
                .filter(CsvFeatureLoader::containsCsvTag) // chỉ lấy file có @csv
                .collect(Collectors.toList());

        String env = System.getProperty("env", "stg");
        for (Path featurePath : featureFiles) {
            preprocessFeature(featurePath.toString(), env);
        }
    }

    /** Chỉ xử lý file có @csv */
    private static boolean containsCsvTag(Path path) {
        try {
            return Files.lines(path).anyMatch(line -> line.trim().startsWith("@csv"));
        } catch (IOException e) {
            return false;
        }
    }

    public static void preprocessFeature(String featurePath, String env) throws Exception {
        Path featureFile = Paths.get(featurePath);
        List<String> lines = Files.readAllLines(featureFile);
        List<String> output = new ArrayList<>();

        String featureName = featureFile.getFileName().toString().replace(".feature", "");
        String generatedPath = featurePath.replace(".feature", "_generated.feature");

        String currentCsvFile = null;
        List<String> currentScenario = new ArrayList<>();

        for (String rawLine : lines) {
            String line = rawLine.trim();

            if (line.startsWith("@csv")) {
                if (!currentScenario.isEmpty()) {
                    appendScenario(output, currentScenario, currentCsvFile, env);
                    currentScenario.clear();
                }
                currentCsvFile = line.contains(":")
                        ? line.split(":", 2)[1].trim()
                        : featureFile.getFileName().toString().replace(".feature", ".csv");
                continue;
            }

            if (line.startsWith("Scenario:") || line.startsWith("Scenario Outline:")) {
                if (!currentScenario.isEmpty()) {
                    appendScenario(output, currentScenario, currentCsvFile, env);
                    currentScenario.clear();
                    currentCsvFile = null;
                }
            }

            currentScenario.add(rawLine);
        }

        if (!currentScenario.isEmpty()) {
            appendScenario(output, currentScenario, currentCsvFile, env);
        }

        // Ghi file mới
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(generatedPath))) {
            for (String out : output) {
                writer.write(out);
                writer.newLine();
            }
        }

        // Ẩn file gốc
        Files.move(featureFile, Paths.get(featurePath + ".disabled"), StandardCopyOption.REPLACE_EXISTING);
    }

    private static void appendScenario(List<String> output, List<String> scenarioLines, String csvFile, String env) throws IOException {
        if (csvFile == null) {
            output.addAll(scenarioLines);
            output.add("");
            return;
        }

        String csvPath = String.format("%s%s/%s", DATA_DIR, env, csvFile);
        File csv = new File(csvPath);
        if (!csv.exists()) {
            csvPath = String.format("%sstg/%s", DATA_DIR, csvFile);
            csv = new File(csvPath);
        }

        List<String[]> rows = new ArrayList<>();
        if (csv.exists()) {
            rows = Files.lines(csv.toPath())
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
        }

        for (String line : scenarioLines) {
            if (line.trim().startsWith("Scenario:")) {
                output.add(line.replace("Scenario:", "Scenario Outline:"));
            } else {
                output.add(line);
            }
        }

        if (!rows.isEmpty()) {
            output.add("  Examples:");
            for (String[] row : rows) {
                List<String> trimmed = new ArrayList<>();
                for (String cell : row) trimmed.add(cell.trim());
                output.add("    | " + String.join(" | ", trimmed) + " |");
            }
        }
        output.add("");
    }
}
