package cn.com.flaginfo.listener.com.support;

import cn.com.flaginfo.listener.com.bean.BeanUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 会话holder<br/>
 * 一次会话可以加入N个对象存储
 * @author Rain
 *
 */
@SuppressWarnings("unchecked")
public class SessionHolder {
	
	private static ThreadLocal<Map<String,Object>> local = new ThreadLocal<Map<String,Object>>();
	
	public static void set(Object o){
		Map<String,Object> curThreadValue = local.get();
		if(curThreadValue == null){
			curThreadValue = new HashMap<String,Object>();
			local.set(curThreadValue);
		}
		curThreadValue.put(o.getClass().getName(),o);
	}
	
	/**
	 * 获取每次会话中的holder的对象
	 * @param clazz
	 * @return
	 */
	public static <T> T get(Class<T> clazz){
		Map<String,Object> curThreadValue = local.get();
		if(curThreadValue == null){
			return null;
		}
		return (T)curThreadValue.get(clazz.getName());
	}
	
	/**
	 * 获取holder中对象的值
	 * @param clazz
	 * @param objectField
	 * @return
	 */
	public static <T> Object get(Class<T> clazz,String objectField){
		T t = get(clazz);
		return BeanUtil.getPropertyValue(t, objectField);
	}
	
	public static void remove(Class clazz){
		Map<String,Object> curThreadValue = local.get();
		if(curThreadValue == null){
			return;
		}
		curThreadValue.remove(clazz.getName());
	}
	
	public static void remove(){
		local.remove();
	}
}
