package cn.com.flaginfo.listener.web.init;

import cn.com.flaginfo.listener.utils.DiamondProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by duanyunhu on 2017/11/30.
 */
public class PropertiesSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public PropertiesSourceInitializer() {
    }

    public void initialize(ConfigurableApplicationContext applicationContext) {
        DiamondProperties.initInstance(applicationContext.getEnvironment());
    }
}
