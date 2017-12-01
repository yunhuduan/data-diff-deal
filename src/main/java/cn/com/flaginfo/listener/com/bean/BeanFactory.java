package cn.com.flaginfo.listener.com.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

@Component
public class BeanFactory implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		applicationContext = ac;
		
	}
	
	public static <T> T getBean(Class<T> beanClass){

		return applicationContext.getBean(beanClass);
	}
	
	public static Object getBean(String name){
		return applicationContext.getBean(name);
	}
	
	public static Map<String,Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType){
		return applicationContext.getBeansWithAnnotation(annotationType);
	}

}
