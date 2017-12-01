package cn.com.flaginfo.listener.com.jdbc;

import cn.com.flaginfo.listener.com.bean.BeanFactory;
import cn.com.flaginfo.listener.com.bean.BeanUtil;
import cn.com.flaginfo.listener.com.support.PartialCollection;
import cn.com.flaginfo.listener.com.support.QueryInfo;
import cn.com.flaginfo.listener.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.util.Assert;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 仿照Hibernate中query，目标是代码结构查询简单，方便易用 1.简化sql条件拼接 2.封装查询
 * 
 * @author Rain
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class JdbcOperation implements DBOperation {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected static final Pattern PATTERN_WHERE = Pattern.compile(
			"\\s+(where|and|or|start\\s+with)[\\s\\(]+\\S+\\s*([>|=|<]{1,2}|(like)|in|not\\s+in)\\s*:([\\S&&[^\\(\\)]]+)|\\s+(where|and|or)\\s+instr\\s*\\(\\s*\\S+\\s*,\\s*:(\\S+)\\s*\\)\\s*[><=]{1,2}\\s*\\d");
	protected static final Pattern PATTERN_SET = Pattern.compile("(\\s+set|,)\\s*(?:\\w+\\.)?\\w+\\s*=\\s*:(\\w+)");
	protected static final Pattern PATTERN_FUNC = Pattern.compile("(?<=\\(|,)\\s*:(\\w+)");
	
	private String dbAlias;
	
	private DataSourceConfig config = BeanFactory.getBean(DataSourceConfig.class);
	
	protected JdbcTemplate getJdbcTemplate(){
		return config.getJdbcTemplate(this.getDBAlias());
	}

	protected QueryInfo queryInfo;

	protected Class rowClass;

	public Class getRowClass() {
		return rowClass;
	}

	public DBOperation setRowClass(Class rowClass) {
		this.rowClass = rowClass;
		return this;
	}

	// 参数
	protected Map<String, Object> paramsMap = new HashMap<String, Object>();
	protected Map<String, Object> dateMap = new HashMap<String, Object>();
	protected Map<String, Object> inMap = new HashMap<String, Object>();

	protected List<Object> args = new ArrayList<Object>();

	protected List<String> orderByList = new ArrayList<String>();

	protected StringBuffer orderBy = new StringBuffer();

	protected StringBuffer sql = new StringBuffer();

	public JdbcOperation() {

	}

	public JdbcOperation(String startSql) {
		sql.append(startSql);
	}
	
	public JdbcOperation(String startSql,String dbAlias) {
		sql.append(startSql);
		this.dbAlias = dbAlias;
	}
	
	@Override
	public DBOperation setDBAlias(String dbAlias) {
		this.dbAlias = dbAlias;
		return this;
	}
	
	@Override
	public String getDBAlias() {
		return this.dbAlias;
	}
	

	public DBOperation appendSql(String startSql) {
		sql.append(startSql);
		return this;
	}

	@Override
	public DBOperation setQueryInfo(QueryInfo queryInfo) {
		this.queryInfo = queryInfo;
		return this;
	}

	public DBOperation setParameter(String key, Object value) {
		this.setParameter(key, value, false);
		return this;
	}

	public DBOperation setParameter(String key, Object value, boolean required) {
		if (required && (value == null || "".equals(value))) {
			throw new RuntimeException("sql parameter:" + key + " is null");
		}
		this.paramsMap.put(key, value);
		return this;
	}

	public DBOperation setIn(String key, Object... values) {
		return setIn(key, false, values);
	}

	public DBOperation setIn(String key, boolean required, Object... values) {
		if (values == null || values.length == 0) {
			if (required) {
				throw new RuntimeException("sql parameter:" + key + " is null");
			}
			return this;
		}
		List inList = new ArrayList(values.length);
		for (Object value : values) {
			if (value == null || "".equals(value)) {
				continue;
			}
			inList.add(value);
		}
		if (inList.size() > 0) {
			// this.paramsMap.put(key,inList);
			inMap.put(key, inList);
			setParameter(key, values, required);
		}
		return this;
	}

	public DBOperation setOrderBy(String columnName, String order) {
		if (order == null) {
			order = "asc";
		}
		orderByList.add(columnName + " " + order);
		return this;
	}

	public DBOperation setLike(String key, String value, String prefix, String suffix) {
		if (value == null || "".equals(value)) {
			return this;
		}
		if (prefix == null)
			prefix = "";
		if (suffix == null)
			suffix = "";
		this.paramsMap.put(key, prefix + value + suffix);
		return this;
	}

	public DBOperation setDateString(String key, String date, String dateFormat) {

		if (date != null && !"".equals(date)) {
			// date = "to_date('"+date+"',"+dateFormat+"')";
			dateMap.put(key, dateFormat);
		}
		setParameter(key, date);
		return this;
		// return setDate(key,DateUtil.getDate(date));
	}

	protected void assertRequired(String key, boolean required, Object... value) {
		if (required && (value == null || "".equals(value))) {
			throw new RuntimeException("sql parameter:" + key + " is null");
		}
	}

	public DBOperation setDateString(String key, String date, String dateFormat, boolean required) {

		assertRequired(key, required, date);
		this.setDateString(key, date, dateFormat);
		return this;
	}

	@Override
	public DBOperation setDate(String key, Date date) {
		if (null != date) {
			return this.setDateString(key, DateUtil.fmtDate(date));
		}
		return this;
	}

	public DBOperation setDateString(String key, String date) {
		return this.setDateString(key, date, "yyyy-MM-dd hh24:mi:ss");
	}

	public String parseSql() {
		String finalSql = sql.toString();
		finalSql = finalSql.replaceAll("\\s{2,}", " ");

		for (String order : orderByList) {
			if ("".equals(orderBy.toString())) {
				orderBy.append(" order by ").append(order);
			} else {
				orderBy.append(",").append(order);
			}
		}
		if (orderByList == null || orderByList.size() == 0) {

		}
		
		Matcher set = PATTERN_SET.matcher(finalSql);
		while (set.find()) {
			String conds = set.group();
			String key = set.group(2);
			Object value = this.paramsMap.get(key);
			String replace = "?";
			if (dateMap.get(key) != null) {
				replace = "to_date(?,'" + dateMap.get(key) + "')";
			}
			replace = conds.replace(":" + key, replace);
			finalSql = finalSql.replaceAll(conds, replace);
			args.add(value);
		}

		Matcher func = PATTERN_FUNC.matcher(finalSql);
		while (func.find()) {
			String conds = func.group();
			String key = func.group(1);
			Object value = this.paramsMap.get(key);
			String replace = "?";
			if (dateMap.get(key) != null) {
				replace = "to_date(?,'" + dateMap.get(key) + "')";
			}
			replace = conds.replace(":" + key, replace);
			finalSql = finalSql.replaceAll(conds, replace);
			args.add(value);
		}

		Matcher where = PATTERN_WHERE.matcher(finalSql);
		while (where.find()) {
			String conds = where.group();
			String key = null;
			if (where.group(4) != null) {
				key = where.group(4);
			} else if (where.group(6) != null) {
				key = where.group(6);
			} else {
				throw new RuntimeException("parse sql exception,can't find the key");
			}
			conds = conds.replaceAll("\\s*where\\s+", "").replaceAll("\\s*(and|or)\\s+\\(", "");
			Object value = this.paramsMap.get(key);
			if (value == null || "".equals(value)) {
				finalSql = finalSql.replace(conds, "");
			} else {
				String replace = " ? ";
				if (dateMap.get(key) != null) {
					replace = "to_date(?,'" + dateMap.get(key) + "')";
				}
				if (inMap.get(key) != null) {
					List<String> inList = (List) inMap.get(key);
					replace = "(";
					for (Object in : inList) {
						if (replace.equals("(")) {
							replace = replace + "?";
						} else {
							replace = replace + ",?";
						}
						args.add(in);
					}
					replace = replace + ")";

				} else {
					args.add(value);
				}
				finalSql = finalSql.replaceFirst(":" + key, replace);
			}
		}
		finalSql = finalSql.replaceAll("where\\s+\\)", "where ").replaceAll("where\\s+and", " where ")
				.replaceAll("where\\s*$", "").replaceAll("where\\s+order\\s+", " order ")
				.replaceAll("(and|or)\\s+\\(\\s*\\)\\s*", " ").replaceAll("\\s+\\(\\s+(and|or)\\s+", " ( ");
		logger.debug("====" + finalSql + ";args=" + args);

		return finalSql;
	}

	/**
	 * 查询数据转换成指定的clazz
	 */
	@Override
	public <T> List<T> list(Class<T> clazz) {
		this.setRowClass(clazz);
		String finalSql = this.parseSql();
		finalSql = finalSql + orderBy.toString();
		List<T> list = this.getJdbcTemplate().query(finalSql, args.toArray(), getMappingRowMapper());
		return list;
	}
	
	/**
	 * get mapping return Mapper
	 * @return
	 */
	protected RowMapper getMappingRowMapper() {
		return getMappingRowMapper(this.rowClass);
	}
	
	/**
	 * 计算返回的RowMapper类型
	 * @param clazz
	 * @return
	 */
	protected RowMapper getMappingRowMapper(Class clazz) {
		RowMapper mapper = null;
		if (clazz == null || Map.class.isAssignableFrom(clazz)) {
			mapper = (RowMapper) new MapRowMapper();
		} else if(BeanUtils.isSimpleValueType(clazz)
				){
			mapper = new SingleColumnRowMapper(clazz);
		}else{
			mapper = new BeanPropertyRowMapper(clazz);
		}
		return mapper;
	}

	@Override
	public List list() {
		return this.list(Map.class);
	}
	
	@Override
	public <T> PartialCollection<T> listPartial(Class<T> clazz) {
		Assert.notNull(this.queryInfo);
		List<T> list = this.list(clazz);
		return new PartialCollection<T>(list,queryInfo.getTotal(),this.queryInfo.getOffset());
	}
	
	public PartialCollection listPartial() {
		return listPartial(Map.class);
	}

	public void executeUpdate() {
		String finalSql = this.parseSql();
		this.getJdbcTemplate().update(finalSql, args.toArray());
	}

	@Override
	public void save(Object obj, String table) {

		Map<String, Object> insertMap = null;
		if (!(obj instanceof Map)) {
			insertMap = BeanUtil.convertBeanToDBMap(obj);
		} else {
			insertMap = (Map<String, Object>) obj;
		}
		sql.append("insert into ").append(table).append("(");
		Set<String> keySet = insertMap.keySet();
		boolean isFirst = true;
		List<Object> args = new ArrayList<Object>(insertMap.size());
		StringBuffer valuesSql = new StringBuffer();
		for (String key : keySet) {
			String replace = "?";
			Object value = insertMap.get(key);
			key = JdbcFieldUtil.changeFieldToColumnName(key);
			if (value != null) {
				if (value instanceof Date) {
					value = new java.sql.Timestamp(((Date) value).getTime());
				}
			}
			if ("".equals(value)) {
				value = null;
			}
			args.add(value);
			if (isFirst) {
				isFirst = false;
				sql.append(key);
				valuesSql.append(replace);
			} else {
				sql.append(",").append(key);
				valuesSql.append(",").append(replace);
			}
		}
		sql.append(") values (").append(valuesSql).append(")");
		this.getJdbcTemplate().update(sql.toString(), args.toArray());
		
	}
	
	public void update(Map<String,Object> setValues,
			Map<String,Object> condValues,
			String table){
		sql.append("update ").append(table).append(" set ");
		Set<String> keySet = setValues.keySet();
		boolean isFirst = true;
		List<Object> args = new ArrayList<Object>(setValues.size());
		for (String key : keySet) {
			Object value = setValues.get(key);
			key = JdbcFieldUtil.changeFieldToColumnName(key);
			if ("".equals(value)) {
				value = null;
			}
			args.add(value);
			if (isFirst) {
				isFirst = false;
				sql.append(key).append("=?");
			} else {
				sql.append(",").append(key).append("=?");
			}
		}
		isFirst = true;
		
		for (String key : condValues.keySet()) {
			Object value = condValues.get(key);
			key = JdbcFieldUtil.changeFieldToColumnName(key);
			if (isFirst) {
				isFirst = false;
				sql.append(" where ").append(key).append("=?");
			} else {
				sql.append(" and ").append(key).append("=?");
			}
			args.add(value);
		}
		this.getJdbcTemplate().update(sql.toString(), args.toArray());
	}
	
	public void update(Object obj, String table) {

		Map<String, Object> updateMap = null;
		if (!(obj instanceof Map)) {
			updateMap = BeanUtil.convertBeanToDBMap(obj);
		} else {
			updateMap = (Map<String, Object>) obj;
		}
		Object id = updateMap.remove("id");
		Map<String,Object> condsMap = new HashMap();
		condsMap.put("id", id);
		this.update(updateMap,condsMap,table);
	}
	
	@Override
	public Map<String,Object> uniqueResult() {
		return uniqueResult(Map.class);
	}

	@Override
	public <T> T uniqueResult(Class<T> clazz) {
		if(BeanUtils.isSimpleValueType(clazz)){
			return uniqueSingleColumnResult(clazz);
		}
		return uniqueBeanResult(clazz);
	}
	
	/**
	 * 返回bean结果
	 * @return
	 */
	private <T> T uniqueBeanResult(Class clazz){
		List<T> list = this.list(clazz);
		if(list == null || list.size()==0){
			return null;
		}
		return list.get(0);
	}
	
	
	private <T> T uniqueSingleColumnResult(Class clazz){
		
		Map<String,Object> rMap = uniqueBeanResult(Map.class);
		if(rMap==null || rMap.size()==0){
			return null;
		}
		Set<String> set = rMap.keySet();
		Object value = null;
		for (String key : set) {
			value = rMap.get(key);
		}
		if(value == null){
			return null;
		}
		if(value.getClass().isAssignableFrom(clazz)){
			return (T)value;
		}
		String stringValue = value.toString();
		if (Integer.class.equals(clazz)) {
            return (T) Integer.valueOf(stringValue);
        } else if (Boolean.class.equals(clazz)) {
            return (T) Boolean.valueOf(stringValue);
        } else if (Float.class.equals(clazz)) {
            return (T) Float.valueOf(stringValue);
        } else if (Long.class.equals(clazz)) {
            return (T) Long.valueOf(stringValue);
        } else if (Double.class.equals(clazz)) {
            return (T) Double.valueOf(stringValue);
        }else if (Date.class.equals(clazz)) {
            return (T) DateUtil.getDate(stringValue);
        }
		return (T)stringValue;
	}
	

}
