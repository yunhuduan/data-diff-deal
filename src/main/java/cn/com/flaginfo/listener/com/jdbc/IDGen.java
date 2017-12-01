package cn.com.flaginfo.listener.com.jdbc;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * ID生成器<br/>
 * 默认调用url：http://id.flaginfo.net:8080/IDGen/next-id
 * @author Rain
 *
 */
@Component
public class IDGen {
	

	/**
	 * 获取单个id
	 * @return
	 */
	public String nextId(){
		List<String> data = getNewSource(1);
		if(data.isEmpty()){
			return "";
		}
		
		return getNewSource(1).iterator().next();
	}
	
	/**
	 * 批量获取id
	 * @param batchNum
	 * @return
	 */
	public List<String> nextIds(int batchNum){
		return getNewSource(batchNum);
	}
	
	private List<String> getNewSource(int batchNum){
		/*BaseResponse<List<String>> response = idGenApi.getNextId(batchNum);
		
		if(200 == response.getCode()){
			List<String> data = response.getData();
			if(data != null && !data.isEmpty()){
				return data;
			}
		}
		*/
		return new ArrayList<String>();
	}
	
}
