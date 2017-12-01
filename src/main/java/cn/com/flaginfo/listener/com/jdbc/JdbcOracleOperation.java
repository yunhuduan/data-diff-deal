package cn.com.flaginfo.listener.com.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 仿照Hibernate中query，目标是代码结构查询简单，方便易用
 * 1.简化sql条件拼接
 * 2.封装查询
 * @author Rain
 *
 */
public class JdbcOracleOperation extends JdbcOperation {
	
	
	/**
	 * 执行分页查询
	 */
	@Override
	public <T> List<T> list(Class<T> clazz) {
		
		String finalSql = this.parseSql();
		String countSql = "select count(1) C from (" + finalSql + ")";
		
		Map<String,Object> countMap = null;
		try {
			countMap =  this.getJdbcTemplate().queryForMap(countSql,args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if(countMap == null || (countMap != null && countMap.isEmpty())
				|| ((BigDecimal)countMap.get("c")).intValue() == 0
				){
			queryInfo.setTotal(0);
			return new ArrayList<T>();
		}
		finalSql = finalSql + orderBy.toString();
		String querySql = "select * from (select * from (select row_.*, rownum rownum_ from (" + finalSql
				+ ") row_  ) a where Upper##% )where Lower##% ";
		if (queryInfo.getOffset() == null && queryInfo.getPageLimit() == null) {
			querySql = "select row_.*, rownum rownum_ from (" + querySql + ") row_";
		} else if (queryInfo.getOffset() != null && queryInfo.getPageLimit() == null) {
			int initIndx = queryInfo.getOffset() == null ? 0 : queryInfo.getOffset().intValue();
			querySql = querySql.replace("Upper##%", "1=1");
			querySql = querySql.replace("Lower##%", "rownum_ >" + initIndx);
		} else if (queryInfo.getOffset() == null && queryInfo.getPageLimit() != null) {
			int limit = queryInfo.getPageLimit() == null ? 0 : queryInfo.getPageLimit().intValue();
			querySql = querySql.replace("Upper##%", "a.rownum_ <=" + limit);
			querySql = querySql.replace("Lower##%", "rownum_ > 0");
		} else {
			int initIndx = queryInfo.getOffset() == null ? 0 : queryInfo.getOffset().intValue();
			int limit = queryInfo.getPageLimit() == null ? 0 : queryInfo.getPageLimit().intValue();
			querySql = querySql.replace("Upper##%", "a.rownum_ <=" + (limit + initIndx));
			querySql = querySql.replace("Lower##%", "rownum_ > " + initIndx);
		}
		List<T> list = this.getJdbcTemplate().query(querySql,args.toArray(),getMappingRowMapper());
		return list;
	}

}
