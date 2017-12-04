package cn.com.flaginfo.listener.threads.com;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by duanyunhu on 2017/12/4.
 */
public abstract class AbstractPoolManage extends Thread implements PoolManage {

    public Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * pool轮循时间间隔
     */
    protected int listenerTime = 20000;

    /**
     * 线程存放的池子
     */
    protected Map<String, PoolItem> pool = new HashMap<String, PoolItem>();

    /**
     * @param listenerTime 线程池轮询时间间隔单位:毫秒
     */
    public AbstractPoolManage(int listenerTime) {
        this.listenerTime = listenerTime;
    }

    @Override
    public PoolItem getItem(String itemName) {
        return this.pool.get(itemName);
    }

    @Override
    public void addItem(String itemName, PoolItem item) {
        if (this.pool.containsKey(itemName)) {
            logger.info("=====>>>threadPool add a exist item to pool,remove old itemName:" + itemName);
            this.removeItem(itemName);
        }
        item.setRuning(true);
        this.pool.put(itemName, item);
        item.start();
        logger.info("=====>>>threadPool add item to pool,itemName:" + itemName);
    }

    @Override
    public void removeItem(String itemName) {
        PoolItem poolItem = this.pool.remove(itemName);
        if (poolItem != null) {
            poolItem.setRuning(false);
            poolItem = null;
            logger.info("threadPool remove item finish itemName:" + itemName);
        } else {
            logger.info("threadPool not exist itemName:" + itemName + ",do not need remove");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                refereshThreadPool();
                logger.info("current thread pool size:" + pool.size());
                Thread.sleep(this.listenerTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 刷新当前线程池中的数量,维护池中线程状态
     */
    private void refereshThreadPool() {
        HashSet<String> itemNames = this.getPoolItemNames();
        if(itemNames == null){
            itemNames = new HashSet<String>();
        }
        if (itemNames.size() > 0) {
            //添加或者修改线程池中线程
            for (String itemName : itemNames) {
                if (!"".equals(itemName) && !this.pool.containsKey(itemName)) {
                    PoolItem item = this.createPoolItem(itemName);
                    this.addItem(itemName, item);
                } else {
                    PoolItem item = this.getItem(itemName);
                    if (item.paramsNeedUpdate()) {
                        item.setParams(item.getUpdatedParams());
                    }
                }
            }
        }

        //删除已经不需要监控的线程
        List<String> removeList = new ArrayList<String>();
        Iterator<String> it = this.pool.keySet().iterator();
        while (it.hasNext()) {
            String itemName = it.next();
            if (!itemNames.contains(itemName)) {
                removeList.add(itemName);
            }
        }

        for (int j = 0; j < removeList.size(); j++) {
            this.removeItem(removeList.get(j));
        }


    }
}
