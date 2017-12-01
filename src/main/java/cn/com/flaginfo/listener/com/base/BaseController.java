package cn.com.flaginfo.listener.com.base;

import cn.com.flaginfo.listener.com.support.DateEditor;
import cn.com.flaginfo.listener.com.support.QueryInfo;
import cn.com.flaginfo.listener.com.support.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@InitBinder
	protected void initBinder(HttpServletRequest request,
	                              ServletRequestDataBinder binder) throws Exception {
	    //对于需要转换为Date类型的属性，使用DateEditor进行处理
	    binder.registerCustomEditor(Date.class, new DateEditor());
	}
	
	protected <T> T getSessionHolder(Class<T> clazz){
		return SessionHolder.get(clazz);
	}
	
	
	public static QueryInfo getQueryInfo(Map requestMap) {
		QueryInfo queryInfo = new QueryInfo();
		if(requestMap ==null){
			requestMap = new HashMap() ;
		}
		Map pageInfo = (Map) requestMap.get("pageInfo");
		if (pageInfo == null) {
			pageInfo = new HashMap();
			pageInfo.put("curPage", 1);
			pageInfo.put("pageLimit", 10);
		}
		int curPage = (Integer) pageInfo.get("curPage");
		if (curPage > 0) {
			queryInfo.setCurPage(curPage);
		}
		queryInfo.setPageLimit((Integer) pageInfo.get("pageLimit"));
		String startId = (String) pageInfo.get("startId");
		queryInfo.setStartId(startId);
		
		return queryInfo;
	}
	
}
