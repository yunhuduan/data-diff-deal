package cn.com.flaginfo.listener.com.jdbc;

import cn.com.flaginfo.listener.com.support.PartialCollection;
import cn.com.flaginfo.listener.com.support.QueryInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作，替代Hibernate
 * 提供查询、删除、插入、更新等一系列操作数据库方法
 * @author Rain
 *
 */
public interface DBOperation {
	
	
	/**
	 * 查询返回pojo Bean
	 * @param clazz
	 * @return
	 */
	<T> List<T> list(Class<T> clazz);
	
	/**
	 * 返回分页数据
	 * @param clazz
	 * @return
	 */
	<T> PartialCollection<T> listPartial(Class<T> clazz);
	
	/**
	 * 查询分页数据，默认结果集为Map作为对象
	 * @return
	 */
	PartialCollection listPartial();
	
	/**
	 * 配置数据库别名
	 * @param dbAlias
	 * @return
	 */
	DBOperation setDBAlias(String dbAlias);
	
	/**
	 * 获取当前操作的数据库
	 * @return
	 */
	String getDBAlias();
	
	/**
	 * 设置返回
	 * @param <T>
	 * @return
	 */
	<T> DBOperation setRowClass(Class<T> clazz);
	
	/**
	 * 添加sql语句
	 * @param startSql
	 * @return
	 */
	DBOperation appendSql(String startSql);
	
	/**
	 * 设置条件的值，如果为空，sql中的动态条件会被替换
	 * @param key
	 * @param value
	 * @return
	 */
	DBOperation setParameter(String key, Object value);

	/**
	 * 设置条件的值，如果值为空，会抛出Runtime异常
	 * @param key 对应条件
	 * @param value 条件的值
	 * @param required 是否必须
	 * @return
	 */
	DBOperation setParameter(String key, Object value, boolean required);

	/**
	 * 设置like的值
	 * @param key
	 * @param value
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	DBOperation setLike(String key, String value, String prefix, String suffix);

	/**
	 * 设置分页信息
	 * @param queryInfo
	 * @return
	 */
	DBOperation setQueryInfo(QueryInfo queryInfo);


	/**
	 * 设置时间
	 * @param key
	 * @param date
	 * @return
	 */
	DBOperation setDate(String key, Date date);

	/**
	 * 设置字符日期，格式采用默认值yyyy-MM-dd hh24:mi:ss
	 * @param key
	 * @param date
	 * @return
	 */
	DBOperation setDateString(String key, String date);

	/**
	 * 设置In值
	 * @param key
	 * @param required 是否为必须
	 * @param values 值
	 * @return
	 */
	DBOperation setIn(String key, boolean required, Object... values);

	/**
	 * 设置In值
	 * @param key
	 * @param values
	 * @return
	 */
	DBOperation setIn(String key, Object... values);

	/**
	 * 设置order by
	 * @param columnName
	 * @param order
	 * @return
	 */
	DBOperation setOrderBy(String columnName, String order);

	/**
	 * 查询接口
	 * @return
	 */
	List<?> list();

	/**
	 * 返回单一结果集，支持对象pojo、单一型：java.lang.*下的类、java.util.Date
	 * @param clazz
	 * @return
	 */
	<T> T uniqueResult(Class<T> clazz);

	/**
	 * 默认返回Map结果集
	 * @return
	 */
	Map<String,Object> uniqueResult();

	/**
	 * 保存对象，obj可以为Map、pojo
	 * @param obj
	 * @param table
	 */
	void save(Object obj, String table);

	/**
	 * 更新对象
	 * @param obj
	 * @param table
	 */
	void update(Object obj, String table);

	/**
	 * 按照条件来更新
	 * @param setValues
	 * @param condValues
	 * @param table
	 */
	void update(Map<String, Object> setValues,
                Map<String, Object> condValues,
                String table);
	
	/**
	 * 执行更新操作，删除、更新都可以使用
	 */
	void executeUpdate();
	
	
}
