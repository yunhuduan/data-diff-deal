package cn.com.flaginfo.listener.com.jdbc;

import cn.com.flaginfo.listener.com.support.MessageChangeLinstener;
import cn.com.flaginfo.listener.utils.PropertiesUtil;
import cn.com.flaginfo.listener.utils.StringUtil;
import cn.com.flaginfo.listener.utils.SystemMessage;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DataSourceConfig extends MessageChangeLinstener{
	
	private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
	
	private final String [] registerKey = new String[]{"spring.datasource."};
	
	private final static String DEFAULT_DBNAME = "default";
	
	private String [] dbnameArray;
	
	private Map<String,DataSource> datasourceMapping = new HashMap<String,DataSource>();
	
	private Map<String,JdbcTemplate> jdbcTemplateMapping = new HashMap<String,JdbcTemplate>();
	
	
	public DataSourceConfig() {
		super();
		String dbnames = SystemMessage.getString("spring.datasource.names");
		if(StringUtil.isNullOrEmpty(dbnames)){
			dbnames = DEFAULT_DBNAME;
		}
		dbnameArray = dbnames.split(",");
		configDataSource();
	}
	
	@Override
	public void change() {
		logger.info("datasource properties change");
		configDataSource();
	}
	
	@Override
	public String[] register() {
		return registerKey;
	}
	
	@Bean
	@Primary
	public DataSource dataSource(){
		return datasourceMapping.get(DEFAULT_DBNAME);
	}
	
	@Bean
	@Primary
	public JdbcTemplate jdbcTemplate(){
		return jdbcTemplateMapping.get(DEFAULT_DBNAME);
	}
	
	private void configDataSource(){
		
		for(String key:dbnameArray){
			DruidDataSource ds = (DruidDataSource)datasourceMapping.get(key);
			if(ds == null){
				ds = new DruidDataSource();
				datasourceMapping.put(key,ds);
				JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
				jdbcTemplateMapping.put(key, jdbcTemplate);
			}
			Properties config = PropertiesUtil.filterByPrefix(SystemMessage.getAllConfig(), registerKey[0]);
			config.putAll(PropertiesUtil.filterByPrefix(SystemMessage.getAllConfig(), registerKey[0]+key+"."));
			logger.info("key="+key+" config:"+config);
			try {
				ds.close();
				ds.restart();
				DruidDataSourceFactory.config(ds, config);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/**
		 * 反注册进去，防止shutdown的时候内存溢出
		 */
		try {
            DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	/**
	 * 获取jdbcTemplate
	 * @param dbname
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate(String dbname){
		if(StringUtil.isNotNullOrEmpty(dbname) && !"default".equals(dbname)){
			return (JdbcTemplate)jdbcTemplateMapping.get(dbname);
		}
		return (JdbcTemplate)jdbcTemplateMapping.get(DEFAULT_DBNAME);
	}
	
		
}
