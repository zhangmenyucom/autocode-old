package com.auto.util;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class DataUtils {

	private static Logger logger = Logger.getLogger(DataUtils.class);
	
	private static String db_url;
	private static String db_user;
	private static String db_pwd;

	@SuppressWarnings("static-access")
	public DataUtils(String db_url, String db_user, String db_pwd) {
		this.db_url = db_url;
		this.db_user = db_user;
		this.db_pwd = db_pwd;
	}

	/**
	 * @notes:获取数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 * @author taylor 2014-6-24 下午4:22:44
	 */
	public static Connection getDBConnection() throws SQLException {
		return DriverManager.getConnection(db_url, db_user, db_pwd);
	}

	/**
	 * @notes:JDBC执行SQL查询单个对象
	 * 
	 * @param sql
	 * @return
	 * @author taylor 2014-6-24 下午4:31:31
	 */
	public static Map<String, Object> executeQueryOneSQL(String sql) {
		Connection connt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connt = DataUtils.getDBConnection();
			stmt = connt.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);

			ResultSetMetaData rsmd = rs.getMetaData();// 元数据信息
			int count = rsmd.getColumnCount();// 字段数量
			String[] colNames = new String[count];

			for (int i = 1; i <= count; i++) {
				colNames[i - 1] = rsmd.getColumnName(i);// 字段名称
			}

			while (rs.next()) {
				Map<String, Object> item = new HashMap<String, Object>();
				for (int i = 1; i <= count; i++) {
					item.put(colNames[i - 1], rs.getObject(colNames[i - 1]));
				}
				return item;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (connt != null)
					connt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @notes:JDBC执行SQL查询多条对象
	 * 
	 * @param sql
	 * @return
	 * @author taylor 2014-6-24 下午5:40:46
	 */
	public static List<Map<String, Object>> executeQueryListSQL(String sql) {
		Connection connt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connt = DataUtils.getDBConnection();
			stmt = connt.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>(0);

			ResultSetMetaData rsmd = rs.getMetaData();// 元数据信息
			int count = rsmd.getColumnCount();// 字段数量
			String[] colNames = new String[count];

			for (int i = 1; i <= count; i++) {
				colNames[i - 1] = rsmd.getColumnName(i);// 字段名称
			}

			while (rs.next()) {
				Map<String, Object> item = new HashMap<String, Object>();
				for (int i = 1; i <= count; i++) {
					item.put(colNames[i - 1], rs.getObject(colNames[i - 1]));
				}
				dataList.add(item);
			}
			return dataList;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (connt != null)
					connt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @notes:执行数据更新操作方法
	 * 
	 * @param sql
	 *            insert|update SQL
	 * @return
	 * @author taylor 2014-6-24 下午4:46:14
	 */
	public static int executeMergerSQL(String sql) {
		Connection connt = null;
		PreparedStatement stmt = null;
		try {
			connt = DataUtils.getDBConnection();
			stmt = connt.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (connt != null)
					connt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @notes:根据传入的结果集返回结果集的元数据，以列名为键以列类型为值的map对象
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @author taylor 2014-6-24 下午5:40:06
	 */
	@SuppressWarnings("unused")
	private static Map<String, Integer> getMetaData(ResultSet rs) throws SQLException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		ResultSetMetaData metaData = rs.getMetaData();
		int numberOfColumns = metaData.getColumnCount();
		for (int column = 0; column < numberOfColumns; column++) {
			String columnName = metaData.getColumnLabel(column + 1);
			int colunmType = metaData.getColumnType(column + 1);
			map.put(columnName, colunmType);
		}
		return map;
	}

	/**
	 * @notes:将结果集封装为以列名存储的map对象
	 * @param rs
	 * @param metaDataMap元数据集合
	 * @return
	 * @throws Exception
	 * @author taylor 2014-6-24 下午5:39:49
	 */
	@SuppressWarnings("unused")
	private static Map<String, Object> setData(ResultSet rs, Map<String, Integer> metaDataMap) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		for (String columnName : metaDataMap.keySet()) {
			logger.info(columnName);
			int columnType = metaDataMap.get(columnName);
			Object object = rs.getObject(columnName);
			if (object == null) {
				map.put(columnName, null);
				continue;
			}
			// 以下并为对所有的数据类型做处理，未特殊处理的数据类型将以object的形式存储。
			switch (columnType) {
			case java.sql.Types.VARCHAR:
				map.put(columnName, object);
				break;
			case java.sql.Types.DATE:
				map.put(columnName, DateTools.getDateByString(object.toString(), "yyyy-MM-dd"));
				break;
			case java.sql.Types.TIMESTAMP:
				map.put(columnName, DateTools.getDateByString(object.toString(), "yyyy-MM-dd HH:mm:ss"));
				break;
			case java.sql.Types.TIME:
				map.put(columnName, DateTools.getDateByString(object.toString(), "HH:mm:ss"));
				break;
			case java.sql.Types.CLOB:
				try {
					if (object != null) {
						Clob clob = (Clob) object;
						long length = clob.length();
						map.put(columnName, clob.getSubString(1L, (int) length));
					}
				} catch (Exception e) {
					logger.error("将字段值从clob转换为字符串时出错@!", e);
				}
				break;
			case java.sql.Types.BLOB:
				map.put(columnName, "");
				break;
			default:
				map.put(columnName, object);
				break;
			}
		}
		return map;
	}

	/**
	 * @notes:获取表对应字段的相关信息
	 *
	 * @param tableName		表名
	 * @param packages		实体类import的类包Set对象
	 * @return
	 * @author	taylor
	 * 2015-7-15	下午3:07:15
	 */
	public static List<TableColumnVo> getTableColumnList(String tableName, Set<String> packages) {
		Connection connt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connt = DataUtils.getDBConnection();
			stmt = connt.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1;");
			if (null == rs) {
				return null;
			}
			
			/**
			 * 获取表中的主键字段
			 */
			ResultSet rst = connt.getMetaData().getPrimaryKeys(null,null, tableName);
			rst.next();
			String pkColumn = rst.getString("COLUMN_NAME");
			
			ResultSetMetaData data = rs.getMetaData();
			List<TableColumnVo> columnVos = new ArrayList<TableColumnVo>(0);
			for (int i = 1; i <= data.getColumnCount(); i++) {
				TableColumnVo columnVo = new TableColumnVo();
				// 获得指定列的列名
				String columnName = data.getColumnName(i);
				columnVo.setColumnName(columnName);
				if (columnName.equals(pkColumn)) {//主键
					columnVo.setIsPk(1);
				}
				String propertyName = StringTool.changeColumn2Property(columnName);
				columnVo.setPropertyName(propertyName);
				String indexPorpertyName = StringTool.toUpperCaseIndex(propertyName);
				columnVo.setPropertySetMethod("set" + indexPorpertyName);
				columnVo.setPropertyGetMethod("get" + indexPorpertyName);
				// 获得指定列的数据类型
				int columnType = data.getColumnType(i);
				columnVo.setColumnType4SqlType(columnType);
				// 获得指定列的数据类型名
				String columnTypeName = data.getColumnTypeName(i);
				columnVo.setColumnTypeName(columnTypeName);
				// 对应数据类型的类
				String columnClassName = data.getColumnClassName(i);
				if (java.sql.Types.TIMESTAMP == columnType) {//时间类型
					columnClassName = "java.util.Date";
					packages.add(columnClassName);
				} else if (java.sql.Types.BLOB == columnType || java.sql.Types.CLOB == columnType) {
					columnClassName = "java.lang.String";
				}
				columnVo.setPropertyTypeAll(columnClassName);
				String propertyType = columnClassName.contains("java.lang") ? columnClassName.replace("java.lang.", ""):columnClassName.replace("java.util.", "");
				columnVo.setPropertyType(propertyType);
				
				/*// 获得指定列的列值
				String columnValue = rs.getString(i);
				// 默认的列的标题
				String columnLabel = data.getColumnLabel(i);
				// 在数据库中类型的最大字符个数
				int columnDisplaySize = data.getColumnDisplaySize(i);
				// 所在的Catalog名字
				String catalogName = data.getCatalogName(i);
				logger.info("###########catalogName##########" + catalogName);
				// 获得列的模式
				String schemaName = data.getSchemaName(i);
				// 某列类型的精确度(类型的长度)
				int precision = data.getPrecision(i);
				// 小数点后的位数
				int scale = data.getScale(i);
				// 获取某列对应的表名
				String tableName4Rs = data.getTableName(i);
				// 是否自动递增
				boolean isAutoInctement = data.isAutoIncrement(i);
				// 在数据库中是否为货币型
				boolean isCurrency = data.isCurrency(i);
				// 是否为空
				int isNullable = data.isNullable(i);
				// 是否为只读
				boolean isReadOnly = data.isReadOnly(i);
				// 能否出现在where中
				boolean isSearchable = data.isSearchable(i);
				// 获得所有列的数目及实际列数
				int columnCount = data.getColumnCount();
				System.out.println(columnCount);
				System.out.println("获得列" + i + "的字段名称:" + columnName);
				System.out.println("获得列" + i + "的字段值:" + columnValue);
				System.out.println("获得列" + i + "的类型,返回SqlType中的编号:" + columnType);
				System.out.println("获得列" + i + "的数据类型名:" + columnTypeName);
				System.out.println("获得列" + i + "所在的Catalog名字:" + catalogName);
				System.out.println("获得列" + i + "对应数据类型的类:" + columnClassName);
				System.out.println("获得列" + i + "在数据库中类型的最大字符个数:" + columnDisplaySize);
				System.out.println("获得列" + i + "的默认的列的标题:" + columnLabel);
				System.out.println("获得列" + i + "的模式:" + schemaName);
				System.out.println("获得列" + i + "类型的精确度(类型的长度):" + precision);
				System.out.println("获得列" + i + "小数点后的位数:" + scale);
				System.out.println("获得列" + i + "对应的表名:" + tableName4Rs);
				System.out.println("获得列" + i + "是否自动递增:" + isAutoInctement);
				System.out.println("获得列" + i + "在数据库中是否为货币型:" + isCurrency);
				System.out.println("获得列" + i + "是否为空:" + isNullable);
				System.out.println("获得列" + i + "是否为只读:" + isReadOnly);
				System.out.println("获得列" + i + "能否出现在where中:" + isSearchable);*/
				columnVos.add(columnVo);
			}
			return columnVos;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (connt != null)
					connt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
