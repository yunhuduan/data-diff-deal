package cn.com.flaginfo.listener.com.mq;

import cn.com.flaginfo.listener.utils.JsonHelper;
import cn.com.flaginfo.listener.utils.SystemMessage;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duanyunhu on 2017/12/4.
 */
public class RocketProducer extends Producer {

    private static Logger logger = LoggerFactory.getLogger(RocketProducer.class);

    private DefaultMQProducer producer;

    public RocketProducer() {

    }

    @Override
    public void init() {
        try {
            producer = new DefaultMQProducer(SystemMessage.getString("rocket_mq_group")); //传入的组的名称
            producer.setNamesrvAddr(SystemMessage.getString("rocket_mq_address")); //ip+端口号
            logger.info("producer name address:" + SystemMessage.getString("rocket_mq_address"));
            producer.setInstanceName(getClass().getSimpleName() + hashCode()); //实例名称,区分不同的程序，分部署部署的时候需要区分
            producer.setHeartbeatBrokerInterval(60000);
            producer.setMaxMessageSize(2 * 1024 * 1024);
            producer.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    producer.shutdown();
                    logger.info("producer shutdown");
                    super.run();
                }
            });
            logger.info("init RocketMq succ");
        } catch (MQClientException e) {
            e.printStackTrace();
            throw new RuntimeException("初始化RocketMq失败，请检查配置");
        }
    }


    public SendResultInfo sendMessage(String topicName, String body) {
        return sendMessage(topicName, "", null, body);
    }

    public SendResultInfo sendMessage(final String topicName, final String tags, final String keys, String body) {
        if (body == null) {
            body = "";
        }
        Message message = new Message();
        message.setTopic(topicName);
        try {
            message.setBody(body.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        message.setKeys(keys);
        message.setTags(tags);
        return sendMessage(message);
    }


    public SendResultInfo sendJsonMessage(final String topicName, final String keys, final Object body) {
        return sendJsonMessage(topicName, "", keys, body);
    }

    public SendResultInfo sendJsonMessage(final String topicName, final String tags, final String keys, final Object body) {
        return sendMessage(topicName, tags, keys, JsonHelper.parseToJson(body));
    }

    private SendResultInfo sendMessage(Message message) {
        SendResultInfo info = new SendResultInfo();
        SendResult sendResult = null;
        try {
            sendResult = producer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            info.setSucc(true);
            info.setMsgId(sendResult.getMsgId());
        }
        logger.info("sendResult:{}", sendResult);
        return info;
    }

}
