package cn.com.flaginfo.listener.threads;

import cn.com.flaginfo.listener.threads.com.AbstractPoolManage;
import cn.com.flaginfo.listener.threads.com.PoolItem;
import cn.com.flaginfo.listener.utils.SystemMessage;

import java.util.*;

/**
 * Created by duanyunhu on 2017/12/4.
 */
public class DataDiffPoolManage extends AbstractPoolManage {

    public DataDiffPoolManage(int listenerTime) {
        super(listenerTime);
    }

    @Override
    public HashSet<String> getPoolItemNames() {
        String dbs = SystemMessage.getString("listener_dbs");
        if(dbs == null || "".equals(dbs)){
            return new HashSet<String>();
        }
        HashSet<String> set = new HashSet<String>(Arrays.asList(dbs.split(",")));
        return set;
    }

    @Override
    public PoolItem createPoolItem(String itemName) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("dbName",itemName);
        return new DataDiffThread(itemName,params);
    }
}
