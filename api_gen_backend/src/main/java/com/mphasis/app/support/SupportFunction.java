package com.mphasis.app.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mphasis.app.model.Column;

public class SupportFunction {

	public String toCamelCase(String sqlName) {
		StringBuilder result = new StringBuilder();
		boolean nextUpper = false;

		for (int i = 0; i < sqlName.length(); i++) {
			char c = sqlName.charAt(i);
			if (c == '_') {
				nextUpper = true;
			} else {
				if (nextUpper) {
					result.append(Character.toUpperCase(c));
					nextUpper = false;
				} else {
					result.append(Character.toLowerCase(c));
				}
			}
		}

		return result.toString();
	}

	public String repoConvertion(String action) {
		Map<String, String> ACTION_TO_JPA_TERM = new HashMap<>();
		ACTION_TO_JPA_TERM.put("count", "countBy");
		ACTION_TO_JPA_TERM.put("getall", "findAll");
		ACTION_TO_JPA_TERM.put("ascending", "Asc");
    	ACTION_TO_JPA_TERM.put("descending", "Desc");

		return ACTION_TO_JPA_TERM.get(action);
	}


	public String getServiceFunctionName(List<String> actions) {
		StringBuilder builder = new StringBuilder();
		for (String action : actions) {
			String javaName = toCamelCase(action);
			if (javaName == null || javaName.isEmpty())
				continue;
			builder.append(Character.toUpperCase(javaName.charAt(0)));
			if (javaName.length() > 1) {
				builder.append(javaName.substring(1));
			}
		}
		return builder.toString();
	}

