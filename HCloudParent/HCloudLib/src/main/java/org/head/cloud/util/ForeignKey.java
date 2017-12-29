package org.head.cloud.util;

import java.io.Serializable;

public class ForeignKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 536585453214883400L;

	private String pkColumnName;
	private String pkTableName;
	private String foreignKeyColumnName;
	private String foreignKeyTable;
	private String foreignKeyName;

	public String getForeignKeyColumnName() {
		return foreignKeyColumnName;
	}

	public void setForeignKeyColumnName(String foreignKeyColumnName) {
		this.foreignKeyColumnName = foreignKeyColumnName;
	}

	public String getPkColumnName() {
		return pkColumnName;
	}

	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}

	public String getPkTableName() {
		return pkTableName;
	}

	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}

	public String getForeignKeyTable() {
		return foreignKeyTable;
	}

	public void setForeignKeyTable(String foreignKeyTable) {
		this.foreignKeyTable = foreignKeyTable;
	}

	public String getForeignKeyName() {
		return foreignKeyName;
	}

	public void setForeignKeyName(String foreignKeyName) {
		this.foreignKeyName = foreignKeyName;
	}
	
	

}
