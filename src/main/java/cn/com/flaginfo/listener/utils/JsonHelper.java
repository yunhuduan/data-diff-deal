package cn.com.flaginfo.listener.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * json对象辅助类
 * @author Rain
 *
 */
public class JsonHelper {
	/**
	 * 据说json==>对象是线程安全的
	 */
	public static ObjectMapper readMapper = new ObjectMapper();
	
	static{
		readMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,true);
		readMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		readMapper.setDateFormat(new SimpleDateFormat(DateUtil.defaultFormat));
	}
	
	public static <T> T parseToObject(InputStream is,Class<T> toClass){
		try {
			return (T)readMapper.readValue(is, toClass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T parseToObject(byte [] b,int offset, int len, Class<T> valueType){
		try {
			return (T)readMapper.readValue(b,offset,len, valueType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T parseToObject(String json,Class<T> toClass){
		try {
			if(StringUtil.isNullOrEmpty(json)){
				return null;
			}
			return (T)readMapper.readValue(json, toClass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static String getNodeValue(String json,String fieldName){
		try {
			if(StringUtil.isNullOrEmpty(json)){
				return null;
			}
			if(fieldName == null){
				return json;
			}
			JsonNode node = readMapper.readTree(json);
			JsonNode fnode = node.path(fieldName);
			return fnode.toString();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T parseToObject(String json,String fieldName,Class<T> toClass){
		return parseToObject(getNodeValue(json,fieldName), toClass);
	}
	
	public static Map parseToMap(String json){
		return parseToObject(json,Map.class);
	}
	
	public static Map parseToMap(byte []b){
		if(b==null || b.length==0){
			return null;
		}
		return parseToObject(b,0,b.length,Map.class);
	}
	
	public static Map parseToMap(InputStream is){
		return parseToObject(is,Map.class);
	}
	
	public static Map parseToMap(Object o){
	    String oJson = parseToJson(o);
        return parseToObject(oJson,Map.class);
    }
	
	/**
	 * 将对象转化成Json
	 * @param o 对象
	 * @return
	 */
	public static String parseToJson(Object o){
		return parseToJson(o,false);
	}
	
	/**
	 * 将对象转化成Json
	 * @param o
	 * @param ignoreNull 是否忽略对象中为null的属性
	 * @return
	 */
	public static String parseToJson(Object o,boolean ignoreNull){
		if(o==null){
			return null;
		}
		ObjectMapper writerMapper = new ObjectMapper();
		writerMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
		writerMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,ignoreNull);
		writerMapper.setSerializationInclusion(Include.NON_NULL);
		writerMapper.setDateFormat(new SimpleDateFormat(DateUtil.defaultFormat));
		try {
			return writerMapper.writeValueAsString(o);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	
	
}
