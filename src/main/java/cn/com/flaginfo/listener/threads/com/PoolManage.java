package cn.com.flaginfo.listener.threads.com;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by duanyunhu on 2017/12/4.
 */
public interface PoolManage {

    /**
     * 获取线程池中的线程名称
     * @return
     */
    HashSet<String> getPoolItemNames();

    /**
     * 生成poolItem
     * @param itemName
     * @return
     */
    PoolItem createPoolItem(String itemName);

    /**
     * 获取池中一个item
     * @param itemName
     * @return
     */
    PoolItem getItem(String itemName);

    /**
     * 添加一个item,自动运行
     * @param itemName
     * @param item
     */
    void addItem(String itemName, PoolItem item);

    /**
     * 移除一个item并设置item的运行状态为false
     * @param itemName
     */
    void removeItem(String itemName);

}
