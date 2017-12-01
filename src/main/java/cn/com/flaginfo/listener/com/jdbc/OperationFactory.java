package cn.com.flaginfo.listener.com.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * 数据库Query生产
 * 目前一个项目中只支持配置一种数据库
 * @author Rain
 * 
 */
public class OperationFactory {
	
	//数据库类型
	protected static DBType dbType = null;
	
	static{
		if(dbType == null){
			Enumeration<Driver> e = DriverManager.getDrivers();
			dbType = DBType.MYSQL;
			while(e.hasMoreElements()){
				Object o = e.nextElement();
				if(o.getClass().getName().toLowerCase().contains("oracle")){
					dbType = DBType.ORACLE;
				}else if(o.getClass().getName().toLowerCase().contains("mysql")){
					dbType = DBType.MYSQL;
				}
				System.out.println(o.getClass().getName());
			}
		}
	}
	
	/**
	 * 产生Query
	 * 更具当前配置的数据库
	 * @return
	 */
	public static DBOperation getDBOperator(){
//		if(dbType == DBType.ORACLE){
//			return new JdbcOracleOperation();
//		}else if(dbType == DBType.MYSQL){
//			
//		}
//		throw new RuntimeException("不支持配置的数据库类型");
		return new JdbcOracleOperation();
	}
	
}
