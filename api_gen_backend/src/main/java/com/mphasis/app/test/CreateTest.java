package com.mphasis.app.test;
import java.util.List;
import com.mphasis.app.model.Column;

public class CreateTest {

    private final String tableName;
    private final List<String> operations;
    private final String packageName;
    private List<Column> columns;  // changed from JsonNode to List<Column>

    public CreateTest(String tableName, List<String> operations, String packageName) {
        this.tableName = tableName;
        this.operations = operations;
        this.packageName = packageName;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getString() {
        String className = capitalize(tableName) + "ControllerTests";
        String modelClass = capitalize(tableName) + "Model";
        String controllerClass = capitalize(tableName) + "Controller";

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n")
          .append("import static org.junit.jupiter.api.Assertions.*;\n")
          .append("import org.junit.jupiter.api.Test;\n")
          .append("import org.springframework.beans.factory.annotation.Autowired;\n")
          .append("import org.springframework.boot.test.context.SpringBootTest;\n")
          .append("import org.springframework.http.ResponseEntity;\n")
          .append("import java.time.LocalDate;\n")
          .append("import java.util.List;\n")
          .append("import ").append(packageName).append(".").append(controllerClass).append(";\n")
          .append("import ").append(packageName).append(".model.").append(modelClass).append(";\n\n");

        sb.append("@SpringBootTest\n")
          .append("public class ").append(className).append(" {\n\n")
          .append("    @Autowired\n")
          .append("    private ").append(controllerClass).append(" controller;\n\n")

          .append("    @Test\n")
          .append("    void contextLoads() {\n")
          .append("        assertNotNull(controller);\n")
          .append("    }\n\n");

        for (String method : operations) {
            switch (method) {
                case "add" -> sb.append(generateAddTest(modelClass));
                case "getById" -> sb.append(generateGetByIdTest(modelClass));
                case "getAll" -> sb.append(generateGetAllTest());
                case "count" -> sb.append(generateCountTest());
            }
        }

        sb.append(generateCreateModelMethod(modelClass));
        sb.append("}\n");
        return sb.toString();
    }

    private String generateAddTest(String modelClass) {
        return String.format("""
            @Test
            void addShouldAddEntrySuccessfully() {
                var model = createTestModel();
                ResponseEntity<?> response = controller.add(model);
                assertNotNull(response);
                assertTrue(response.getBody() instanceof %s);
            }\n\n""", modelClass);
    }

    private String generateGetByIdTest(String modelClass) {
        return String.format("""
            @Test
            void getByIdShouldReturnCorrectEntry() {
                var model = controller.add(createTestModel()).getBody();
                ResponseEntity<?> response = controller.getById(((%s) model).getId());
                assertNotNull(response);
                assertEquals(((%s) model).getId(), ((%s) response.getBody()).getId());
            }\n\n""", modelClass, modelClass, modelClass);
    }

    private String generateGetAllTest() {
        return """
            @Test
            void getAllShouldReturnList() {
                controller.add(createTestModel());
                ResponseEntity<?> response = controller.getAll();
                assertNotNull(response);
                assertTrue(response.getBody() instanceof List<?>);
            }\n\n""";
    }

    private String generateCountTest() {
        return """
            @Test
            void countShouldReturnPositive() {
                controller.add(createTestModel());
                ResponseEntity<?> response = controller.getCount();
                assertNotNull(response);
                assertTrue(((Long) response.getBody()) > 0);
            }\n\n""";
    }

    private String generateCreateModelMethod(String modelClass) {
        StringBuilder sb = new StringBuilder();
        sb.append("    private ").append(modelClass).append(" createTestModel() {\n")
          .append("        ").append(modelClass).append(" model = new ").append(modelClass).append("();\n");

        if (columns != null) {
            for (Column column : columns) {
                String name = column.getName();
                String dataType = column.getDataType();

                if ("id".equalsIgnoreCase(name)) continue;

                sb.append("        model.set").append(capitalize(name)).append("(");
                switch (dataType.toLowerCase()) {
                    case "varchar(255)", "text" -> sb.append("\"Test ").append(name).append("\"");
                    case "bigint", "bigserial" -> sb.append("1L");
                    case "date" -> sb.append("LocalDate.now()");
                    case "int", "integer" -> sb.append("123");
                    case "boolean" -> sb.append("true");
                    default -> sb.append("null");
                }
                sb.append(");\n");
            }
        }

        sb.append("        return model;\n")
          .append("    }\n\n");

        return sb.toString();
    }

    private static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
