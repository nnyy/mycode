package org.head.cloud.util;

import java.io.Serializable;
import java.sql.JDBCType;

import org.head.cloud.db.DataBaseType;

public class FieldValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int index;
	private Object value;
	private int destJdbcType;
	private DataBaseType destDbType;
	private JAVAType javaType;

	public FieldValue() {
	}

	public FieldValue(int index, Object value, int destJdbcType, int sourcejdbcType, DataBaseType deBaseType) {
		this.index = index;
		this.value = value;
		// this.sourcejdbcType=
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getDestJdbcType() {
		return destJdbcType;
	}

	public void setDestJdbcType(int destJdbcType) {
		this.destJdbcType = destJdbcType;
	}

	public DataBaseType getDestDbType() {
		return destDbType;
	}

	public void setDestDbType(DataBaseType destDbType) {
		this.destDbType = destDbType;
	}

	public JAVAType getJavaType() {
		return javaType;
	}

	public void setJavaType(JAVAType javaType) {
		this.javaType = javaType;
	}

}
