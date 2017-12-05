package cn.com.flaginfo.listener.com.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by duanyunhu on 2017/12/5.
 */
public abstract class Producer {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public enum ProducerType {
        rocket, active, zero;
    }

    static final ConcurrentHashMap<ProducerType, Producer> holder = new ConcurrentHashMap<ProducerType, Producer>();

    /**
     * 生成单例的Producer
     *
     * @param type 类型，选择需要的MQ类型
     * @return
     */
    public static Producer getInstance(ProducerType type) {
        Producer p = (Producer) holder.get(type);
        if (p == null) {
            synchronized (holder) {
                p = (Producer) holder.get(type);
                if (p != null) {
                    return p;
                }
                if (type == ProducerType.rocket) {
                    p = new RocketProducer();
                    holder.put(type, p);
                }
            }
        }
        return p;
    }

    public static Producer getInstance() {
        return getInstance(ProducerType.rocket);
    }

    protected Producer() {
        init();
    }

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * 发送消息
     *
     * @param topicName
     * @param body
     * @return
     */
    public abstract SendResultInfo sendMessage(String topicName, String body);

    /**
     * 发送消息
     *
     * @param topicName
     * @param tags      标签
     * @param keys      关键字，尽量保持唯一性，方便以后查询。在实际的业务使用中，例如taskId、messageId等
     * @param body      内容
     * @return
     */
    public abstract SendResultInfo sendMessage(final String topicName, final String tags, final String keys, String body);

    /**
     * 自动转JSON格式的消息
     *
     * @param topicName
     * @param body
     * @return
     */
    public abstract SendResultInfo sendJsonMessage(final String topicName, final String keys, final Object body);


}
