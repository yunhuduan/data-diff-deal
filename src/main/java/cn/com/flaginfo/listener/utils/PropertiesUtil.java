package cn.com.flaginfo.listener.utils;

import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class PropertiesUtil {
	
	public static Properties filterByPrefix(Map<String,Object> sourceMap,String ...prefixs){
		if(sourceMap == null){
			return null;
		}
		Properties p = new Properties();
		Set<String> source = sourceMap.keySet();
		for(String prefix:prefixs){
			for(String key:source){
				if(key.startsWith(prefix)){
					p.put(key.substring(prefix.length(),key.length()), sourceMap.get(key));
				}
			}
		}
		return p;
	}
	
	
}
