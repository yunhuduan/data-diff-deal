package cn.com.flaginfo.listener.com.support;

import cn.com.flaginfo.listener.utils.DynamicProperties;

/**
 * 信息改变监听者
 * @author Rain
 *
 */
public abstract class MessageChangeLinstener {

	public MessageChangeLinstener(){
		DynamicProperties.getInstance().addChangeLinstener(this);
	}

	/**
	 * 监听什么数据的变动，返回的key值为配置变量的startWith匹配
	 * @return
	 */
	public abstract String[] register();

	public abstract void change();



}
