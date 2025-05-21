package com.mphasis.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.app.create.CreateController;
import com.mphasis.app.create.CreateDatabase;
import com.mphasis.app.create.CreateEntity;
import com.mphasis.app.create.CreateRepo;
import com.mphasis.app.create.CreateService;
import com.mphasis.app.model.Table;
import com.mphasis.app.support.SupportFunction;
import com.mphasis.app.test.CreateTest;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class APIAutomation {

    public static void run(JsonNode inputJson, String savePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        Table table = null;
        SupportFunction support = new SupportFunction();
        CreateDatabase createDB = new CreateDatabase();

        try {
            // Convert JsonNode to Table object
            table = objectMapper.treeToValue(inputJson, Table.class);
            System.out.println("Parsed Table from JSON");
        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
            return;
        }

        if (table != null) {
            CreateEntity createEntity = new CreateEntity();
            CreateService createService = new CreateService(table);
            CreateController createController = new CreateController(table);
            CreateRepo createRepo = new CreateRepo(table);

            String testSavePath = support.getTestPath(savePath, table.getToPackage());
            if (!testSavePath.endsWith(File.separator)) {
                testSavePath += File.separator;
            }

            savePath = support.getSavePath(savePath, table.getToPackage());

            try {
                // If createQuery still needs raw JSON as a file, you may need to adjust this
                createDB.createQuery(inputJson.toString());
            } catch (Exception e) {
                System.out.println("Error generating SQL: " + e.getMessage());
            }

            try {
                String filePath = savePath + "/model/" + support.capitalizedName(table.getTableName()) + "Model.java";
                String content = createEntity.GetString(table);
                Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Created Model");
            } catch (Exception e) {
                System.out.println("Error writing entity: " + e.getMessage());
            }

            try {
                String filePath = savePath + "/service/" + support.capitalizedName(table.getTableName()) + "Service.java";
                String content = createService.getString();
                Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Created Service");
            } catch (Exception e) {
                System.out.println("Error writing service: " + e.getMessage());
            }

            try {
                String filePath = savePath + "/controller/" + support.capitalizedName(table.getTableName()) + "Controller.java";
                String content = createController.GetString();
                Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Created Controller");
            } catch (Exception e) {
                System.out.println("Error writing controller: " + e.getMessage());
            }

            try {
                String filePath = savePath + "/repository/" + support.capitalizedName(table.getTableName()) + "Repo.java";
                String content = createRepo.GetString();
                Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Created Repo");
            } catch (Exception e) {
                System.out.println("Error writing repo: " + e.getMessage());
            }

            try {
                List<String> operations = table.getRequirement();
                String filePath = testSavePath + support.capitalizedName(table.getTableName()) + "ControllerTest.java";
                CreateTest createTest = new CreateTest(table.getTableName(), operations, table.getToPackage());
                createTest.setColumns(table.getColumns());
                String content = createTest.getString();
                Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("Created Test");
            } catch (Exception e) {
                System.out.println("Error writing test: " + e.getMessage());
            }
        }
    }
}
