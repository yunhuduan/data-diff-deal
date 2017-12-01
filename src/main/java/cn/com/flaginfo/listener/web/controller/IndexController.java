package cn.com.flaginfo.listener.web.controller;


import cn.com.flaginfo.listener.com.support.PartialCollection;
import cn.com.flaginfo.listener.com.support.QueryInfo;
import cn.com.flaginfo.listener.service.TaskService;
import cn.com.flaginfo.listener.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by duanyunhu on 2017/11/30.
 */
@RestController
@RequestMapping(value = "/index")
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    TaskService taskManager;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    List getUserCustomers(@RequestParam(value = "db") String db) {
        logger.info("=====>>get db:" + db);
        if (StringUtil.isNullOrEmpty(db)) {
            db = "db_zx";
        }
        PartialCollection res = taskManager.getDataDiffList(db, null, new QueryInfo());
        return res.getRealDataSet();
    }
}
