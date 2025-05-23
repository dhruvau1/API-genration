package com.mphasis.app.model;

import com.mphasis.app.support.SupportFunction;
import java.util.List;

public class Column {
	private String name;
	private String dataType;
	private List<String> validationConstraints;
	private List<String> databaseConstraints;
	private SupportFunction support = new SupportFunction();

	public Column() {

	}

	public Column(String name, String dataType, List<String> validationConstraints, List<String> databaseConstraints) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.validationConstraints = validationConstraints;
		this.databaseConstraints = databaseConstraints;
	}

	public String getSQLName() {
		return name;
	}

	public String getJavaName() {
		return support.toCamelCase(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public List<String> getvalidationConstraints() {
		return validationConstraints;
	}

	public void setvalidationConstraints(List<String> validationConstraints) {
		this.validationConstraints = validationConstraints;
	}

	public List<String> getdatabaseConstraints() {
		return databaseConstraints;
	}

	public void setdatabaseConstraints(List<String> databaseConstraints) {
		this.databaseConstraints = databaseConstraints;
	}

	@Override
	public String toString() {
		return "Column [name=" + name
				+ ", dataType=" + dataType
				+ ", validationConstraints=" + validationConstraints
				+ ", databaseConstraints=" + databaseConstraints
				+ "]";
	}
}
