package org.head.cloud.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.head.cloud.db.DataBaseType;

public class Table implements Serializable {

	private String tableName;
	private List<Column> cols;
	private List<String> pks;
	private List<ForeignKey> fkeys;
	private List<List<FieldValue>> valueLists;
	private DataBaseType soure;
	private DataBaseType dest;
	private String destConnName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Column> getCols() {
		return cols;
	}

	public void setCols(List<Column> cols) {
		this.cols = cols;
	}

	public List<String> getPks() {
		return pks;
	}

	public void setPks(List<String> pks) {
		this.pks = pks;
	}

	public List<ForeignKey> getFkeys() {
		return fkeys;
	}

	public void setFkeys(List<ForeignKey> fkeys) {
		this.fkeys = fkeys;
	}

	public List<List<FieldValue>> getValueLists() {
		return valueLists;
	}

	public void setValueLists(List<List<FieldValue>> valueLists) {
		this.valueLists = valueLists;
	}

	public DataBaseType getSoure() {
		return soure;
	}

	public void setSoure(DataBaseType soure) {
		this.soure = soure;
	}

	public DataBaseType getDest() {
		return dest;
	}

	public void setDest(DataBaseType dest) {
		this.dest = dest;
	}

	public String getDestConnName() {
		return destConnName;
	}

	public void setDestConnName(String destConnName) {
		this.destConnName = destConnName;
	}

}
