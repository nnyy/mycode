package org.head.cloud.db;

public enum DataBaseType {

	MSSQL("mssql", 1), ORACLE("oracle", 2), MYSQL("mysql", 3), PHOENIX("phoenix", 4), HIVE("hive", 5), DB2("db2",
			6), POSTGRESQL("postgreSQL", 7),SYSBASE("sysbase",8),INFORMIX("informix",9);

	private final String value;

	private final int index;

	private DataBaseType(String value, int index) {
		this.value = value;
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name();
	}

	public static DataBaseType getDataBaseTypeByIndex(int i) {
		for (DataBaseType dt : values()) {
			if (dt.getIndex() == i) {
				return dt;
			}
		}
		return null;
	}

	public static DataBaseType geBaseTypeByValue(String value) {
		for (DataBaseType dt : values()) {
			if (dt.getValue().equals(value)) {
				return dt;
			}
		}
		return null;
	}

	public String toString() {
		return value + "-" + index;
	}

}
