package cn.com.flaginfo.listener.utils;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * 获取动态数据文件配置
 * @author Rain
 *
 */
public class SystemMessage {

	private static DynamicProperties properties = DynamicProperties.getInstance();

	public static Map<String,Object> getAllConfig() {
		return properties.getAllConfig();
	}

	public static String getString(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 获取配置，并将配置值转换为整数
	 *
	 * @param key
	 * @return
	 */
	public static Integer getInteger(String key) {
		String value = getString(key);
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为Long类型
	 *
	 * @param key
	 * @return
	 */
	public static Long getLong(String key) {
		String value = getString(key);
		try {
			return Long.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为Double类型
	 *
	 * @param key
	 * @return
	 */
	public static Double getDouble(String key) {
		String value = getString(key);
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为Float类型
	 *
	 * @param key
	 * @return
	 */
	public static Float getFloat(String key) {
		String value = getString(key);
		try {
			return Float.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为bool类型
	 *
	 * @param key
	 * @return
	 */
	public static Boolean getBoolean(String key) {
		String value = getString(key);
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取配置，并将配置值转换为BigDecimal类型
	 *
	 * @param key
	 * @return
	 */
	public static BigDecimal getBigDecimal(String key) {
		String value = getString(key);
		try {
			return new BigDecimal(value);
		} catch (Exception e) {
			return null;
		}
	}

}

