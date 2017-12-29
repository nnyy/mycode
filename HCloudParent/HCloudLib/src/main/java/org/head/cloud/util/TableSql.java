package org.head.cloud.util;

import java.util.ArrayList;
import java.util.List;

public class TableSql {

	private String tbName;
	private String createSql;
	private String insertSql;
	private List<List<FieldValue>> valueList;
	private String destConnName;

	public String getTbName() {
		return tbName;
	}

	public void setTbName(String tbName) {
		this.tbName = tbName;
	}

	public String getCreateSql() {
		return createSql;
	}

	public void setCreateSql(String createSql) {
		this.createSql = createSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public List<List<FieldValue>> getValueList() {
		return valueList;
	}

	public void setValueList(List<List<FieldValue>> valueList) {
		this.valueList = valueList;
	}

	public String getDestConnName() {
		return destConnName;
	}

	public void setDestConnName(String destConnName) {
		this.destConnName = destConnName;
	}

}
