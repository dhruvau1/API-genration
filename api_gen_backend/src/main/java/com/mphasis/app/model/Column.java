package com.mphasis.app.model;

import com.mphasis.app.support.SupportFunction;

public class Column {
	private String name;
	private String dataType;
	private String validationConstraints;
	private String databaseConstraints;
	private SupportFunction support = new SupportFunction();

	public Column() {

	}

	public Column(String name, String dataType, String validationConstraints, String databaseConstraints) {
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

	public String getvalidationConstraints() {
		return validationConstraints;
	}

	public void setvalidationConstraints(String validationConstraints) {
		this.validationConstraints = validationConstraints;
	}

	public String getdatabaseConstraints() {
		return databaseConstraints;
	}

	public void setdatabaseConstraints(String databaseConstraints) {
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
