package cn.com.flaginfo.listener.com.base;

import cn.com.flaginfo.listener.com.bean.BeanUtil;
import cn.com.flaginfo.listener.com.jdbc.DBOperation;
import cn.com.flaginfo.listener.com.jdbc.IDGen;
import cn.com.flaginfo.listener.com.jdbc.OperationFactory;
import cn.com.flaginfo.listener.com.jdbc.Table;
import cn.com.flaginfo.listener.com.support.SessionHolder;
import cn.com.flaginfo.listener.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础方法不涉及业务熟悉
 * 
 * @author Rain
 *
 */
public class BaseService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IDGen idGen;

	protected DBOperation createDBOperation(String sql) {
		return OperationFactory.getDBOperator().appendSql(sql);
	}

	protected DBOperation createDBOperation(String sql, String dbAlias) {
		return OperationFactory.getDBOperator().appendSql(sql).setDBAlias(dbAlias);
	}


	public <T> T getSessionHolder(Class<T> clazz) {
		return SessionHolder.get(clazz);
	}


	/**
	 * 保存对象，默认主键字段为ID，如果没有该值，自动加入
	 * 
	 * @param o
	 * @param tableName
	 *            表名字
	 * @param dbAlias
	 *            库名
	 * 
	 * @return
	 */
	public String save(Object o, String tableName, String dbAlias) {
		Assert.notNull(o);
		Map<String, Object> insertObject = null;
		if (o instanceof Map) {
			insertObject = (Map) o;
		} else {
			insertObject = BeanUtil.convertBeanToDBMap(o);
		}
		String id = (String) insertObject.get("id");
		if (StringUtil.isNullOrEmpty(id)) {
			id = idGen.nextId();
			insertObject.put("id", id);
		}
		this.createDBOperation("", dbAlias).save(insertObject, tableName);
		return id;
	}



}
