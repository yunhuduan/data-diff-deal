package cn.com.flaginfo.listener.service;

import cn.com.flaginfo.listener.com.base.BaseService;
import cn.com.flaginfo.listener.com.jdbc.DBOperation;
import cn.com.flaginfo.listener.com.support.PartialCollection;
import cn.com.flaginfo.listener.com.support.QueryInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by duanyunhu on 2017/11/30.
 */
@Service("taskService")
public class TaskService extends BaseService {

    public PartialCollection getDataDiffList(String dbName, Map<String, Object> params, QueryInfo info) {
        String sql = "select id,table_id,type,table_name from im_data_diff_listener order by id asc";
        return createDBOperation(sql, dbName).setQueryInfo(info).listPartial();
    }

    public void delDiffData(String dbName, List<String> ids) {
        DBOperation op = createDBOperation("delete from im_data_diff_listener where id = :id", dbName);
        for (int i = 0; i < ids.size(); i++) {
            op.setParameter("id", ids.get(i), true);
            op.executeUpdate();
        }
    }

    public void saveTest(String db) {
        Random rd = new Random();
        int id = rd.nextInt(100);
        Map<String, Object> test = new HashMap<String, Object>();
        test.put("id", id);
        test.put("name", "t" + id);
        test.put("phone", id + "3344");
        createDBOperation("").setDBAlias(db).save(test, "im_trg_test");

    }

}