	public String capitalizedName(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public String getConstraintsAnnotations(List<String> constraints, String columnName, String sqlName) {
		if (constraints == null || constraints.isEmpty()) {
			return "";
		}

		StringBuilder annotations = new StringBuilder();
	

		for (String rawPart : constraints) {
			String part = rawPart.trim().toUpperCase();

			switch (part) {
				case "NOT NULL":
					annotations.append("@NotNull(message = \"").append(columnName).append(" should not be null\")\n");
					break;

				case "UNIQUE":
					annotations.append("@Column(unique = true, name = \"").append(sqlName).append("\")\n");
					break;

				case "NOT BLANK":
					annotations.append("@NotBlank(message = \"").append(columnName).append(" is mandatory.\")\n");
					break;

				case "EMAIL":
					annotations.append("@Email\n");
					break;

				case "PRIMARY KEY":
					annotations.append("@Id\n");
					annotations.append("@Column(name = \"").append(sqlName).append("\")\n");
					break;

				default:
					if (part.startsWith("MIN=")) {
						try {
							int minValue = Integer.parseInt(part.substring(4).trim());
							annotations.append("\t@Min(value = ").append(minValue)
									.append(", message = \"").append(columnName)
									.append(" must be at least ").append(minValue).append("\")\n");
						} catch (Exception e) {
							System.err.println("Invalid MIN constraint format: " + part);
						}
					} else if (part.startsWith("MAX=")) {
						try {
							int maxValue = Integer.parseInt(part.substring(4).trim());
							annotations.append("\t@Max(value = ").append(maxValue)
									.append(", message = \"").append(columnName)
									.append(" must be at most ").append(maxValue).append("\")\n");
						} catch (Exception e) {
							System.err.println("Invalid MAX constraint format: " + part);
						}
					} else if (part.startsWith("SIZE=")) {
						try {
							String sizeRange = part.substring(5).trim();
							String[] minMax = sizeRange.split("-");
							int min = Integer.parseInt(minMax[0].trim());
							int max = Integer.parseInt(minMax[1].trim());
							annotations.append("\t@Size(min = ").append(min)
									.append(", max = ").append(max)
									.append(", message = \"").append(sqlName)
									.append(" must be at most ").append(max)
									.append(" characters, and should have at least ").append(min)
									.append(" character").append(min > 1 ? "s" : "")
									.append("\")\n");
						} catch (Exception e) {
							System.err.println("Invalid SIZE constraint format: " + part);
						}
					} else if (part.startsWith("PATTERN=")) {
						try {
							String regex = rawPart.trim().substring(8);
							annotations.append("\t@Pattern(regexp = \"")
									.append(regex)
									.append("\", message = \"")
									.append(sqlName)
									.append(" must match the required format\")\n");
						} catch (Exception e) {
							System.err.println("Invalid PATTERN constraint: " + part);
						}
					}
					break;
			}
		}

		return annotations.toString().trim();
	}

	public String getSavePath(String filePath, String packageName) {
		String path = filePath + "src\\main\\java\\" + packageName.replace('.', '\\');
		return path;
	}

	public String getTestPath(String filePath, String packageName) {
		String path = filePath + "src\\test\\java\\" + packageName.replace('.', '\\');
		return path;
	}

	public String getBasePackage(String packageName) {
		if (packageName == null || packageName.isEmpty()) {
			return "";
		}
		String[] parts = packageName.split("\\.");
		return parts.length > 1 ? parts[1] : "";
	}

	public String dataTypeConverter(String datatype) {
		Map<String, String> SQL_TO_JAVA_TYPE_MAP = new HashMap<>();
		SQL_TO_JAVA_TYPE_MAP.put("varchar", "String");
		SQL_TO_JAVA_TYPE_MAP.put("varchar2", "String");
		SQL_TO_JAVA_TYPE_MAP.put("char", "String");
		SQL_TO_JAVA_TYPE_MAP.put("text", "String");
		SQL_TO_JAVA_TYPE_MAP.put("number", "int");
		SQL_TO_JAVA_TYPE_MAP.put("integer", "int");
		SQL_TO_JAVA_TYPE_MAP.put("int", "int");
		SQL_TO_JAVA_TYPE_MAP.put("smallint", "int");
		SQL_TO_JAVA_TYPE_MAP.put("serial", "long");
		SQL_TO_JAVA_TYPE_MAP.put("bigint", "long");
		SQL_TO_JAVA_TYPE_MAP.put("bigserial", "long");
		SQL_TO_JAVA_TYPE_MAP.put("float", "float");
		SQL_TO_JAVA_TYPE_MAP.put("double", "double");
		SQL_TO_JAVA_TYPE_MAP.put("real", "double");
		SQL_TO_JAVA_TYPE_MAP.put("date", "LocalDateTime");
		SQL_TO_JAVA_TYPE_MAP.put("timestamp", "LocalDateTime");
		SQL_TO_JAVA_TYPE_MAP.put("boolean", "boolean");
		SQL_TO_JAVA_TYPE_MAP.put("tinyint", "boolean");
		SQL_TO_JAVA_TYPE_MAP.put("numeric", "BigDecimal");
		SQL_TO_JAVA_TYPE_MAP.put("decimal", "BigDecimal");
		SQL_TO_JAVA_TYPE_MAP.put("jsonb", "JsonNode");
		return SQL_TO_JAVA_TYPE_MAP.getOrDefault(datatype.toLowerCase(), "String");
	}

	public String getColumnDatatype(String field, List<Column> col) {
		for (Column c : col) {
			if (c.getName().equals(field)) {
				return dataTypeConverter(c.getDataType());
			}
		}
		return "INVALID_DATA_TYPE";
	}

	public String getVariableAnnotations(String type)
	{
		Map<String, String> MAP_TO_ANNOTATION = new HashMap<>();
		MAP_TO_ANNOTATION.put("requestParams", "@RequestParams");
		MAP_TO_ANNOTATION.put("path", "@PathVariable");
		MAP_TO_ANNOTATION.put("requestBody", "@RequestBody");

		return MAP_TO_ANNOTATION.get(type);
	}

	public String getMappingAnnotations(String mapping) {
		Map<String, String> MAP_TO_ANNOTATION = new HashMap<>();
		MAP_TO_ANNOTATION.put("GET", "@GetMapping");
		MAP_TO_ANNOTATION.put("POST", "@PostMapping");
		MAP_TO_ANNOTATION.put("PUT", "@PutMapping");
		MAP_TO_ANNOTATION.put("DELETE", "@DeleteMapping");
		MAP_TO_ANNOTATION.put("PATCH", "@PatchMapping");

		return MAP_TO_ANNOTATION.getOrDefault(mapping.toUpperCase(), "// Unknown HTTP method");
	}

}
