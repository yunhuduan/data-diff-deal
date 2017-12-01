package cn.com.flaginfo.listener.threads;

import cn.com.flaginfo.listener.DataDiffDealApplication;
import cn.com.flaginfo.listener.com.bean.BeanFactory;
import cn.com.flaginfo.listener.com.support.PartialCollection;
import cn.com.flaginfo.listener.com.support.QueryInfo;
import cn.com.flaginfo.listener.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by duanyunhu on 2017/12/1.
 */
public class DataDiffThread extends Thread {

    private String dbName;

    private final Logger log = LoggerFactory.getLogger(DataDiffThread.class);

    private TaskService taskService;

    public DataDiffThread(String dbName) {
        super(dbName);
        this.dbName = dbName;
        taskService = (TaskService) BeanFactory.getBean("taskService");
    }

    @Override
    public void run() {

        while (true) {
            try {
                log.info("*************>>[" + this.dbName + "]dataDiffThread exec start****************");

                PartialCollection res = taskService.getDataDiffList(this.dbName, null, new QueryInfo());
                List list = res.getRealDataSet();
                List<String> ids = new ArrayList<String>(list.size());
                for (int i = 0; i < list.size(); i++) {
                    Map row = (Map) list.get(i);
                    String id = (String) row.get("id");
                    String tableId = (String) row.get("tableId");
                    String type = (String) row.get("type");
                    String tableName = (String) row.get("tableName");
                    log.info("====>>>read row:" + id + "," + type + "," + tableName + "," + tableId);
                    ids.add(id);
                }

                taskService.delDiffData(this.dbName, ids);

                log.info("*************>>[" + this.dbName + "]dataDiffThread exec end****************");

                //taskService.saveTest(this.dbName);
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
