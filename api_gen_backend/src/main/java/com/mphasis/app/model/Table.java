package com.mphasis.app.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private String schema;
	private String tableName;
	private List<Column> columns = new ArrayList<Column>();
	private String toPackage;
	private List<String> requirement;
	private List<Services> services = new ArrayList<Services>();

	public Table() {
		
	}

	public Table(String schema, String tableName, List<Column> columns, String toPackage, List<String> requirement, List<Services> services) {
		this.schema = schema;
		this.tableName = tableName;
		this.toPackage = toPackage;
		this.requirement = requirement;
		
		if(columns != null)
			this.columns = columns;

		if (services != null) {
			this.services = services;
		}
	}

	public List<String> getRequirement() {
		return requirement;
	}

	public void setRequirement(List<String> requirement) {
		this.requirement = requirement;
	}

	public String getSchema() {
		return schema;
	}


	public String getTableName() {
		return tableName;
	}


	public List<Column> getColumns() {
		return columns;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getToPackage() {
		return toPackage;
	}

	public void setToPackage(String toPackage) {
		this.toPackage = toPackage;
	}

	public void setServices(List<Services> services) {
		this.services = services;
	}

	public List<Services> getServices() {
		return services;
	}

	@Override
	public String toString() {
		return "Table [schema=" + schema + ", tableName=" + tableName + ", columns=" + columns + "]";
	}
	
	
	public void addColumn(String name, String dataType, String validationConstraints, String databaseConstraints) {
		Column column = new Column(name, dataType, validationConstraints, databaseConstraints);
		
		columns.add(column);
	}
}
