package org.head.cloud.connection.handler;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Time;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.head.cloud.db.DataBaseType;
import org.head.cloud.util.FieldValue;
import org.head.cloud.util.JAVAType;

public class FiedValueTypeHandler implements ITypeHandler<FieldValue> {

	@Override
	public void setParament(PreparedStatement ps, FieldValue params) throws SQLException {
		JAVAType jtype = params.getJavaType();
		int index = params.getIndex();
		Object value = params.getValue();
		if (null != value) {
			switch (jtype) {
			case STRING:
				ps.setString(index, params.getValue().toString());
				break;
			case BIGDECIMAL:
				ps.setBigDecimal(index, (BigDecimal) value);
				break;
			case LONG:
				ps.setLong(index, (long) value);
				break;
			case INT:
				ps.setInt(index, (int) value);
				break;
			case BYTE:
				ps.setByte(index, (byte) value);
				break;
			case SHORT:
				ps.setShort(index, (short) value);
				break;
			case FLOAT:
				ps.setFloat(index, (float) value);
				break;
			case DOUBLE:
				ps.setDouble(index, (float) value);
			case BOOLEAN:
				ps.setBoolean(index, (boolean) value);
				break;
			case DATE:
				ps.setDate(index, (Date) value);
				break;
			case TIME:
				ps.setTime(index, (Time) value);
				break;
			case TIMESTAMP:
				ps.setTimestamp(index, (Timestamp) value);
				break;
			case BYTEARRAY:
				byte[] bufs = (byte[]) value;
				ByteArrayInputStream bis = new ByteArrayInputStream(bufs);
				ps.setBinaryStream(index, bis);
				break;
			case CHARARRAY:
				char[] cs = (char[]) value;
				StringReader reader = new StringReader(new String(cs));
				ps.setCharacterStream(index, reader);
				break;
			default:
				ps.setObject(index, value);
				break;
			}
		} else {
			ps.setObject(index, value);
		}

	}

	@Override
	public FieldValue getResult(ResultSet set, String colName, JDBCType sourceType, JDBCType destJDbcType) {
		// TODO Auto-generated method stub
		// set.geto
		return null;
	}

}
