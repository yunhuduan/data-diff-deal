package cn.com.flaginfo.listener.com.jdbc;

import java.lang.annotation.*;

/**
 * orm中用来带注解对象使用的表名
 * @author Rain
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
	/**
	 * 表名
	 * @return
	 */
	public String name();
	
	
	
}
