package cn.com.flaginfo.listener.com.jdbc;

import cn.com.flaginfo.listener.utils.DateUtil;
import cn.com.flaginfo.listener.utils.StringUtil;
import cn.com.flaginfo.listener.utils.SystemMessage;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class MapRowMapper extends ColumnMapRowMapper {
	
	@Override
	public Map<String,Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, Object> mapOfColValues = createColumnMap(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
			Object obj = getColumnValue(rs, i);
			String type = SystemMessage.getString("server.dto-return-map-type");
			if(StringUtil.isNullOrEmpty(type) || "0".equals(type)){
				if(obj instanceof Number){
					obj = String.valueOf(obj);
				}else if(obj instanceof Date){
					obj = DateUtil.fmtDate((Date)obj);
				}
			}
			mapOfColValues.put(JdbcFieldUtil.changeColumnToFieldName(key), obj);
		}
		return mapOfColValues;
	}
	
	
}
