package com.mphasis.app;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.app.create.CreateController;
import com.mphasis.app.create.CreateDatabase;
import com.mphasis.app.create.CreateEntity;
import com.mphasis.app.create.CreateRepo;
import com.mphasis.app.create.CreateService;
import com.mphasis.app.model.Table;
import com.mphasis.app.support.SupportFunction;
import com.mphasis.app.test.CreateTest;

public class App {

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();
		Table table = null;
		SupportFunction support = new SupportFunction();
		CreateDatabase createDB = new CreateDatabase();

		String jsonPath = args[0];
		String savePath = args[1];
		
	

		try {
			table = objectMapper.readValue(new File(jsonPath), Table.class);
			System.out.println("Found Table");
		} catch (Exception e) {
			System.out.println("Error reading JSON: " + e.getMessage());
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

			try{
				createDB.createQuery(jsonPath);
			}catch(Exception e){
				System.out.println("Error generating SQL: " + e.getMessage());
			}
			
			try {
				String filePath = savePath + "/model/" + support.capitalizedName(table.getTableName()) + "Model.java";
				Path path = Paths.get(filePath);
				String content = createEntity.GetString(table);
				Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
						StandardOpenOption.APPEND);
				System.out.println("Created Model");
			} catch (Exception e) {
				System.out.println("Error writing entity: " + e.getMessage());
			}

			try {
				String filePath = savePath + "/service/" + support.capitalizedName(table.getTableName())
						+ "Service.java";
				String content = createService.getString();
				Path path = Paths.get(filePath);
				Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
						StandardOpenOption.APPEND);
				System.out.println("Created Service");
			} catch (Exception e) {
				System.out.println("Error writing service: " + e.getMessage());
			}

			try {
				String filePath = savePath + "/controller/" + support.capitalizedName(table.getTableName())
						+ "Controller.java";
				String content = createController.GetString();
				Path path = Paths.get(filePath);
				Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
						StandardOpenOption.APPEND);
				System.out.println("Created Controller");
			} catch (Exception e) {
				System.out.println("Error writing controller: " + e.getMessage());
			}

			try {
				String filePath = savePath + "/repository/" + support.capitalizedName(table.getTableName())+ "Repo.java";
				String content = createRepo.GetString();
				Path path = Paths.get(filePath);
				Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
						StandardOpenOption.APPEND);
				System.out.println("Created Repo");
			} catch (Exception e) {
				System.out.println("Error writing controller: " + e.getMessage());
			}

			try {
				List<String> operations = table.getRequirement();
				String filePath = testSavePath + support.capitalizedName(table.getTableName()) + "ControllerTest.java";
				CreateTest createTest = new CreateTest(table.getTableName(), operations,table.getToPackage());
				createTest.setColumns(table.getColumns());
				String content = createTest.getString();
				Path path = Paths.get(filePath);
				Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
						StandardOpenOption.APPEND);
				System.out.println("Created Test");
			} catch (Exception e) {
				System.out.println("Error writing test: " + e.getMessage());
			}
		}
	}
}