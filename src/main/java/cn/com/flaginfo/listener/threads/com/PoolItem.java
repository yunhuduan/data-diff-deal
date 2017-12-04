package cn.com.flaginfo.listener.threads.com;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by duanyunhu on 2017/12/4.
 * 存放在pool中的每个线程需要实现的父类
 */
public abstract class PoolItem extends Thread {

    public Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 当前线程运行状态
     */
    protected boolean runing = true;

    /**
     * 当前线程运行时需要的参数
     */
    protected Map<String, Object> params;

    /**
     * 当前线程对应到线程池中的名称
     */
    protected String itemName;

    public PoolItem(String itemName,Map<String, Object> params) {
        this.itemName = itemName;
        this.params = params;
    }

    public PoolItem() {

    }

    /**
     * 线程池轮询过程中是否需要更新当前线程的参数
     * @return
     */
    protected boolean paramsNeedUpdate(){
        return false;
    };

    /**
     * 获取当前线程更新参数
     * @return
     */
    public Map<String,Object> getUpdatedParams(){
        return this.params;
    };

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public boolean isRuning() {
        return runing;
    }

    public void setRuning(boolean runing) {
        this.runing = runing;
    }


}
