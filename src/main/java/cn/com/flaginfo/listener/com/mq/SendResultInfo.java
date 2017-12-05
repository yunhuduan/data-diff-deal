package cn.com.flaginfo.listener.com.mq;

/**
 * Created by duanyunhu on 2017/12/5.
 */
public class SendResultInfo {

    private boolean isSucc = false;

    private String msgId;

    public boolean isSucc() {
        return isSucc;
    }

    public void setSucc(boolean isSucc) {
        this.isSucc = isSucc;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "isSucc=" + isSucc + ",msg=" + getMsgId();
    }


}
