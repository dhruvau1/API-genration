package com.mphasis.app.create;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class CreateDatabase {

    public static String generateCreateTableSQL(String jsonFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jsonFilePath));

        String schema = root.path("schema").asText();
        String tableName = root.path("tableName").asText();
        JsonNode columns = root.path("columns");

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(schema).append(".").append(tableName).append(" (\n");

        for (int i = 0; i < columns.size(); i++) {
            JsonNode column = columns.get(i);
            String name = column.path("name").asText();
            String dataType = column.path("dataType").asText();
            String dbConstraints = column.path("databaseConstraints").asText();

            sb.append("    ").append(name).append(" ").append(dataType);

            if (!dbConstraints.isEmpty()) {
                String[] constraintsParts = dbConstraints.split(",");
                for (String part : constraintsParts) {
                    part = part.trim();
                    if (!part.isEmpty()) {
                        sb.append(" ").append(part);
                    }
                }
            }

            if (i < columns.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(");");
        return sb.toString();
    }

    public void createQuery(String jsonPath) {

        String jdbcUrl = "jdbc:postgresql://localhost:5432/dataproduct";
        String username = "postgres";
        String password = "postgres";

        try {
            String sql = generateCreateTableSQL(jsonPath);

            // Establishing a connection and executing the create query
            try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
                    Statement stmt = conn.createStatement()) {

                System.out.println("Executing SQL:\n" + sql);
                stmt.execute(sql);
                System.out.println("Table created successfully.");

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Failed to read JSON file: " + e.getMessage());
        }
    }
}
