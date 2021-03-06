package cn.com.flaginfo.listener;

import cn.com.flaginfo.listener.threads.DataDiffPoolManage;
import cn.com.flaginfo.listener.web.init.PropertiesSourceInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class DataDiffDealApplication {

    private static final Logger log = LoggerFactory.getLogger(DataDiffDealApplication.class);


    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(DataDiffDealApplication.class);
        sa.addInitializers(new PropertiesSourceInitializer());
        sa.run(args);
        log.info("server start up end");
        new DataDiffPoolManage(10000).start();

    }

}
