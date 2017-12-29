package org.head.cloud.util;

import java.io.Serializable;
import java.util.List;

public class Column implements Serializable {
	private String columnName;
	private int SqlType;
	private String typeName;
	private int columnSize;
	private int decimalDigits;
	private int nullLable = 3;
	private int isPrimarykey;
	private int isForeignKey;
	private int isAutoIncrement;
	private String defaultValue;

	public Column() {
	}

	public Column(String cname, int sqltype, String typename, int colsize, int nulllable, int primarykey,
			int foreignkey, int digits) {
		this.columnName = cname;
		this.SqlType = sqltype;
		this.typeName = typename;
		this.columnSize = colsize;
		this.nullLable = nulllable;
		this.isForeignKey = foreignkey;
		this.isPrimarykey = primarykey;
		this.decimalDigits = digits;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getSqlType() {
		return SqlType;
	}

	public void setSqlType(int sqlType) {
		SqlType = sqlType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public int getNullLable() {
		return nullLable;
	}

	public void setNullLable(int nullLable) {
		this.nullLable = nullLable;
	}

	public int getIsPrimarykey() {
		return isPrimarykey;
	}

	public void setIsPrimarykey(int isPrimarykey) {
		this.isPrimarykey = isPrimarykey;
	}

	public int getIsForeignKey() {
		return isForeignKey;
	}

	public void setIsForeignKey(int isForeignKey) {
		this.isForeignKey = isForeignKey;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public int getIsAutoIncrement() {
		return isAutoIncrement;
	}

	public void setIsAutoIncrement(int isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	// private

}
