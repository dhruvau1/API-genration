package com.mphasis.app.create;

import java.util.List;

import com.mphasis.app.model.Column;
import com.mphasis.app.model.Table;
import com.mphasis.app.support.SupportFunction;

public class CreateEntity {

    private String table_name;
    private String schema;
    private Table tableObj;

    StringBuilder javaStringBuilder = new StringBuilder();
    StringBuilder fieldsBuilder = new StringBuilder();
    StringBuilder methodsBuilder = new StringBuilder();
    SupportFunction support = new SupportFunction();

    public CreateEntity() {
    }

    private void addPackage() {
        javaStringBuilder.append("package ").append(tableObj.getToPackage()).append(".model;\n\n");
    }

    private void addDefaultImports() {
        javaStringBuilder.append("import jakarta.persistence.*;\n");
        javaStringBuilder.append("import jakarta.validation.constraints.*;\n");
        javaStringBuilder.append("import java.time.LocalDateTime;\n\n");
    }

    private void addBoilerPlate() {
        javaStringBuilder.append("@Entity\n");
        javaStringBuilder.append("@Table(name = \"").append(tableObj.getTableName().toLowerCase())
                .append("\", schema = \"").append(schema).append("\")\n");
        javaStringBuilder.append("public class ").append(table_name).append("Model {\n\n");
    }

    private void addConstructor() {
        methodsBuilder.append("\tpublic ").append(table_name).append("Model() {}\n\n");

        List<Column> cols = tableObj.getColumns();
        if (!cols.isEmpty()) {
            methodsBuilder.append("\tpublic ").append(table_name).append("Model(");
            for (int i = 0; i < cols.size(); i++) {
                Column column = cols.get(i);
                String datatype = support.dataTypeConverter(column.getDataType());
                methodsBuilder.append(datatype).append(" ").append(column.getJavaName());
                if (i < cols.size() - 1) {
                    methodsBuilder.append(", ");
                }
            }
            methodsBuilder.append(") {\n");
            for (Column column : cols) {
                methodsBuilder.append("\t\tthis.").append(column.getJavaName()).append(" = ")
                        .append(column.getJavaName()).append(";\n");
            }
            methodsBuilder.append("\t}\n\n");
        }
    }

    private void addColumnData() {
        List<Column> cols = tableObj.getColumns();

        for (Column column : cols) {
            addDataType(column);
        }

        for (Column column : cols) {
            addGetter(column);
            addSetter(column);
        }
    }

    private void addDataType(Column column) {
        String datatype = support.dataTypeConverter(column.getDataType());
        String javaName = column.getJavaName();
        String sqlName = column.getSQLName();
        String constraints = support.getConstraintsAnnotations(column.getvalidationConstraints(), javaName, sqlName).trim();
        boolean isPrimaryKey = column.getvalidationConstraints().contains("PRIMARY KEY");
        boolean isUnique = column.getvalidationConstraints().contains("UNIQUE");
    
        // Add @Id and @GeneratedValue for primary key
        if (isPrimaryKey) {
            fieldsBuilder.append("\t@Id\n");
            fieldsBuilder.append("\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n");
        }
    
        if (!isUnique) {
            fieldsBuilder.append("\t@Column(name = \"").append(column.getSQLName()).append("\")\n");
        }
    
        
        if (!constraints.isEmpty()) {
            fieldsBuilder.append("\t").append(constraints).append("\n");
        }
    
        fieldsBuilder.append("\tprivate ").append(datatype).append(" ").append(javaName).append(";\n\n");
    }
    

    private void addGetter(Column column) {
        String columnName = column.getJavaName();
        String capitalized = capitalize(columnName);
        String datatype = support.dataTypeConverter(column.getDataType());
        String getterPrefix = datatype.equalsIgnoreCase("boolean") ? "is" : "get";

        methodsBuilder.append("\tpublic ").append(datatype).append(" ").append(getterPrefix)
                .append(capitalized).append("() {\n")
                .append("\t\treturn ").append(columnName).append(";\n")
                .append("\t}\n\n");
    }

    private void addSetter(Column column) {
        String columnName = column.getJavaName();
        String capitalized = capitalize(columnName);
        String datatype = support.dataTypeConverter(column.getDataType());

        methodsBuilder.append("\tpublic void set").append(capitalized).append("(").append(datatype)
                .append(" ").append(columnName).append(") {\n")
                .append("\t\tthis.").append(columnName).append(" = ").append(columnName).append(";\n")
                .append("\t}\n\n");
    }

    public String GetString(Table table) {
        this.table_name = support.capitalizedName(table.getTableName());
        this.schema = table.getSchema();
        this.tableObj = table;

        addPackage();
        addDefaultImports();
        addBoilerPlate();
        addColumnData();
        addConstructor();

        javaStringBuilder.append(fieldsBuilder);
        javaStringBuilder.append("\n");
        javaStringBuilder.append(methodsBuilder);
        javaStringBuilder.append("}");

        return javaStringBuilder.toString();
    }

    private String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
