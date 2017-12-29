package org.head.cloud.connection.handler;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ITypeHandler<T> {
	
	 void setParament(PreparedStatement ps,T params) throws SQLException;
	
	 T getResult(ResultSet set,String colName,JDBCType sourceType,JDBCType destJDbcType);

}
