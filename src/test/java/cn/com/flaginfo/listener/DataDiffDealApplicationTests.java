package cn.com.flaginfo.listener;

import cn.com.flaginfo.listener.com.support.PartialCollection;
import cn.com.flaginfo.listener.com.support.QueryInfo;
import cn.com.flaginfo.listener.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataDiffDealApplicationTests {

	@Autowired
	TaskService taskManager;

	@Test
	public void contextLoads() {
		PartialCollection res = taskManager.getDataDiffList("db_zx", null, new QueryInfo());
		System.out.println(res.getRealDataSet());
	}

}
