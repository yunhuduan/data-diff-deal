package cn.com.flaginfo.listener.threads;

import cn.com.flaginfo.listener.DataDiffDealApplication;
import cn.com.flaginfo.listener.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by duanyunhu on 2017/12/1.
 */
@Component
public class DataDiffJob {

    private static final Logger log = LoggerFactory.getLogger(DataDiffDealApplication.class);

    /*@Scheduled(fixedDelay=2000)
    public void fixedDelayJob(){
        String time = DateUtil.fmtDate(new Date());
        log.info(time+" >>fixedDelay执行....");
        try {
            Thread.sleep(5000);
            log.info(time+" >>fixedDelay执行 end.....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate=2000)
    public void fixedRateJob(){
        String time = DateUtil.fmtDate(new Date());
        log.info(time+" >>fixedRate执行....");
        try {
            Thread.sleep(5000);
            log.info(time+" >>fixedRate执行 end.....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron="5/5 * * * * ?")
    public void cronJob(){
        log.info(DateUtil.fmtDate(new Date())+" >>cron执行....");
    }*/

}
