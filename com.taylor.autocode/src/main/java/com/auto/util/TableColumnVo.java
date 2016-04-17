package com.auto.util;

/**
 * @notes:表列名描述类
 *
 * @author taylor
 *
 * 2015-7-14	下午4:21:52
 */
public class TableColumnVo {
	
	//列 是否是主键				如：0=非主键	1=主键
	public Integer isPk = 0;

	//列名						如：user_name
	public String columnName;
	
	//列数据库类型名称				如：VARCHAR
	public String columnTypeName;
	
	//列对应的类型值				如：12
	public Integer columnType4SqlType;
	
	//列 实体类名称				如：userName
	public String propertyName;
	
	//列对应的Java类型名称			如：String
	public String propertyType;
	
	//列对应的Java类型全名称		如：java.lang.String
	public String propertyTypeAll;
	
	//列 get方法名称				如：getUserName
	public String propertyGetMethod;
	
	//列 set方法名称				如：setUserName
	public String propertySetMethod;
	
	//Mapper参数描述				如：#{userName,jdbcType=VARCHAR}
	public String mapperArg;

	public Integer getIsPk() {
		return isPk;
	}

	public void setIsPk(Integer isPk) {
		this.isPk = isPk;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnTypeName() {
		switch (columnType4SqlType) {
			case java.sql.Types.INTEGER:columnTypeName="INTEGER";
				break;
			case java.sql.Types.TIMESTAMP:columnTypeName="TIMESTAMP";
				break;
			case java.sql.Types.DATE:columnTypeName="TIMESTAMP";
				break;
			case java.sql.Types.BLOB:columnTypeName="VARCHAR";
				break;
			case java.sql.Types.CLOB:columnTypeName="VARCHAR";
				break;
		}
		return columnTypeName;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	public Integer getColumnType4SqlType() {
		return columnType4SqlType;
	}

	public void setColumnType4SqlType(Integer columnType4SqlType) {
		this.columnType4SqlType = columnType4SqlType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertyTypeAll() {
		return propertyTypeAll;
	}

	public void setPropertyTypeAll(String propertyTypeAll) {
		this.propertyTypeAll = propertyTypeAll;
	}

	public String getPropertyGetMethod() {
		return propertyGetMethod;
	}

	public void setPropertyGetMethod(String propertyGetMethod) {
		this.propertyGetMethod = propertyGetMethod;
	}

	public String getPropertySetMethod() {
		return propertySetMethod;
	}

	public void setPropertySetMethod(String propertySetMethod) {
		this.propertySetMethod = propertySetMethod;
	}

	public String getMapperArg() {
		mapperArg = "#{" + propertyName + ",jdbcType=" + columnTypeName + "}";
		return mapperArg;
	}

	public void setMapperArg(String mapperArg) {
		this.mapperArg = mapperArg;
	}
}