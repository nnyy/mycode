package org.head.cloud.util;

import org.head.cloud.db.DataBaseType;

public enum JAVAType {

	OBJECT("Object",0),STRING("String", 1), INT("int", 2), BIGDECIMAL("BigDecimal", 3), BOOLEAN("boolean", 4), BYTE("byte", 5), SHORT(
			"short", 6), LONG("long", 7), FLOAT("float", 8), DOUBLE("double", 9), DATE("Date",
					10), TIME("Time", 11), TIMESTAMP("Timestamp", 12), CLOB("Clob",
							13), BYTEARRAY("ByteArray", 14), CHARARRAY("CharArray", 15), BLOB("Bolb", 16);
	private final String value;

	private final int index;

	private JAVAType(String value, int index) {
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

	public static JAVAType getJAVATypeByIndex(int i) {
		for (JAVAType dt : values()) {
			if (dt.getIndex() == i) {
				return dt;
			}
		}
		return null;
	}

	public static JAVAType geJAVATypeByValue(String value) {
		for (JAVAType dt : values()) {
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
